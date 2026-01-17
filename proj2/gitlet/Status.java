package gitlet;


import jdk.javadoc.internal.doclets.formats.html.markup.Head;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Status implements java.io.Serializable {
    public  Status () throws IOException, ClassNotFoundException {
        printTheStatus();
    }

    private void printTheStatus() throws IOException, ClassNotFoundException {
        /*
            打印全部的branch
         */
        System.out.println("=== Branches ===");
        System.out.println("*" + Utils.readContentsAsString(Main.Head));
        for (File branchFile : Main.Branches.listFiles()) {
            if (branchFile.getName().equals(Utils.readContentsAsString(Main.Head))) {
                continue;
            }
            else {
                System.out.println(branchFile.getName());
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Main.Stage));
        Stage currentStage = (Stage) ois.readObject();
        if (currentStage != null) {
            for (String fileName : currentStage.stages.keySet()) {
                System.out.println(fileName);
            }
        }
        System.out.println();

        System.out.println("=== Removed Files ===");

        if (currentStage != null) {
            for (String fileName : currentStage.deleteFiles) {
                System.out.println(fileName);
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }
}
