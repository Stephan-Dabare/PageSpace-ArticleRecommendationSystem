package Models;

import DB.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralUser extends User {
    private static final boolean isAdmin = false;
    private List<String> likedCategories = new ArrayList<>();
    private List<String> readArticles = new ArrayList<>();

    public GeneralUser(String username, String password) {
        super(username, password, isAdmin);
        this.likedCategories = new ArrayList<>();
        this.readArticles = new ArrayList<>();
    }

    //
    public void likeCategory(String category) {
        if (!likedCategories.contains(category)) {
            likedCategories.add(category);
            DatabaseHandler.updateLikedCategories(this.getUsername(), likedCategories);
        }
    }

    public List<Article> loadPreferredArticles() {
        List<String> preferredCategories = DatabaseHandler.getLikedCategories(this.getUsername());
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        return dbHandler.getAllArticles()
                .stream()
                .filter(article -> preferredCategories.contains(article.getCategory().getName()))
                .collect(Collectors.toList());
    }

    public void readArticle(String title) {
        if (!readArticles.contains(title)) {
            readArticles.add(title);
            DatabaseHandler.updateReadArticles(this.getUsername(), readArticles);
        }
    }

    public List<Article> loadReadArticles() {
        List<String> readArticles = DatabaseHandler.getReadArticles(this.getUsername());
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        return dbHandler.getAllArticles()
                .stream()
                .filter(article -> readArticles.contains(article.getTitle()))
                .collect(Collectors.toList());
    }

    public boolean hasReadArticle(String title) {
        return readArticles.contains(title);
    }
}
