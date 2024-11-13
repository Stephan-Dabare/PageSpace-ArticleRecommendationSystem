package OOModels;

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
    // return boolean isAdmin
    public boolean isAdmin() { return isAdmin; }
}
