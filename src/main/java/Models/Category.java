package Models;

import java.util.ArrayList;
import java.util.List;

public class Category {
    // Instance variable
    private String name;
    // List of articles in the category
    private List<Article> articles;

    // Constructor
    public Category(String name) {
        this.name = name;
        this.articles = new ArrayList<>();
    }

    // getter for name
    public String getName() {
        return name;
    }

    // getArticles method
    public List<Article> getArticles() {
        return articles;
    }

    // toString method to print the name of the category.
    @Override
    public String toString() {
        return name;
    }
}
