package gitlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Find implements Serializable {

    public Find(String message) throws IOException, ClassNotFoundException {

        for (File branches : Main.Branches.listFiles()) {

            String lastCommitPointer = Utils.readContentsAsString(branches);
            String inp = lastCommitPointer;
            File file = new File(Main.Objects, inp);
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file));
            Commit lastCommit = (Commit) fis.readObject();
            String lastCommitHashCode = lastCommit.ID;

            while(true) {
                if (lastCommit.message.equals(message)) {
                    System.out.println(lastCommitHashCode);
                }
                /* 这个地方是找到*/
                if (lastCommitHashCode != null && lastCommitHashCode != "") {
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
    }
}
