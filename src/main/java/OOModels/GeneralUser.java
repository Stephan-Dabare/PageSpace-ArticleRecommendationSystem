package OOModels;

// GeneralUser class extends User class
public class GeneralUser extends User {
    // Constructor
    public GeneralUser(String username, String password) {
        // Call the constructor of the superclass
        // Set isAdmin to false
        super(username, password, false);
    }
}