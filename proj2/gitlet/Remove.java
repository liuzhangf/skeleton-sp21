package gitlet;

import java.io.*;
public class Remove {

    public Remove(String file) throws IOException, ClassNotFoundException {
        delete(file);
    }
    public void delete(String file) throws IOException, ClassNotFoundException {
        /*读取现在的stage，并删除文件。*/
        if (gitlet.Main.Stage.length() > 0) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(gitlet.Main.Stage));
            Stage currentStage = (Stage) ois.readObject();
            ois.close();
            if (currentStage != null) {
                if (currentStage.stages.containsKey(file)) {
                    currentStage.stages.remove(file);
                    ObjectOutputStream oos3 = new ObjectOutputStream(new FileOutputStream(gitlet.Main.Stage));
                    oos3.writeObject(currentStage);
                    oos3.close();
                }
                else {
                    followUp(file);
                }
            }
        }
        /* 如果这个文件，没有在暂存区里，但是这个文件是「仓库里的正式文件」
           （也就是在你最近一次 commit 的快照里、被 HEAD 指向的branch,branch指向最新一次的提交记录，
            最新提交记录着）*/
        else {
            followUp(file);
        }
    }

    /*当前stage存在要求的file，继续处理。否则follow up。*/
    public void followUp (String file) throws IOException, ClassNotFoundException {

        String lastCommitPaht = Utils.readContentsAsString(gitlet.Main.Head);
        String lastCommitPointer = Utils.readContentsAsString(new File(Main.Branches, lastCommitPaht));
        lastCommitPaht = lastCommitPointer;
        File lastCommitFile = new File(Main.Objects, lastCommitPaht);
        ObjectInputStream oos = new ObjectInputStream(new FileInputStream(lastCommitFile));
        Commit LastCommit = (Commit) oos.readObject();
        oos.close();

        if (LastCommit != null) {
            if (LastCommit.HashMapBlobs.containsKey(file)) {
                /*如果被上一个从commit追踪*/
                Stage newstage;
                if (Main.Stage.length() > 0){
                    ObjectInputStream inp = new ObjectInputStream(new FileInputStream(Main.Stage));
                    newstage = (Stage) inp.readObject();
                    newstage.deleteFiles.add(file);
                }else {
                    newstage = new Stage();
                }

                if (newstage.stages.containsKey(file)) {
                    newstage.stages.remove(file);
                }
                else {
                    newstage.deleteFiles.add(file);
                }

                ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(gitlet.Main.Stage));
                oos2.writeObject(newstage);
                oos2.close();

                /*
                    当且仅当被上一个追踪才删掉工作目录中的文件
                 */
                File lastCommitBranch = new File(Main.Branches, Utils.readContentsAsString(Main.Head));
                File thisCommitFile = new File(Main.Objects, Utils.readContentsAsString(lastCommitBranch));
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(thisCommitFile));
                Commit lastCommitFile1 = (Commit) ois.readObject();
                if (lastCommitFile1.HashMapBlobs != null && lastCommitFile1.HashMapBlobs.size() > 0) {
                    if (lastCommitFile1.HashMapBlobs.containsKey(file)) {
                        File currentFile = new File(Main.CWD, file);
                        if(currentFile.exists()) {
                            currentFile.delete();
                        }
                    }
                }
            }
            else {
                System.out.println("No reason to remove the file.");
            }
        }
        else {
            System.out.println("No reason to remove the file.");
        }
    }
}
