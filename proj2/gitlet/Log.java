package gitlet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Serializable {

    public Log(String lastCommitPointer) throws IOException, ClassNotFoundException {

        if (lastCommitPointer != "") {

            String inp = lastCommitPointer;
            File file = new File(Main.Objects, inp);
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file));
            Commit lastCommit = (Commit) fis.readObject();
            String lastCommitHashCode = lastCommit.ID;

            while(true) {

                System.out.println("===");
                System.out.println("commit " + lastCommitHashCode);

                String date = formatTimestamp(lastCommit.timestamp);
                System.out.println("Date: " + date);
                System.out.println(lastCommit.message);
                System.out.println();

                lastCommitHashCode = lastCommit.lastCommitID1;

                file = new File(Main.Objects, lastCommitHashCode);

                if (file.length() == 0) break;
                else {
                    fis = new ObjectInputStream(new FileInputStream(file));
                    lastCommit = (Commit) fis.readObject();
                }
            }
        }
        else {global_Log();}
    }

    private static String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");
        return sdf.format(date);
    }

    public void global_Log() throws IOException, ClassNotFoundException {
        for (File branches : Main.Branches.listFiles()) {
            String lastCommitPointer = Utils.readContentsAsString(branches);
            new Log(lastCommitPointer);
        }
    }
}
