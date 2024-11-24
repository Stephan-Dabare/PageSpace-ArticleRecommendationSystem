package DatabaseControllers;

import OOModels.Admin;
import OOModels.GeneralUser;
import OOModels.User;
import OOModels.Article;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static String dbUrl = "jdbc:sqlite:database.db";
    private Connection connection;

    public DatabaseManager() {
        createConnection();
    }

    private void createConnection() {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, isAdmin) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.isAdmin() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                boolean isAdmin = rs.getInt("isAdmin") == 1;
                if (isAdmin) {
                    return new Admin(username, password);
                } else {
                    return new GeneralUser(username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT username FROM users";
        try (Connection conn = DriverManager.getConnection(dbUrl);
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

    public static void addArticle(Article article) {
        String sql = "INSERT INTO articles (title, content, category, datePublished, source, image) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getCategory());

            if (article.getDatePublished() != null) {
                pstmt.setDate(4, new java.sql.Date(article.getDatePublished().getTime()));
            } else {
                pstmt.setNull(4, Types.DATE);
            }

            pstmt.setString(5, article.getSource());
            pstmt.setBytes(6, bufferedImageToBytes(article.getImage()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("category"),
                        rs.getDate("datePublished"),
                        rs.getString("source"),
                        bytesToBufferedImage(rs.getBytes("image"))
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

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

    private static BufferedImage bytesToBufferedImage(byte[] bytes) {
        if (bytes == null) return null;
        try {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateLikedCategoriesInDB(String username, List<String> likedCategories) {
        String sql = "UPDATE users SET liked_category = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) { // Use existing connection
            String likedCategoriesString = String.join(",", likedCategories);
            pstmt.setString(1, likedCategoriesString);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLikedCategories(String username) {
        String sql = "SELECT liked_category FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String likedCategories = rs.getString("liked_category");
                if (likedCategories != null && !likedCategories.isEmpty()) {
                    return Arrays.asList(likedCategories.split(","));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}