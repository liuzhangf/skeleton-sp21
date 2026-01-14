package gitlet;

import java.io.*;

public class Log implements Serializable {

    public Log() throws IOException, ClassNotFoundException {
        String inp = Utils.readContentsAsString(Main.Head);
        File file = new File(Main.Objects, inp);
        ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file));
        Commit lastCommit = (Commit) fis.readObject();
        String lastCommitHashCode = lastCommit.ID;
        while(true) {
            System.out.println("===\n");
            System.out.println("commit " + lastCommitHashCode);
            System.out.println("Date " + lastCommit.timestamp);
            System.out.println(lastCommit.message);
            System.out.println("\n");

            lastCommitHashCode = lastCommit.lastCommitID1;

            file = new File(Main.Objects, lastCommitHashCode);
            if (file.length() == 0) break;

            else {
                fis = new ObjectInputStream(new FileInputStream(file));
                lastCommit = (Commit) fis.readObject();
            }
        }
    }

}
