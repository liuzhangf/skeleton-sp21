package gitlet;

import java.io.*;


public class Blobs implements Serializable {

    public String ID;
    private byte[] content;
    String FileName;

    public Blobs(String Filename) throws IOException {
        File file = new File(Filename);
        if (file.exists()) {
        //    System.out.println("File exists And will be overwritten");
            FileInputStream  ois = new FileInputStream (file);
            byte[] content = new byte[(int) Filename.length()];
            ois.read(content);
            this.content = content;
            this.ID = Utils.sha1(content);
            ois.close();
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
