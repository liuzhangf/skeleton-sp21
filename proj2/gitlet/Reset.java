package gitlet;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

import static gitlet.Stage.clearFile;

public class Reset {
    public Reset(String Commitid) throws IOException, ClassNotFoundException {
        Reset(Commitid);
        deleteThisCommit(Commitid);
        followUp();
    }

    /*
        尝试恢复当前的全部的
     */
    private void Reset(String Commitid) throws IOException, ClassNotFoundException {
        File commitidFile = new File(Main.Objects, Commitid);
        if (!commitidFile.exists()) {
            System.out.println("No commit with that id exists.");
        }
        else {
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(commitidFile));
            Commit thisCommit = (Commit) inp.readObject();
            inp.close();
            for (String file : thisCommit.HashMapBlobs.keySet()) {
                HashMap<String, Blobs> HashBlobs = thisCommit.HashMapBlobs.get(file);
                File writeFile = new File(Main.CWD,file);
                if (!writeFile.exists()) {
                    writeFile.createNewFile();
                }
                clearFile(writeFile);
                for (Blobs blobs : HashBlobs.values()) {
                    Utils.writeContents(writeFile,blobs.getContent());
                }
            }
            Utils.writeContents(Main.Head, thisCommit.witchBranch);
        }
    }

    private void deleteThisCommit(String Commitid) throws IOException, ClassNotFoundException {
        /*
            读入目标的Commit
         */
        File commitidFile = new File(Main.Objects, Commitid);
        ObjectInputStream inp = new ObjectInputStream(new FileInputStream(commitidFile));
        Commit thisCommit = (Commit) inp.readObject();
        /*
            读入当前的commit, 如果目标commit不存在，
            但是当前的commit存在，那么就删除。
        */
        String currentBranches = Utils.readContentsAsString(Main.Head);
        File branchesFile = new File(Main.Branches, currentBranches);
        String currentCommitId = Utils.readContentsAsString(branchesFile);
        inp = new ObjectInputStream(new FileInputStream(new File(Main.Objects, currentCommitId)));
        Commit currentCommit = (Commit) inp.readObject();
        inp.close();

        for (String file : currentCommit.HashMapBlobs.keySet()) {
            if (!thisCommit.HashMapBlobs.containsKey(file)) {
                currentCommit.HashMapBlobs.remove(file);
            }
        }
    }

    private void followUp() throws IOException {
        clearFile(Main.Stage);
    }
    public static void clearFile(File file) throws IOException {
        new FileOutputStream(file).close();
    }
}
