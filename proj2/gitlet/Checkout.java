package gitlet;

import java.io.*;
import java.util.HashMap;

import static gitlet.Stage.clearFile;

public class Checkout {

    public Checkout(String Filename, String CommitID) throws IOException, ClassNotFoundException {
        /* 两个参数可以表示四种状态 */
        if (CommitID.equals("")) {
            checkoutOverride(Filename);
        }
        else if (Filename.equals("")) {
            checkoutOverride1(CommitID);
        }
        else {
            checkoutOverride(Filename, CommitID);
        }
    }

    private void checkoutOverride1(String Branch) throws IOException, ClassNotFoundException {

        File checkoutDir = new File(Main.Branches, Branch);
        if (!checkoutDir.exists()) {
            System.out.println("No such branch exists.");
        }
        else if (Utils.readContentsAsString(Main.Head).equals(Branch)) {
            System.out.println("No need to checkout the current branch.");
        }
        else  {
            /*
                最开始的一段是用来判断是否具有未被最近的一次commit追踪， 同时会被覆盖的文件
             */
            String newBranchh = Utils.readContentsAsString((Main.Head));
            String lastCommitHashCode = Utils.readContentsAsString(new File(Main.Branches, newBranchh));
            File checkoutCommit = new File(Main.Objects, lastCommitHashCode);
            ObjectInputStream oiss = new ObjectInputStream(new FileInputStream(checkoutCommit));
            Commit lastCommit1 = (Commit) oiss.readObject();
            oiss.close();

            boolean flag = true;

            /*没有被当前的commit跟踪*/
            String targetCheckoutHash = Utils.readContentsAsString(new File(Main.Branches, Branch));
            File targetCheckoutCommit = new File(Main.Objects, targetCheckoutHash);
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(targetCheckoutCommit));
            Commit targetCommit1 = (Commit) oos.readObject();
            oos.close();
            Stage stage = null;
            if (Main.Stage.length() > 0) {
                ObjectInputStream inppp = new ObjectInputStream(new FileInputStream(Main.Stage));
                stage = (Stage) inppp.readObject();
            }

            for (File singleFile : Main.CWD.listFiles()) {
                if (singleFile.isFile()) {
                    if (!lastCommit1.HashMapBlobs.containsKey(singleFile.getName()) && targetCommit1.HashMapBlobs.containsKey(singleFile.getName())) {
                        flag = false;
                    }
                    if (!flag) {
                        if (stage != null) {
                            if (!stage.stages.containsKey(singleFile.getName())) {
                                flag = true;
                            }
                        }
                    }
                }
            }

            if (!flag) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }

            File lastCommitFile = new File(Main.Objects,lastCommitHashCode);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) ois.readObject();
            ois.close();
            for (File cwdFile : Main.CWD.listFiles()){
                if (cwdFile.isFile()) cwdFile.delete();
            }
            Utils.writeContents(Main.Head, Branch);
            for (String file : targetCommit1.HashMapBlobs.keySet()) {
                HashMap<String, Blobs> HashBlobs = lastCommit.HashMapBlobs.get(file);
                File writeFile = new File(Main.CWD,file);
                clearFile(writeFile);
                for (Blobs blobs : HashBlobs.values()) {
                    Utils.writeContents(writeFile,blobs.getContent());
                }
            }

        }
    }

    private void checkoutOverride(String fileName) throws IOException, ClassNotFoundException {

        String LastBranches = Utils.readContentsAsString(Main.Head);
        String lastCommitHashCode = Utils.readContentsAsString(new File(Main.Branches, LastBranches));
        if (lastCommitHashCode.length() == 0) {
            System.out.println("File does not exist in that commit.");
        }
        else {
            /*读取出来lastCommit*/
            boolean flag  = false;
            File lastCommitFile = new File(Main.Objects,lastCommitHashCode);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) ois.readObject();
            ois.close();
            for (String file : lastCommit.HashMapBlobs.keySet()) {
                if (fileName.equals(file)) {
                    HashMap<String, Blobs> HashBlobs = lastCommit.HashMapBlobs.get(file);
                    File writeFile = new File(Main.CWD,file);
                    clearFile(writeFile);
                    for (Blobs blobs : HashBlobs.values()) {
                        Utils.writeContents(writeFile,blobs.getContent());
                    }
                    flag = true;
                }
            }
            if (!flag) {
                System.out.println("File does not exist in that commit.");
            }
        }
    }

    private void checkoutOverride(String fileName, String commitHash) throws IOException, ClassNotFoundException {
        boolean flag  = false;
        File lastCommitFile = new File(Main.Objects,commitHash);

        if (!lastCommitFile.exists()) {
            System.out.println("No commit with that id exists.");
        }
        else {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) ois.readObject();
            ois.close();
            for (String file : lastCommit.HashMapBlobs.keySet()) {
                if (fileName.equals(file)) {
                    HashMap<String, Blobs> HashBlobs = lastCommit.HashMapBlobs.get(file);
                    File writeFile = new File(Main.CWD,file);
                    clearFile(writeFile);
                    for (Blobs blobs : HashBlobs.values()) {
                        Utils.writeContents(writeFile,blobs.getContent());
                    }
                    flag = true;
                }
            }

            if (!flag) {
                System.out.println("File does not exist in that commit.");
            }
        }
    }
}
