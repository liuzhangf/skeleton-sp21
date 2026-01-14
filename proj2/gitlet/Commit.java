package gitlet;
import java.io.*;
import java.util.*;
import java.io.Serializable;

import static gitlet.Main.*;
import static gitlet.Main.Head;
import static gitlet.Stage.clearFile;

// TODO: any imports you need here


public class Commit implements Serializable {

    public final String ID;
    public String parentID1;
    public String parentID2;
    private final long timestamp;
    private  Map<String, HashMap<String, Blobs>> HashMapBlobs;
    private LinkedList<Blobs> blobsList;
    private String message;
    String lastCommitID1;
    String lastCommitID2;
    /*
        传进来的参数的含义如下 ：
        timestamp :时间戳
        message ： 要输出的信息
        parentID1 / parentID2 ：可能会存在两个或者更多的
        BlobID : 所有Bolbs的hashcode
        text : 文件名
     */

    public Commit(long timestamp, String message, String[] BolbID, String[] text, gitlet.Stage stage) throws IOException, ClassNotFoundException {

        this.timestamp = timestamp;
        this.message = message;
        HashMapBlobs = new HashMap<>();

        String s1 = "";
        String s2 = "";
        for (int i = 0; i < text.length; i++) {
            s2 += text[i];
        }
        for (int i = 0; i < BolbID.length; i++) {
            s1 += BolbID[i];
        }

        this.ID = Utils.sha1(this.message, s1, s2, lastCommitID1, lastCommitID2);

        for (int i = 0; i < stage.stages.size(); i++) {
            Blobs blob = stage.stages.get(i);
            blobsList.add(blob);
        }

        /*
            Head存放的是上一次commit的commit的hashcode
            读出来上一次commit文件里的东西
            然后再添加本次Commit的add stage的文件
         */
        readOldBlobs();

        if (BolbID != null && text != null) {
            matchBolbWithCommit(BolbID, text);
        }
        /*
            生成新的Commit文件存入Object文件夹
         */

        BuildNewCommitObject();

        /*
         清空当前的stage, 这个地方直接清空文件就行
         */
        clearFile(Stage);
    }

    /*
        这段代码主要是用于存下来本次commit的全部Blobs

     */
    private void matchBolbWithCommit (String[] BolbID, String[] text) throws IOException {
        for (int i = 0; i < BolbID.length; i++) {
            Blobs newblobs = new Blobs(text[i]);
            HashMap<String, Blobs> newHashMapBlobs = new HashMap<>();
            newHashMapBlobs.put(BolbID[i], newblobs);
            HashMapBlobs.put( text[i], newHashMapBlobs );
        }
    }

    private void readOldBlobs() throws IOException, ClassNotFoundException {
        ObjectInputStream fis = new ObjectInputStream(new FileInputStream(Head));
        lastCommitID1 = String.valueOf(fis.readObject());
        File parentCommitFile = new File(gitlet.Main.Objects,lastCommitID1);
        fis = new ObjectInputStream(new FileInputStream(parentCommitFile));

        Commit lastCommit = (Commit) fis.readObject();

        this.HashMapBlobs = lastCommit.HashMapBlobs;
    }

    private void BuildNewCommitObject() throws IOException, ClassNotFoundException {
        File NewCommitFile = new File(gitlet.Main.Objects, this.ID);
        if (!NewCommitFile.exists()) {
            NewCommitFile.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NewCommitFile));
            oos.writeObject(this);
            oos.close();
        }
    }

}
