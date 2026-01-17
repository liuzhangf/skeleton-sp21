package gitlet;

import java.io.*;

    /*
        .gitlet/
        ├─ 📂 objects/       # 存放【所有Commit对象+所有Blob对象】的地方
        │  └─ xxxxxx...      # 只有1个文件：初始Commit的哈希文件（文件名=哈希值，内容=序列化的Commit）
        ├─ 📂 branches/      # 存放【所有分支】的地方 → ✅【重点】多分支的核心存储目录✅
        │  └─ master         # 只有1个文件：master分支文件（文件名=分支名，文件内容=该分支指向的Commit哈希）
        └─ 📄 HEAD           # 全局唯一的「当前分支标记文件」（文件内容=字符串，比如：master）
     */

public class Main {

    public static final File CWD= new File(System.getProperty("user.dir"));
    public static final File Gitlet = new File(CWD, ".gitlet");
    public static final File Objects = new File(Gitlet, "objects");
    public static final File Branches = new File(Gitlet, "branches");
    public static final File Head = new File(Gitlet, "HEAD");
    public static final File Stage = new File(Gitlet, "stage");
    public static final File Commit = new File(Gitlet, "commit");

    public static void main(String[] args) {
        try {
            String firstArg = args[0];

            switch (firstArg) {
                case "init":
                    init();
                    break;
                case "add":
                    add(args[1]);
                    break;
                case "commit":
                    commit(args);
                    break;
                case  "rm":
                    delete(args);
                    break;
                case "log":
                    dealWithLog();
                    break;
                case "find":
                    if (args.length < 2) {
                        System.out.println("Found no commit with that message.");
                    }
                    else {
                        new Find(args[1]);
                    }
                    break;
                case "checkout":
                    checkout(args);
                    break;
                case "global-log" :
                    dealWithGlobalLog();
                    break;
                case "branch" :
                    new Branch("create",args[1]);
                    break;
                case "rm-branch" :
                    new Branch("delete",args[1]);
                    break;
                case "reset" :
                    new Reset(args[1]);
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkout(String[] args) throws IOException, ClassNotFoundException {
        if (args.length == 3) {
            if (args[1].equals("--")) {
                new Checkout(args[2], "");
            }
            else {
                System.out.println("Incorrect operands.");
            }
        }
        else if (args.length == 4) {
            if (args[2].equals("--")) {
                new Checkout(args[3], args[1]);
            }
            else {
                System.out.println("Incorrect operands.");
            }
        }
        else if (args.length == 2) {
            new Checkout("", args[1]);
        }
        else {
            System.out.println("Incorrect operands.");
        }
    }

    public static void dealWithGlobalLog() throws IOException, ClassNotFoundException {
        new Log("");
    }

    public static void dealWithLog() throws IOException, ClassNotFoundException {
        String inp = Utils.readContentsAsString(Main.Head);
        //System.out.println(inp);
        String lastCommitPointer = Utils.readContentsAsString(new File(Main.Branches, inp));
        //System.out.println(lastCommitPointer);
        new Log(lastCommitPointer);
    }

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
            Utils.writeContents(Main.Head, "master");
            Branch newBranch = new Branch("create","master");
            /*写入commit*/
            Commit cm = new Commit(System.currentTimeMillis(), "initial commit",
                    new String[0] , new String[0], null);
            File BranchesFile = new File(Main.Branches, newBranch.getBranchName() );
            Utils.writeContents(BranchesFile, cm.ID);
            File Object_commit = new File(Objects, cm.ID);
            Object_commit.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Object_commit));
            out.writeObject(cm);
            out.close();
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
                    //System.out.println("Adding " + fileArray.length + " changes to the commit.");
                    for (int i = 0; i < blobsObjectArray.length; i++) {
                        blobsArray[i] = blobsObjectArray[i].getID();
                    }
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Stage));
                    Stage stage2 = (Stage) ois.readObject();
                    ois.close();
                    Commit newCommit = new Commit(System.currentTimeMillis(), args[1], blobsArray, fileArray, stage2);
                    //System.out.println(newCommit.HashMapBlobs.size());
                }
            }
        }
    }

    public static void delete(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 2) {
            System.out.println("No reason to remove the file.");
        }
        else {
            new Remove(args[1]);
        }
    }
}
