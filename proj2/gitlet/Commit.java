package gitlet;
import java.io.*;
import java.util.*;
import java.io.Serializable;
import gitlet.Utils;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */

public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    public final String ID;
    public String parentID1;
    public String parentID2;
    private final long timestamp;

    private  Map<String, String> blobid;

    /** The message of this Commit. */
    private String message;

    public Commit(long timestamp, String message, String parentID1, String parentID2, String[] BolbID, String[] text) {
        this.timestamp = timestamp;
        this.message = message;
        this.parentID1 = parentID1;
        this.parentID2 = parentID2;
        blobid = new HashMap<>();
        if (BolbID != null && text != null) {
            matchBolbWithCommit(BolbID, text);
        }
        this.ID = Utils.sha1(this.message, timestamp, blobid, parentID1, parentID2);
    }

    public void matchBolbWithCommit (String[] BolbID, String[] text) {
        for (int i = 0; i < BolbID.length; i++) {
            blobid.put(BolbID[i], text[i]);
        }
    }

    /* TODO: fill in the rest of this class. */
}
