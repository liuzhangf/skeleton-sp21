package gitlet;

import java.io.*;
import java.nio.file.Files;


public class Blobs implements Serializable {

    public String ID;
    private byte[] content;
    String FileName;

    public Blobs(String Filename) throws IOException {
        File file = new File(Filename);
        if (file.exists()) {
            /*
            FileInputStream  ois = new FileInputStream (file);
            byte[] content = new byte[(int) Filename.length()];
            ois.read(content);
             */
            this.content = Files.readAllBytes(file.toPath());
            this.ID = Utils.sha1(content);
        }
        this.FileName = Filename;
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
