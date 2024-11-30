package Models;

import DB.DatabaseHandler;

import java.util.List;

public class User {
    protected String username;
    protected String password;
    protected boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

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
