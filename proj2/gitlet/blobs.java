package gitlet;

import java.io.Serializable;
import gitlet.Utils;


public class blobs implements Serializable {
    public String ID;
    private byte[] content;

    public blobs(byte[] content) {
        this.ID = Utils.sha1(content);
        this.content = content;
    }

}
