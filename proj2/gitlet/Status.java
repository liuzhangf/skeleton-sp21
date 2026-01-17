package gitlet;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Status implements java.io.Serializable {
    public  Status () throws IOException, ClassNotFoundException {
        printTheStatus();
    }

    private void printTheStatus() throws IOException, ClassNotFoundException {

        /*打印全部的branch*/
        System.out.println("=== Branches ===");
        System.out.println("*" + Utils.readContentsAsString(Main.Head));
        for (File branchFile : Main.Branches.listFiles()) {
            if (branchFile.getName().equals(Utils.readContentsAsString(Main.Head))) {
                continue;
            }
            else {
                if (branchFile.isFile()) {
                    System.out.println(branchFile.getName());
                }
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        if (Main.Stage != null && Main.Stage.length() > 0) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
            Stage currentStage = (Stage) ois.readObject();
            if (currentStage != null) {
                for (String fileName : currentStage.stages.keySet()) {
                    System.out.println(fileName);
                }
            }
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        if (Main.Stage != null && Main.Stage.length() > 0) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
            Stage currentStage = (Stage) ois.readObject();
            if (currentStage != null) {
                for (String fileName : currentStage.deleteFiles) {
                    System.out.println(fileName);
                }
            }
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        if (Main.Stage != null) {

            Stage currentStage;
            if (Main.Stage != null && Main.Stage.length() > 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
                currentStage = (Stage) ois.readObject();
            }
            else {
                currentStage = null;
            }

            File lastBranches = new File( Main.Branches, Utils.readContentsAsString(Main.Head));
            File lastCommitFile = new File(Main.Objects, Utils.readContentsAsString(lastBranches));
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) oos.readObject();

            for (File currentFile : Main.CWD.listFiles()) {
                if (lastCommit != null && lastCommit.HashMapBlobs.size() > 0 &&  lastCommit.HashMapBlobs.containsKey(currentFile.getName())) {

                    HashMap<String, Blobs> mapBlobs = lastCommit.HashMapBlobs.get(currentFile.getName());
                    HashMap<String, Blobs> mapBlobs1 = mapBlobs;
                    Blobs currentBlob = (Blobs) mapBlobs1.values();

                    if (!currentBlob.getContent().equals(Files.readAllBytes(currentFile.toPath()))) {
                        if (currentStage != null && !currentStage.stages.containsKey(currentFile.getName())) {
                        /*
                            Tracked in the current commit, changed in the working directory,
                            but not staged;  这里并不包含删除的逻辑
                         */
                            System.out.println(currentFile.getName() + " (modified)");
                        }
                        else {
                            if (currentStage == null) {
                                System.out.println(currentFile.getName() + " (modified)");
                            }
                        }
                    }
                }
            }

            /*
                Not staged for removal, but tracked in the current
                commit and deleted from the working directory.
             */
            for (String fileName : lastCommit.HashMapBlobs.keySet()) {
                if ( lastCommit != null && lastCommit.HashMapBlobs != null ) {
                    if (! new File(Main.CWD, fileName).exists()) {
                        if (!currentStage.deleteFiles.contains(fileName)) {
                            System.out.println(fileName + " (deleted)");
                        }
                    }
                }
            }
            /*
                Staged for addition, but with different contents than in the working directory;
                Staged for addition, but deleted in the working directory
             */
            if (currentStage != null) {
                for (String fileName : currentStage.stages.keySet()) {
                    if (new File(Main.CWD, fileName).isFile()){
                        if (!new File(Main.CWD, fileName).exists()) {
                            /*压根不存在，就是删除*/
                            if (new File(Main.CWD, fileName).isFile()) {
                                System.out.println(fileName + " (deleted)");
                            }
                        }

                        else {
                            /*存在但是变了，就是修改*/
                            if (currentStage != null) {
                                File currentFile = new File(Main.CWD, fileName);
                                Blobs currentBlob = (Blobs) currentStage.stages.get(fileName);
                                if (Files.readAllBytes(currentFile.toPath()) != currentBlob.getContent()) {
                                    System.out.println(fileName + " (modified)");
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        if (Main.Stage != null) {

            Stage currentStage;
            if (Main.Stage != null && Main.Stage.length() > 0) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
                currentStage = (Stage) ois.readObject();
            }
            else {
                currentStage = null;
            }
            File lastBranches = new File( Main.Branches, Utils.readContentsAsString(Main.Head));
            File lastCommitFile = new File(Main.Objects, Utils.readContentsAsString(lastBranches));

            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(lastCommitFile));
            Commit lastCommit = (Commit) oos.readObject();

            for (File currentFile : Main.CWD.listFiles()) {
                if (currentFile.isFile()) {
                    if (lastCommit != null && lastCommit.HashMapBlobs.size() > 0 && ! lastCommit.HashMapBlobs.containsKey(currentFile.getName())) {
                        if (currentStage != null) {
                            if (!currentStage.stages.containsKey(currentFile.getName())) {
                                System.out.println(currentFile.getName() + " (modified)");
                            }
                        }
                        else if (currentStage == null) {
                            System.out.println(currentFile.getName() + " (modified)");
                        }
                    }
                    else if (lastCommit != null && lastCommit.HashMapBlobs.size() == 0) {
                        if (currentStage != null) {
                            if (!currentStage.stages.containsKey(currentFile.getName())) {
                                System.out.println(currentFile.getName() + " (modified)");
                            }
                        }
                        else if (currentStage == null) {
                            System.out.println(currentFile.getName() + " (modified)");
                        }
                    }
                }
            }
        }
        System.out.println();
    }
}
