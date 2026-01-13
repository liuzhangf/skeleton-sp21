package gitlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    static final File CWD= new File(System.getProperty("user.dir"));
    static final File Gitlet = new File(CWD, ".gitlet");
    static final File Objects = new File(CWD, ".objects");
    static final File Branches = new File(CWD, ".branches");
    static final File Head = new File(CWD, "HEAD");
    static final File Stage = new File(CWD, "stage");

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        // TODO: what if args is empty?
        String firstArg = args[0];

        if (!Stage.exists()){
            Stage.mkdir();
        }

        switch(firstArg) {
            case "init":
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                add(args[1]);
                break;
            // TODO: FILL THE REST IN
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

    public static void init () throws IOException {

        if (Gitlet.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }
        else {

            Gitlet.mkdir();
            Objects.mkdir();
            Branches.mkdir();
            Head.createNewFile();

            Commit cm = new Commit(0, "initial commit", null, null, null, null);
            File Object_commit = new File(Objects, cm.ID);
            Object_commit.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Object_commit));
            out.writeObject(cm);
            String head =  "master";            // 标记当前是什么工作的branch。
            Utils.writeContents( Head , head);

            File branches = new File(Branches, "master");
            branches.createNewFile();
            Utils.writeContents( Branches , cm.ID);
        }
    }

    public static void add (String filename) throws IOException {

    }

}
