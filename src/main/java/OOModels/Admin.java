package OOModels;

import DatabaseControllers.DatabaseManager;

// Admin class extends User class
public class Admin extends User {
    // Constructor
    public Admin(String username, String password) {
        // Call the constructor of the superclass
        // Set isAdmin to true
        super(username, password, true);
    }

    public void adminAddArticle(Article article) {
        // Add article to the database
        DatabaseManager.addArticle(article);
    }
}
