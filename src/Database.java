import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/AlashLibrary";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Na260206";


    public static boolean logIn(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            return resultSet.next();
        }
    }
    public static boolean register(User user) throws SQLException {
        String sql = "INSERT INTO users (login, password, isAdmin, createdAt) VALUES (?, ?, ?, ?)";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        if (isLoginTaken(user.getLogin())) {
            connection.close();
            return false;
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setBoolean(3, false);
                preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            } finally {
                connection.close();
            }
        }
        return true;
    }

    public static boolean createPost(int authorId, String content) throws SQLException {
        String sql = "INSERT INTO posts (authorId, content, timestamp) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);
            preparedStatement.setString(2, content);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            connection.close();
            }
        return true;
    }

    public static boolean editPost(String text) throws SQLException {
        String sql = "UPDATE posts SET content = text";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, text);
            preparedStatement.executeUpdate();
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;
        }
    }


    public static boolean like(int id) throws SQLException {
        String sql = "UPDATE posts SET rating = likes + 1 WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;
        }
    }

    public static boolean dislike(int id) throws SQLException {
        // SQL to increment the likes for a given post id
        String sql = "UPDATE posts SET rating = likes - 1 WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;

        }
    }

//    public static List<Post> getBooks() throws SQLException {
//        List<Post> books = new ArrayList<>();
//        String sql = "SELECT * FROM books";
//        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                Post post = new Post(
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getString("author"),
//                        rs.getString("genre"),
//                        rs.getString("isbn"),
//                        rs.getString("language")
//                );
//                books.add(post);
//            }
//        } catch (SQLException e) {
//            System.err.println(e.getMessage());
//        }
//        return books;
//    }









    private static boolean isLoginTaken(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // User exists if count > 0
                }
            }
        }
        return false; // Login is not taken, so return false
    }

    private static boolean isIsbnTaken(String isbn) throws SQLException {
        String query = "SELECT COUNT(*) FROM books WHERE isbn = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // isbn taken if count > 0
                }
            }
        }
        return false; // isbn is not taken, so return false
    }
}