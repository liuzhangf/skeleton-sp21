package gitlet;

import java.io.*;


public class Remove {

    public Remove(String file) throws IOException, ClassNotFoundException {
        delete(file);
    }

    public void delete(String file) throws IOException, ClassNotFoundException {
        /*
            读取现在的stage，并删除文件。
        */
        if (gitlet.Main.Stage.length() > 0) {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(gitlet.Main.Stage));
            Stage currentStage = (Stage) ois.readObject();
            ois.close();
            if (currentStage != null) {
                if (currentStage.stages.containsKey(file)) {
                    currentStage.stages.remove(file);
                }
            }

        }

        /*
           如果这个文件，没有在暂存区里，但是这个文件是「仓库里的正式文件」
           （也就是在你最近一次 commit 的快照里、被 HEAD 指向的最新提交记录着）
         */
        else {
            String lastCommitPaht = Utils.readContentsAsString(gitlet.Main.Head);
            File lastCommitFile = new File(Main.Objects, lastCommitPaht);

            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit LastCommit = (Commit) oos.readObject();
            oos.close();
            if (LastCommit != null) {
                if (LastCommit.HashMapBlobs.containsKey(file)) {
                    Stage newstage = new Stage();
                    newstage.deleteFiles.add(file);

                    ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(gitlet.Main.Stage));
                    oos2.writeObject(LastCommit);
                    oos2.close();
                }
                else {
                    System.out.println("No reason to remove the file.\n");
                }
            }
            System.out.println("No reason to remove the file.\n");
        }
    }

}
