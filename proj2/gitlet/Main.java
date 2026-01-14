package gitlet;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import gitlet.Commit;

public class Main {

    public static final File CWD= new File(System.getProperty("user.dir"));
    public static final File Gitlet = new File(CWD, ".gitlet");
    public static final File Objects = new File(Gitlet, "objects");
    public static final File Branches = new File(Gitlet, "branches");
    public static final File Head = new File(Gitlet, "HEAD");
    public static final File Stage = new File(Gitlet, "stage");
    public static final File Commit = new File(Gitlet, "commit");

    //LinkedList<Commit> commitsList;

    public static void main(String[] args) {
        // TODO: what if args is empty?
        try {
            String firstArg = args[0];

            switch (firstArg) {
                case "init":
                    init();
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    add(args[1]);
                    break;
                // TODO: FILL THE REST IN
                case "commit":
                    commit(args);
                    break;
                case  "rm":
                    delete(args);
                    break;
                case "log":
                    new Log();
                    break;
                case "find":
                    if (args.length < 2) {
                        System.out.println("Found no commit with that message.");
                    }
                    else {
                        new Find("args[1]");
                    }
                    break;
            }
        }
        catch (Exception e) {
        //    e.printStackTrace();
        }
    }

    /*
        .gitlet/
        ├─ 📂 objects/       # 存放【所有Commit对象+所有Blob对象】的地方
        │  └─ xxxxxx...      # 只有1个文件：初始Commit的哈希文件（文件名=哈希值，内容=序列化的Commit）
        ├─ 📂 branches/      # 存放【所有分支】的地方 → ✅【重点】多分支的核心存储目录✅
        │  └─ master         # 只有1个文件：master分支文件（文件名=分支名，文件内容=该分支指向的Commit哈希）
        └─ 📄 HEAD           # 全局唯一的「当前分支标记文件」（文件内容=字符串，比如：master）
     */

    public static void init () throws IOException, ClassNotFoundException {

        if (Gitlet.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        else {

            Gitlet.mkdir();
            Objects.mkdir();
            Branches.mkdir();
            Head.createNewFile();
            Stage.createNewFile();

            Commit cm = new Commit(System.currentTimeMillis(), "initial commit", new String[0] , new String[0], null);

            File Object_commit = new File(Objects, cm.ID);
            Object_commit.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Object_commit));
            out.writeObject(cm);
            out.close();
            File branches = new File(Branches, "master");
            branches.createNewFile();
            Utils.writeContents( branches , cm.ID);
            cm.lastCommitID1 = "";
        }

    }

    public static void add (String arg) throws IOException, ClassNotFoundException {
        Stage stage;
        if(!Stage.exists() || Stage.length() == 0){
            stage = new Stage();
        }
        else {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Stage))) {
                stage = (Stage) ois.readObject();
            }
        }
        stage.addStage(arg);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Stage))) {
            oos.writeObject(stage);
        }
    }

    public static void commit (String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            System.out.println("Please enter a commit message.");
        }
        else {

            if (Stage.length() == 0) {
                System.out.println("No changes added to the commit.");
            }
            else {

                ObjectInputStream inp = new ObjectInputStream(new FileInputStream(Stage));
                Stage stage = (Stage) inp.readObject();
                inp.close();

                if (stage.stages == null) {
                    System.out.println("No changes added to the commit.");
                }

                else {

                    String[] fileArray = stage.stages.keySet().toArray(new String[0]);
                    Blobs[] blobsObjectArray = stage.stages.values().toArray(new Blobs[0]);
                    String[] blobsArray = new String[blobsObjectArray.length];
                    for (int i = 0; i < blobsObjectArray.length; i++) {
                        blobsArray[i] = blobsObjectArray[i].getID();
                    }
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Stage));
                    Stage stage2 = (Stage) ois.readObject();
                    ois.close();
                    new Commit(System.currentTimeMillis(), args[1], blobsArray, fileArray, stage2);

                }
            }
        }
    }

    public static void delete(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
        //    System.out.println("Please enter a commit message.");
            System.out.println("No reason to remove the file.");
        //    System.out.println("hahaa");
        }
        else {
            new Remove(args[1]);
        }
    }
}
