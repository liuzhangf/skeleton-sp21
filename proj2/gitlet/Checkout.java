package gitlet;

import java.io.*;
import java.util.HashMap;

import static gitlet.Stage.clearFile;

public class Checkout {

    public Checkout(String Filename, String CommitID) throws IOException, ClassNotFoundException {
        if (CommitID.equals("")) {
        //    System.out.println("Commit ID is empty");
            checkoutOverride(Filename);
        }
        else {
        //    System.out.println("cnm");
            checkoutOverride(Filename, CommitID);
        }
    }

    private void checkoutOverride(String fileName) throws IOException, ClassNotFoundException {
        String lastCommitHashCode = Utils.readContentsAsString(Main.Head);
        if (lastCommitHashCode.length() == 0) {
            System.out.println("File does not exist in that commit.");
        }
        else {
            /*
                读取出来lastCommit
             */
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
                }
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
