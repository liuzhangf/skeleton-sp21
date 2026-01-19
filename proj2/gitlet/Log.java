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

                if (lastCommit.lastCommitID1 != "" && lastCommit.lastCommitID2 != "") {
                    String short1 = lastCommit.lastCommitID1.substring(0,7);
                    String short2 = lastCommit.lastCommitID2.substring(0,7);
                    System.out.println("Merge: " + short1 + " " + short2);
                }

                String date = formatTimestamp(lastCommit.timestamp);
                System.out.println("Date: " + date);
                System.out.println(lastCommit.message);
                if (lastCommit.lastCommitID2 == "" ) {
                    System.out.println(); // 只有merge commit，加1个空行
                }

                lastCommitHashCode = lastCommit.lastCommitID1;

                file = new File(Main.Objects, lastCommitHashCode);
                if (lastCommitHashCode == null || lastCommitHashCode.isEmpty()) break;
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
        for (File file : Main.Objects.listFiles()) {
            ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file));
            Object obj = fis.readObject();
            if (obj instanceof Commit) {
                Commit commit = (Commit) obj;
                System.out.println("===");
                System.out.println("commit " +  commit.ID);
                String date = formatTimestamp(commit.timestamp);
                System.out.println("Date: " + date);
                System.out.println(commit.message);
                System.out.println();
            }
        }
    }
}
