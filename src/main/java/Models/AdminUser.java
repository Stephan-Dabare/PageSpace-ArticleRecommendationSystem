package Models;

import DB.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class AdminUser extends User {
    private List<Article> articles = new ArrayList<>();

    public AdminUser(String username, String password) {
        super(username, password, true);
    }

    public void addArticle(Article article) {
        if (validateArticle(article)) {
            articles.add(article);
            DatabaseHandler.insertArticle(article);
        } else {
            System.err.println("Invalid article. It was not added.");
        }
    }

    private boolean validateArticle(Article article) {
        return article != null
                && article.getTitle() != null
                && !article.getTitle().isEmpty()
                && article.getCategory() != null
                && article.getDatePublished() != null;
    }
}
