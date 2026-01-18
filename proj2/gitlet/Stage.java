package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import static gitlet.Main.Objects;
import static gitlet.Main.Stage;

public class Stage implements Serializable {

    HashMap <String, Blobs > stages; // <FILEPATH, HASHCODE>
    LinkedList <String> deleteFiles = new LinkedList <> ();

    public Stage() throws IOException, ClassNotFoundException {
        if(Stage.length() > 0) {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(Stage));
            this.stages = (HashMap<String, Blobs>) input.readObject();
            this.deleteFiles = (LinkedList<String>) input.readObject();
            input.close();
        }
        else {
            stages = new HashMap<>();
        }
    }
    /*
        HashMap 的put(Key, Value) 执行时，底层会自动做这 3 件事
        自动判断 Key 是否存在：在 HashMap 中查找你传入的文件名(Key)，看 HashMap 里有没有这个 Key；
        如果 Key 存在 → 自动覆盖 Value：用你传入的「新 Blob 哈希」，直接替换掉 HashMap 里「旧 Blob 哈希」，旧的键值对被隐形删除 + 覆盖，一步完成；
        如果 Key 不存在 → 自动新增键值对：把你的「文件名 + 新 Blob 哈希」直接新增到 HashMap 里，没有任何多余操作。
     */

    public void addStage(String filename) throws IOException, ClassNotFoundException {

        File existing = new File(filename);
        if (existing.exists()) {
            Blobs newBlob = new Blobs(filename);
            File commitFile = new File(Objects, newBlob.ID);

            /*
                If the current working version of the file is identical to the version in the current commit,
                do not stage it to be added, and remove it from the staging area if it is already there
                (as can happen when a file is changed, added, and then changed back to it’s original version).
            */

            /*
                这里的话 是没解决branch的问题。
                可能对于不同的branch  确对应完全相同的文件。
             */

            if (this.deleteFiles.contains(filename)) {
                this.deleteFiles.remove(filename);
            }

            if (commitFile.exists()) { //commitFile 直接是用内容比较的
                if (stages.containsKey( filename )) {
                    stages.remove(filename);
                }
            }

            /*  现在的问题是commitfile也存在delefiles也有*/
            else {
                stages.put(filename, newBlob);
            }

            /*写入暂存区 */
            clearFile(Stage);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Stage));
            out.writeObject(stages);
            out.close();
            /*写入存上一次贡献的没问题的地方这里的没问题主要是指删掉了会被覆盖的原来的Blobs*/
        }
        else {
            System.out.println("File does not exist.");
        }
    }

    public static void clearFile(File file) throws IOException {
        new FileOutputStream(file).close();
    }
}
