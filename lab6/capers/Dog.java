package capers;

import java.io.*;

import static capers.Utils.*;

/** Represents a dog that can be serialized.
 * @author TODO
*/
public class Dog implements Serializable { // TODO

    static final File CWD = new File(System.getProperty("user.dir"));
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");

    static final File DOG_FOLDER = Utils.join(CAPERS_FOLDER, ".dogs"); // TODO (hint: look at the `join`
                                         //      function in Utils)

    /** Age of dog. */
    private int age;
    /** Breed of dog. */
    private String breed;
    /** Name of dog. */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */

    public static Dog fromFile(String name) throws IOException, ClassNotFoundException {
        File dogFile = new File(DOG_FOLDER, name );
        ObjectInputStream inp =
                new ObjectInputStream(new FileInputStream(dogFile));
        Dog thisdog;
        thisdog = (Dog) inp.readObject();
        inp.close();
        return thisdog;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog(String name, Dog thisdog) throws IOException {
        // TODO (hint: don't forget dog names are unique)
        File dogFile = new File(DOG_FOLDER, name );
        if (!dogFile.exists()) {
            dogFile.createNewFile();
        }
        ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(dogFile));
        out.writeObject(thisdog);
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            name, breed, age);
    }

}
