package Models;

import DB.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


// subclass of User class --> General User.
public class GeneralUser extends User {
    // Constant variable set false for all general users.
    private static final boolean isAdmin = false;
    // List to store read articles.
    private List<String> readArticles = new ArrayList<>();
    // List to store liked categories.
    private List<Category> categories = new ArrayList<>();

    // Constructor
    public GeneralUser(String username, String password) {
        // Call super constructor.
        super(username, password, isAdmin);
    }

    // Get the list of categories associated with the user.
    public List<Category> getCategories() {
        return categories;
    }

    // Get the list of read articles associated with the user.
    public void likeCategory(Category category) {
        // Check if the category is already liked.
        if (!categories.contains(category)) {
            // Add the category to the list.
            categories.add(category);
            // Connect with the database.
            List<String> likedCategoryNames = categories.stream()
                    .map(Category::getName)
                    .collect(Collectors.toList());
            DatabaseHandler.updateLikedCategories(this.getUsername(), likedCategoryNames);
            // Load preferred articles.
            this.loadPreferredArticles();
        }
    }

    // method to load preferred articles.
    public List<Article> loadPreferredArticles() {
        // Get the list of liked categories.
        List<Category> likedCategories = getCategories();
        // Get the database handler.
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        // Create a list to store preferred articles.
        List<Article> preferredArticles = new ArrayList<>();

        // loop through the list of liked categories.
        for (Category category : likedCategories) {
            // Load articles for each category.
            Category detailedCategory = dbHandler.getCategoryWithArticles(category.getName());
            // Add the articles to the list.
            preferredArticles.addAll(detailedCategory.getArticles());
        }
        // Return the list of preferred articles.
        return preferredArticles;
    }

    // method to get read articles.
    public void readArticle(String title) {
        // Check if the article is already read.
        if (!readArticles.contains(title)) {
            // Add the article to the list.
            readArticles.add(title);
            // update read articles in the database.
            DatabaseHandler.updateReadArticles(this.getUsername(), readArticles);
        }
    }

    // method to load read articles.
    public List<Article> loadReadArticles() {
        // Get the list of read articles.
        List<String> readArticleTitles = DatabaseHandler.getReadArticles(this.getUsername());
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        // Return the list of read articles.
        return dbHandler.getAllArticles()
                .stream()
                .filter(article -> readArticleTitles.contains(article.getTitle()))
                .collect(Collectors.toList());
    }

    // method to check if an article has been read.
    public boolean hasReadArticle(String title) {
        return categories.stream()
                .flatMap(category -> category.getArticles().stream())
                .anyMatch(article -> article.getTitle().equals(title));
    }
}