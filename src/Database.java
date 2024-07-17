import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Twitter";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Na260206";


    public static boolean logIn(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            String hashedPassword = resultSet.getString("password");

            boolean isAuthenticated = UserService.matches(user.getPassword(), hashedPassword);
            connection.close();
            return isAuthenticated;
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
        String sql = "INSERT INTO posts (authorid, content, createdAt) VALUES (?, ?, ?)";
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


    public static Integer getUserIdByLogin(String login) throws SQLException {
        String sql = "SELECT id FROM users WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                return null;
            }
        }
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
        String sql = "UPDATE posts SET numberofLikes = numberofLikes + 1 WHERE id = ?";
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
        String sql = "UPDATE posts SET numberofLikes = numberofLikes - 1 WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;

        }
    }


    private static final int POSTS_PER_PAGE = 10;
    public static List<Post> getUserPosts(int userId, int pageNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        int offset = (pageNumber - 1) * POSTS_PER_PAGE;
        String sql = "SELECT * FROM posts WHERE authorid = ? LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, POSTS_PER_PAGE);
            preparedStatement.setInt(3, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {

                int postId = resultSet.getInt("id");
                int authorID = resultSet.getInt("authorID");
                LocalDateTime createdat = resultSet.getTimestamp("createdat").toLocalDateTime();
                String content = resultSet.getString("content");
                int numberofLikes = resultSet.getInt("numberofLikes");
                Post post = new Post(postId, authorID, content, numberofLikes, createdat);
                posts.add(post);
            }
            connection.close();
            return posts;
        }
    }

    public static int getNumberOfUserPosts(int userId) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        String sql = "SELECT COUNT(*) AS post_count FROM users LEFT JOIN posts ON users.id = posts.authorid WHERE users.id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) { // Check if there's a row
                int numberOfPosts = resultSet.getInt("post_count");
                return numberOfPosts;
            } else {
                return 0; // No rows indicate no posts
            }
        }
    }


    public static boolean isPostAuthor(int postId, int userId) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        String sql = "SELECT authorid FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int authorId = resultSet.getInt("authorid");
                return authorId == userId;
            } else {
                throw new SQLException("Post not found");
            }
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




}
