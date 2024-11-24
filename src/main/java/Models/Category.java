package Models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Article> articles;

    public Category(String name) {
        this.name = name;
        this.articles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return articles;
    }

    public String getName() {
        return name;
    }

    // Override toString() to return the category name
    @Override
    public String toString() {
        return name;
    }
}
