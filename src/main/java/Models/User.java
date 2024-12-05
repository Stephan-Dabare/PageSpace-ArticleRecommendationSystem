package Models;

import DB.DatabaseHandler;

import java.util.List;

public class User {
    // Attributes
    // Protected --> subclasses can access them
    protected String username;
    protected String password;
    protected boolean isAdmin;

    //Constructor
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    //Getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdmin() {
        return isAdmin;
    }

    //Check whether the username is taken
    public static boolean isUsernameTaken(String Username) {
        DatabaseHandler dbManager = new DatabaseHandler();
        List<String> usernames = dbManager.getAllUsernames();
        return usernames.contains(Username);
    }
}
