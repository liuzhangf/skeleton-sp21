package gitlet;

import java.io.*;


public class Blobs implements Serializable {

    public String ID;
    private byte[] content;

    public Blobs(String Filename) throws IOException {
        File file = new File(Filename);
        FileInputStream  ois = new FileInputStream (file);
        if (file.exists()) {
            byte[] content = new byte[(int) Filename.length()];
            ois.read(content);
            this.content = content;
            this.ID = Utils.sha1(content);
        }
        ois.close();
    }

    public String getID() {
        return ID;
    }

    public byte[] getContent() {
        return content;
    }

}
