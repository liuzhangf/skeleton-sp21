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

        String lastCommitHashCode = Utils.readContentsAsString(new File(Main.Branches, Branch));
        if (lastCommitHashCode == null || lastCommitHashCode.length() == 0) {

        }
        else {
            File lastCommitFile = new File(Main.Objects,lastCommitHashCode);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) ois.readObject();
            ois.close();

            //Utils.writeContents(Main.Head, Branch);

            for (File cwdFile : Main.CWD.listFiles()){
                cwdFile.delete();
            }

            for (String file : lastCommit.HashMapBlobs.keySet()) {
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
