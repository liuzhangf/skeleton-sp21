package gitlet;
import java.io.*;

/*
    只是单纯的把结构变一下
    之前单分支的时候，是Head文件指向最新的一次Commit
    然而， 这次之后， Head 文件指向的是当前的Branch， 每个Branch 存的的 File 类型的 指向当前
    Branch的最新的一次Commit Id。
 */

public class Branch implements Serializable {
    public String name;
    private File latestCommitFile;
    private File newBranchFile;

    /*
        name 存的是branch的名字。
        lastCommitFile 存储的是当前Branch的最新的一次Commit文件。
        newBranchFile存的是当前的Branch文件。
        这个Commit File指向的是当前的Head文件。
    */

    public Branch(String command,String name) throws IOException, ClassNotFoundException {
        if (command.equals("create")) {
            createNewBranch(name);
        }
        else if (command.equals("delete")) {
            removeBranch(name);
        }
    }

    private void createNewBranch(String name) throws IOException, ClassNotFoundException {
        File newBranch = new File(Main.Branches, name);
        if (!newBranch.exists()) {
            this.name = name;
            latestCommitFile = null;
            newBranch.createNewFile();
            String LastBranches = Utils.readContentsAsString(Main.Head).trim();
            String lastCommitHashCode = Utils.readContentsAsString(new File(Main.Branches, LastBranches));
            Utils.writeContents(newBranch, lastCommitHashCode);
        }
        else {
            System.out.println("A branch with that name already exists.");
        }
    }


    public void removeBranch(String name) {
        File deleteBranch = new File(Main.Branches, name);
        if (!deleteBranch.exists()) {
            System.out.println("A branch with that name does not exist.");
        }
        else {
            String currentBranchName = Utils.readContentsAsString(Main.Head);

            /*试图删除当前的分支*/
            if (currentBranchName.equals(name)) {
                System.out.println("Cannot remove the current branch.");
            }
            /*否则就删除当前的分支*/
            else {
                deleteBranch.delete();
            }
        }
    }

    public String getBranchName() {
        return this.name;
    }
}
