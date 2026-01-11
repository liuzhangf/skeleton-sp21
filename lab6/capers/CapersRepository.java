package capers;

import java.io.*;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils
    static final File DOG_FOLDER = Utils.join(CAPERS_FOLDER, ".dogs");
    static final File STORY_FILE = Utils.join(CAPERS_FOLDER, ".story");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */

    public static void setupPersistence() throws IOException {
        if (!CAPERS_FOLDER.exists()) {
            CAPERS_FOLDER.mkdir();
        }
        if (!DOG_FOLDER.exists()) {
            DOG_FOLDER.mkdir();
        }
        if (!STORY_FILE.exists()) {
            try{
                STORY_FILE.createNewFile();
            }catch (IOException e){
                exitWithError("创建story文件失败！");
            }
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public void writeStory(String text) {
        String oldstory = Utils.readContentsAsString(STORY_FILE);
        String newStory = oldstory + text + "\n";
        Utils.writeContents(STORY_FILE, newStory);
        System.out.println(newStory);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */

    public static Dog makeDog(String breed, String name, int age) throws IOException {
        Dog newdog = new Dog(name, breed, age);
        File dogFile = new File(DOG_FOLDER, name );
        if (!dogFile.exists()) {
            dogFile.createNewFile();
        }
        ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(dogFile));

        System.out.println(newdog.toString());
        out.writeObject(newdog);
        out.close();
        return newdog;
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) throws IOException, ClassNotFoundException {
        Dog thisdog = Dog.fromFile(name);

        thisdog.haveBirthday();

        File dogFile = new File(DOG_FOLDER, name );
        if (!dogFile.exists()) {
            dogFile.createNewFile();
        }
        ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(dogFile));
        out.writeObject(thisdog);
    }
}
