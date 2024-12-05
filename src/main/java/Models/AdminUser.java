package Models;

import DB.DatabaseHandler;
import java.util.ArrayList;
import java.util.List;

// subclass of User class --> Admin User.
public class AdminUser extends User {
    // Constant variable set true for all admin users.
    private static final boolean isAdmin = true;
    // List of articles created by the admin user.
    private List<Article> articles = new ArrayList<>();

    // Constructor
    public AdminUser(String username, String password) {
        // Call super constructor.
        super(username, password, isAdmin);
    }

    // addArticle method
    public void addArticle(Article article) {
        // Validate article
        if (validateArticle(article)) {
            // Add article to list
            articles.add(article);
            // Insert article into database.
            DatabaseHandler.insertArticle(article);
        } else {
            // Print error message
            System.err.println("Invalid article. It was not added.");
        }
    }

    // validateArticle method
    private boolean validateArticle(Article article) {
        // See if article is not null and has a title, category, and date published.
        return article != null
                && article.getTitle() != null
                && !article.getTitle().isEmpty()
                && article.getCategory() != null
                && article.getDatePublished() != null;
    }
}
