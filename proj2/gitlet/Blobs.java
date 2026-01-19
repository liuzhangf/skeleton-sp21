package gitlet;

import java.io.*;
import java.nio.file.Files;


public class Blobs implements Serializable {

    public String ID;
    private byte[] content;
    String FileName;
    String whichbranch;

    public Blobs(String Filename) throws IOException {
        File file = new File(Filename);
        if (file.exists()) {
            /*
            FileInputStream  ois = new FileInputStream (file);
            byte[] content = new byte[(int) Filename.length()];
            ois.read(content);
             */
            this.FileName = Filename;
            this.whichbranch = Utils.readContentsAsString(Main.Head);
            this.content = Files.readAllBytes(file.toPath());
            this.ID = Utils.sha1(content, whichbranch,FileName);
        }
    }

    public String getID() {
        return ID;
    }

    public byte[] getContent() {
        return content;
    }

    public String getFileName() {
        return FileName;
    }
}
