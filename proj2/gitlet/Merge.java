package gitlet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

/*
    简历一个depth标签，给每一个commit
    然后每次merge直到depth相等， 再一起往上跑。
 */

public class Merge {

    public Merge( String mergeBranch) throws IOException, ClassNotFoundException {

        failureCases(mergeBranch);
        boolean judgeConflict = true;
        String currentBranch = Utils.readContentsAsString(Main.Head);
        File currentCommitFile = new File(Main.Objects, Utils.readContentsAsString(new File(Main.Branches, currentBranch)));
        File mergeCommitFile = new File(Main.Objects, Utils.readContentsAsString(new File(Main.Branches, mergeBranch)));
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(currentCommitFile));
        Commit currentCommit =  (Commit) in.readObject();
        in.close();

        ObjectInputStream in1 = new ObjectInputStream(new FileInputStream(mergeCommitFile));
        Commit mergeCommit = (Commit) in1.readObject();
        in1.close();

        String LCAID = LCA(currentCommitFile, mergeCommitFile);
        ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(new File(Main.Objects, LCAID)));
        Commit LCACommit = (Commit) in2.readObject();

        if (LCAID == mergeCommit.ID ){
            System.out.println("Given branch is an ancestor of the current branch.");
        }
        else if (LCAID == currentCommit.ID){
            new Checkout("", currentBranch);
            System.out.println("Current branch fast-forwarded.");
        }
        else if (mergeBranch == currentBranch){
            System.out.println("Cannot merge a branch with itself.");
        }

        else {
            /* Merge的锚点不如就设置为当前的最新的一次Commit */
            for (String LCAFileName : LCACommit.HashMapBlobs.keySet()) {

                /* 遍历全部的LCA文件，然后如果最新的版本包含，并且mergeCommit也包含 */
                if (currentCommit.HashMapBlobs.containsKey(LCAFileName) && mergeCommit.HashMapBlobs.containsKey(LCAFileName)) {

                    HashMap<String, Blobs> mergeBlobs = currentCommit.HashMapBlobs.get(LCAFileName);
                    HashMap<String, Blobs> currentBlobs = mergeCommit.HashMapBlobs.get(LCAFileName);
                    HashMap<String, Blobs> LCABlobs = LCACommit.HashMapBlobs.get(LCAFileName);

                    Blobs mergeBlob = mergeBlobs.values().iterator().next();
                    Blobs currentBlob = currentBlobs.values().iterator().next();
                    Blobs LCABBlob = LCABlobs.values().iterator().next();
                    if (!mergeBlob.ID.equals(LCABBlob.ID) && currentBlob.ID.equals(LCABBlob.ID)) {
                        File readyWritingFile = new File(Main.CWD, LCAFileName);
                        if (!readyWritingFile.exists()) {
                            readyWritingFile.createNewFile();
                        }
                        clearFile(readyWritingFile);
                        Utils.writeContents(readyWritingFile, mergeBlob.getContent());
                        Main.add(readyWritingFile.getName());
                    }
                    /*
                    两个分支都修改了该文件，且修改后的内容不一样；
                     */
                    if (!mergeBlob.ID.equals(LCABBlob.ID) && !currentBlob.ID.equals(LCABBlob.ID)) {
                        judgeConflict = false;
                        //System.out.println("FileName1 :" + LCAFileName);
                        File readyWritingFile = new File(Main.CWD, LCAFileName);
                        if (!readyWritingFile.exists()) {
                            readyWritingFile.createNewFile();
                        }

                        /*
                        String text;
                        String currentStr = new String(currentBlob.getContent(), "UTF-8");
                        String mergeStr = new String(mergeBlob.getContent(), "UTF-8");
                        text = "<<<<<<< HEAD\n" + currentStr + "=======\n" + mergeStr + ">>>>>>>\n";
                        Utils.writeContents(readyWritingFile, text);
                         */
                        clearFile(readyWritingFile);
                        byte[]ConflictMessage = buildConflictMessage(currentBlob.getContent(), mergeBlob.getContent());
                        Utils.writeContents(readyWritingFile, ConflictMessage);
                        Main.add(readyWritingFile.getName());
                    }
                }

                //   currentCommit修改了该文件，mergeCommit删除了该文件；
                if ((currentCommit.HashMapBlobs.containsKey(LCAFileName) && !mergeCommit.HashMapBlobs.containsKey(LCAFileName))) {

                    Stage currentStage;
                    if (Main.Stage.length() > 0){
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
                        currentStage = (Stage) ois.readObject();
                        ois.close();
                    }
                    else {
                        currentStage = new Stage();
                    }
                    currentStage.stages.remove(LCAFileName);
                    currentStage.deleteFiles.add(LCAFileName);
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Main.Stage));
                    out.writeObject(currentStage);
                    out.close();
                    File readyDeletingFile = new File(Main.CWD, LCAFileName);
                    if (readyDeletingFile.exists()) {
                        readyDeletingFile.delete();
                    }
                    //System.out.println("Stage has been deleted." + LCAFileName);

                    HashMap<String, Blobs> currentBlobs = currentCommit.HashMapBlobs.get(LCAFileName);
                    Blobs currentBlob = currentBlobs.values().iterator().next();

                    HashMap<String, Blobs> LCABlobs = LCACommit.HashMapBlobs.get(LCAFileName);
                    Blobs LCABBlob = LCABlobs.values().iterator().next();
                    // 做了修改
                    if (!currentBlob.ID.equals(LCABBlob.ID))   {
                        judgeConflict = false;
                        File readyWritingFile = new File(Main.CWD, LCAFileName);
                        if (!readyWritingFile.exists()) {
                            readyWritingFile.createNewFile();
                        }
                        /*
                        clearFile(readyWritingFile);
                        String text;
                        String currentStr = new String(currentBlob.getContent(), "UTF-8");
                        String mergeStr = "";
                        text = "<<<<<<< HEAD\n" + currentStr + "=======\n" + mergeStr + ">>>>>>>\n";
                        Utils.writeContents(readyWritingFile, text);
                        Main.add(readyWritingFile.getName());
                        */
                        clearFile(readyWritingFile);
                        byte[]ConflictMessage = buildConflictMessage(currentBlob.getContent(), new byte[0]);
                        Utils.writeContents(readyWritingFile, ConflictMessage);
                        Main.add(readyWritingFile.getName());
                    }

                }

                if ((!currentCommit.HashMapBlobs.containsKey(LCAFileName) && mergeCommit.HashMapBlobs.containsKey(LCAFileName))) {

                    Stage currentStage;
                    if (Main.Stage.length() > 0){
                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
                        currentStage = (Stage) ois.readObject();
                        ois.close();
                    }
                    else {
                        currentStage = new Stage();
                    }
                    currentStage.stages.remove(LCAFileName);
                    currentStage.deleteFiles.add(LCAFileName);
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Main.Stage));
                    out.writeObject(currentStage);
                    out.close();

                    HashMap<String, Blobs> mergeBlobs = mergeCommit.HashMapBlobs.get(LCAFileName);
                    Blobs mergeBlob = mergeBlobs.values().iterator().next();
                    HashMap<String, Blobs> LCABlobs = LCACommit.HashMapBlobs.get(LCAFileName);
                    Blobs LCABBlob = LCABlobs.values().iterator().next();

                    if (!mergeBlob.ID.equals(LCABBlob.ID)) {// 做了修改
                        judgeConflict = false;
                        File readyWritingFile = new File(Main.CWD, LCAFileName);
                        if (!readyWritingFile.exists()) {
                            readyWritingFile.createNewFile();
                        }
                        /*
                        clearFile(readyWritingFile);
                        String text;
                        String currentStr = "";
                        String mergeStr = new String(mergeBlob.getContent(), "UTF-8");;
                        text = "<<<<<<< HEAD\n" + currentStr + "=======\n" + mergeStr + ">>>>>>>\n";
                        Utils.writeContents(readyWritingFile, text);
                        Main.add(readyWritingFile.getName());
                        */
                        clearFile(readyWritingFile);
                        byte[]ConflictMessage = buildConflictMessage(new byte[0], mergeBlob.getContent());
                        Utils.writeContents(readyWritingFile, ConflictMessage);
                        Main.add(readyWritingFile.getName());
                    }
                }

                /*规则六*/
                if (currentCommit.HashMapBlobs.containsKey(LCAFileName) && !mergeCommit.HashMapBlobs.containsKey(LCAFileName)) {
                    File readyWritingFile = new File(Main.CWD, LCAFileName);

                    HashMap<String, Blobs> currentBlobs = currentCommit.HashMapBlobs.get(LCAFileName);
                    Blobs currentBlob = currentBlobs.values().iterator().next();
                    HashMap<String, Blobs> LCABlobs = LCACommit.HashMapBlobs.get(LCAFileName);
                    Blobs LCABBlob = LCABlobs.values().iterator().next();
                    if (currentBlob.ID.equals(LCABBlob.ID)) {
                        if (readyWritingFile.exists()) {
                            readyWritingFile.delete();
                        }
                    }
                }
            }

            /*规则五*/
            for (String mergeFileName : mergeCommit.HashMapBlobs.keySet()) {
                if (!LCACommit.HashMapBlobs.containsKey(mergeFileName) && !currentCommit.HashMapBlobs.containsKey(mergeFileName)) {
                    HashMap<String, Blobs> mergeBlobs = mergeCommit.HashMapBlobs.get(mergeFileName);
                    Blobs mergeBlob = mergeBlobs.values().iterator().next();
                    File readyWritingFile = new File(Main.CWD, mergeFileName);
                    if (!readyWritingFile.exists()) {
                        readyWritingFile.createNewFile();
                    }
                    clearFile(readyWritingFile);
                    Utils.writeContents(readyWritingFile, mergeBlob.getContent());
                    Main.add(readyWritingFile.getName());
                }
                //该文件在分裂点中不存在，但是在两个分支中各自新增了内容不同的同名文件。
                if (!LCACommit.HashMapBlobs.containsKey(mergeFileName) && currentCommit.HashMapBlobs.containsKey(mergeFileName)){

                    HashMap<String, Blobs> mergeBlobs = mergeCommit.HashMapBlobs.get(mergeFileName);
                    Blobs mergeBlob = mergeBlobs.values().iterator().next();
                    HashMap<String, Blobs> currentBlobs = currentCommit.HashMapBlobs.get(mergeFileName);
                    Blobs currentBlob = currentBlobs.values().iterator().next();

                    judgeConflict = false;
                    File readyWritingFile = new File(Main.CWD, mergeFileName);
                    if (!readyWritingFile.exists()) {
                        readyWritingFile.createNewFile();
                    }
                    /*
                    clearFile(readyWritingFile);
                    String text ;
                    String currentStr = new String(currentBlob.getContent(), "UTF-8");
                    String mergeStr = new String(mergeBlob.getContent(), "UTF-8");
                    text = "<<<<<<< HEAD\n" + currentStr + "=======\n" + mergeStr + ">>>>>>>\n";
                    Utils.writeContents(readyWritingFile, text);
                    Main.add(readyWritingFile.getName());
                     */
                    clearFile(readyWritingFile);
                    byte[]ConflictMessage = buildConflictMessage(currentBlob.getContent(), mergeBlob.getContent());
                    Utils.writeContents(readyWritingFile, ConflictMessage);
                    Main.add(readyWritingFile.getName());
                }
            }

            if (!judgeConflict) System.out.println("Encountered a merge conflict.");
            String [] args = new String[2];
            args[0] = "commit";
            args[1] = "Merged " +  mergeBranch + " into " + currentBranch + ".";

            /*
                 之前的判断的时候stage肯定为空 我只需要判断是不是lastcommit的
                 就可以判断是不是track的文件
             */

            Commit lastCommit = null;
            Commit KmergeCommit = null;
            Stage currentStage = null;

            if (Main.Stage != null && Main.Stage.length() > 0){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
                currentStage = (Stage) ois.readObject();
                ois.close();
            }

            File commitidFile = new File(Main.Objects, Utils.readContentsAsString(new File(Main.Branches, currentBranch)));
            if (commitidFile != null && commitidFile.length() > 0) {
                ObjectInputStream in11 = new ObjectInputStream(Files.newInputStream(commitidFile.toPath()));
                lastCommit = (Commit) in11.readObject();
                in11.close();
            }

            File mergeFile = new File(Main.Objects, Utils.readContentsAsString(new File(Main.Branches, mergeBranch)));
            if (mergeFile != null && mergeFile.length() > 0) {
                ObjectInputStream in11 = new ObjectInputStream(Files.newInputStream(mergeFile.toPath()));
                KmergeCommit = (Commit) in11.readObject();
                in11.close();
            }

            for (File file : Main.CWD.listFiles()) {
                if (lastCommit != null && lastCommit.HashMapBlobs != null && file.isFile()) {
                    if (!lastCommit.HashMapBlobs.containsKey(file.getName()) && ! KmergeCommit.HashMapBlobs.containsKey(file.getName())) {//如果最近的commit不存在 说明不是track的
                        if (currentStage != null) {
                            if (currentStage.stages.containsKey(file.getName()) || currentStage.deleteFiles.contains(file.getName())) {
                                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                                System.exit(0);
                            }
                        }
                    }
                }
            }

            if (judgeConflict){
                Commit thisCommit = Main.commit1(args, mergeCommit.ID);
            }
        }
    }

    /*  找到最近的公共祖先（LCA）。*/
    public String LCA(File a, File b) throws IOException, ClassNotFoundException {

        ObjectInputStream oisLCA = new ObjectInputStream(new FileInputStream(a));
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(b));
        Commit aCommit = (Commit) oisLCA.readObject();
        Commit bCommit = (Commit) ois2.readObject();
        oisLCA.close();
        ois2.close();
        int deptha = aCommit.depth;
        int depthb = bCommit.depth;
        /*
            从树的角度来看 depth a 更靠下
            所以deptha要往上移动
         */
        while (deptha > depthb) {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(new File(Main.Objects, aCommit.lastCommitID1)));
            aCommit = (Commit) oos.readObject();
            oos.close();
            deptha = aCommit.depth;
        }

        while (depthb > deptha) {
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(new File(Main.Objects, bCommit.lastCommitID1)));
            bCommit = (Commit) oos.readObject();
            oos.close();
            depthb = bCommit.depth;
        }
        while (!aCommit.ID.equals(bCommit.ID)) {
            ObjectInputStream oosA = new ObjectInputStream(new FileInputStream(new File(Main.Objects, aCommit.lastCommitID1)));
            aCommit = (Commit) oosA.readObject();
            oosA.close();

            ObjectInputStream oosB = new ObjectInputStream(new FileInputStream(new File(Main.Objects, bCommit.lastCommitID1)));
            bCommit = (Commit) oosB.readObject();
            oosB.close();
        }
        return aCommit.ID;
    }

    public void failureCases(String Branch) throws IOException, ClassNotFoundException {
        if (Main.Stage.length() > 0){
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
            Stage stage = (Stage) ois.readObject();
            ois.close();
            if (stage.stages.size() > 0 || stage.deleteFiles.size() > 0) {
                System.out.println("You have uncommitted changes.");
                System.exit(0);
            }
        }

        File branchName = new File(Main.Branches, Branch);
        if (!branchName.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        String currentBranch = Utils.readContentsAsString(Main.Head);
        if (branchName.getName().equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

    }

    public static void clearFile(File file) throws IOException {
        Files.write(file.toPath(), new byte[0]);
    }

    private byte[] buildConflictMessage(byte[] cContent, byte[] gContent) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write("<<<<<<< HEAD\n".getBytes(StandardCharsets.UTF_8));
            bos.write(cContent);
            bos.write("=======\n".getBytes(StandardCharsets.UTF_8));
            bos.write(gContent);
            bos.write(">>>>>>>\n".getBytes(StandardCharsets.UTF_8));
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}