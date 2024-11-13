package OOModels;

import DatabaseControllers.DatabaseManager;
import java.util.ArrayList;
import java.util.List;

public class GeneralUser extends User {
    private DatabaseManager dbManager;
    private List<String> likedCategories;

    // parameterized constructor
    public GeneralUser(String username, String password) {
        super(username, password, false);
        this.dbManager = new DatabaseManager();
        this.likedCategories = new ArrayList<>();
    }

    public void likeCategory(String category) {
        if (!likedCategories.contains(category)) {
            likedCategories.add(category);
            dbManager.updateLikedCategoriesInDB(this.getUsername(), likedCategories);
        }
    }
}