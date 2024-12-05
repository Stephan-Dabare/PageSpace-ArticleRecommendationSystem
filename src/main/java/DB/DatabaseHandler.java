package DB;

// Imports
import Models.Article;
import Models.Category;
import Models.User;
import Models.GeneralUser;
import Models.AdminUser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseHandler {
    // Database URL for sqlite DB.
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";
    // Connection object
    private Connection connection;
    // DB instance
    private static DatabaseHandler instance;

    // Constructor
    public DatabaseHandler() {
        createConnection();
    }

    // Get instance of the DB
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            // Create a new instance if it doesn't exist.
            instance = new DatabaseHandler();
        }
        return instance;
    }

    // Create connection to the DB.
    private void createConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a new user
    public static void insertUser(User user) {
        String query = "INSERT INTO Users (username, password, is_admin) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.isAdmin() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate a user with username and password.
    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            // Check if the user exists
            if (rs.next()) {
                boolean isAdmin = rs.getInt("is_Admin") == 1;
                if (isAdmin) {
                    return new AdminUser(username, password);
                } else {
                    return new GeneralUser(username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Insert a new article
    public static void insertArticle(Article article) {
        String query = "INSERT INTO Articles (title, content, category, created_by, date_published, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getCategory().getName());
            pstmt.setString(4, article.getCreatedBy().getUsername());
            if (article.getDatePublished() != null) {
                pstmt.setDate(5, new java.sql.Date(article.getDatePublished().getTime()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            pstmt.setBytes(6, bufferedImageToBytes(article.getImage()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all articles
    // synchronized to manage concurrent access.
    public synchronized List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate over the result set and create article objects.
            while (rs.next()) {
                // Create an article object
                Article article = new Article(
                        rs.getString("title"),
                        rs.getString("content"),
                        new Category(rs.getString("category")),
                        new AdminUser(rs.getString("created_by"), "adminPassword"),
                        new java.sql.Date(rs.getDate("date_Published").getTime()),
                        bytesToBufferedImage(rs.getBytes("image"))
                );
                // Add the article to the list
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return the list of articles
        return articles;
    }

    // Convert a buffered image to bytes
    public static byte[] bufferedImageToBytes(BufferedImage image) {
        if (image == null) return null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert bytes to buffered image
    private static BufferedImage bytesToBufferedImage(byte[] bytes) {
        if (bytes == null) return null;
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all users
    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    // Update liked categories
    public static void updateLikedCategories(String username, List<String> likedCategories) {
        String sql = "UPDATE users SET liked_category = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String likedCategoriesString = String.join(",", likedCategories);
            pstmt.setString(1, likedCategoriesString);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update read articles
    public static void updateReadArticles(String username, List<String> readArticles) {
        String sql = "UPDATE users SET read_articles = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String readArticlesString = String.join(",", readArticles);
            pstmt.setString(1, readArticlesString);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get read articles
    public static List<String> getReadArticles(String username) {
        String sql = "SELECT read_articles FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String readArticles = rs.getString("read_articles");
                if (readArticles != null && !readArticles.isEmpty()) {
                    return Arrays.asList(readArticles.split(","));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return an empty list if no read articles.
        return new ArrayList<>();
    }

    // Get liked categories
    // synchronized to manage concurrent access
    public synchronized Category getCategoryWithArticles(String categoryName) {
        // Create a category object
        Category category = new Category(categoryName);
        String sql = "SELECT * FROM Articles WHERE category = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create an article object.
                Article article = new Article(
                        rs.getString("title"),
                        rs.getString("content"),
                        category,
                        new AdminUser(rs.getString("created_by"), "adminPassword"),
                        rs.getDate("date_published"),
                        bytesToBufferedImage(rs.getBytes("image"))
                );

                // Add the article to the category.
                category.getArticles().add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }
}
