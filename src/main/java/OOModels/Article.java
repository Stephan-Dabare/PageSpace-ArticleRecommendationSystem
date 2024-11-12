package OOModels;

import java.awt.image.BufferedImage;
import java.util.Date;

public class Article {
    // Attributes
    private int id;
    private String title;
    private String content;
    private String category;
    private Date datePublished;
    private String source;
    private BufferedImage image;

    // Constructor
    public Article(int id, String title, String content, String category, Date datePublished, String source, BufferedImage image) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.datePublished = datePublished;
        this.source = source;
        this.image = image;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Date getDatePublished() { return datePublished; }
    public void setDatePublished(Date datePublished) { this.datePublished = datePublished; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public BufferedImage getImage() { return image; }
    public void setImage(BufferedImage image) { this.image = image; }
}

