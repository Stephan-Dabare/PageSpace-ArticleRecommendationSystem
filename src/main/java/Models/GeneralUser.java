package Models;

import DB.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralUser extends User {
    private List<String> likedCategories;

    public GeneralUser(String username, String password) {
        super(username, password, false);
        this.likedCategories = new ArrayList<>();
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
}
