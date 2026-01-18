package gitlet;

import java.io.*;

/*
    简历一个depth标签，给每一个commit
    然后每次merge直到depth相等， 再一起往上跑。
 */
public class Merge {
    public String LCA(File a, File b) throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(a));
        ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(b));
        Commit aCommit = (Commit) ois.readObject();
        Commit bCommit = (Commit) ois2.readObject();

    }
}
