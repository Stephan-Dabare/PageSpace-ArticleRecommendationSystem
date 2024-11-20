package OOModels;

import DatabaseControllers.DatabaseManager;
import java.util.List;

public class User {
    private String username;
    private String password;
    private boolean isAdmin;

    // Parameterized constructor
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }


    //Check whether the username is taken
    public static boolean isUsernameTaken(String newUsername) {
        DatabaseManager dbManager = new DatabaseManager();
        List<String> usernames = dbManager.getAllUsernames();
        return usernames.contains(newUsername);
    }
}
