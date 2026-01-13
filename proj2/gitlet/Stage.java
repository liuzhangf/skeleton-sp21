package gitlet;

import java.io.*;
import java.util.HashMap;
import static gitlet.Main.Objects;
import static gitlet.Main.Stage;


import static gitlet.Main.Objects;

public class Stage implements Serializable {

    HashMap <String, String> stages; // <FILEPATH, HASHCODE>

    public Stage() {
        stages = new HashMap<>();
    }

    public void addStage(String filename) throws IOException {
        File existing = new File(filename);

        if (existing.exists()) {

            Blobs newBlob = new Blobs(filename);

            File commitFile = new File(Objects, newBlob.ID);

            if (commitFile.exists()) {                  // 如果说已经commit了和当前一模一样的
                if (stages.containsKey( filename )) {
                    if (stages.get( filename).equals(newBlob.ID)) {
                        stages.remove(filename);
                    }
                    else {
                        stages.put(filename, newBlob.ID);
                    }
                }
            }

            else {
                stages.put(filename, newBlob.ID);
            }

            clearFile(Stage);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Stage));
            out.writeObject(stages);
            out.close();

        }
        else {
            System.out.println("File does not exist.");
        }
    }

    public static void clearFile(File file) throws IOException {
        new FileOutputStream(file).close();
    }
}
