package OOModels;

// Admin class extends User class
public class Admin extends User {
    // Constructor
    public Admin(String username, String password) {
        // Call the constructor of the superclass
        // Set isAdmin to true
        super(username, password, true);
    }
}
