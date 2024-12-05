package Models;

import java.awt.image.BufferedImage;
import java.util.Date;

public class Article {
    // Instance variables
    private String title;
    private String content;
    private Category category;
    private AdminUser createdBy;
    private Date datePublished;
    private BufferedImage image;

    // Parameterized constructor
    public Article(String title, String content, Category category, AdminUser createdBy, Date datePublished, BufferedImage image) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdBy = createdBy;
        this.datePublished = datePublished;
        this.image = image;
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public Category getCategory() {
        return category;
    }
    public Date getDatePublished() {
        return datePublished;
    }
    public BufferedImage getImage() {
        return image;
    }
    public AdminUser getCreatedBy() {
        return createdBy;
    }
}
