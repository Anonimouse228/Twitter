import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final int POSTS_PER_PAGE = 5;


    private static final String JDBC_URL = "jdbc:postgresql://database-1.cvyk6yk8cg2o.eu-north-1.rds.amazonaws.com:5432/postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD1 = "8KId9ES";
    private static final String PASSWORD2 = "ic8KdO4L";


    public static boolean logIn(User user) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
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
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
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

    public static boolean createPost(int authorId, String login, String content) throws SQLException {
        String sql = "INSERT INTO posts (authorid, authorlogin, content, createdAt) VALUES (?, ?, ?, ?)";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, authorId);
            preparedStatement.setString(2, login);
            preparedStatement.setString(3, content);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.executeUpdate();
            connection.close();
            }
        return true;
    }


    public static Integer getUserIdByLogin(String login) throws SQLException {
        String sql = "SELECT id FROM users WHERE login = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
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

    public static User getUserData(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                boolean isAdmin = resultSet.getBoolean("isadmin");
                String aboutMe = resultSet.getString("aboutme");
                LocalDateTime createdAt = resultSet.getTimestamp("createdat").toLocalDateTime();

                return new User(login, password, isAdmin, aboutMe, createdAt);
            } else {
                return null;
            }
        }
    }


    public static boolean editPost(String text, int postId) throws SQLException {
        String sql = "UPDATE posts SET content = ? WHERE id = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, postId);

            preparedStatement.executeUpdate();
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;
        }
    }


    public static boolean like(int postId, int userId) throws SQLException {
        String checkLikeSql = "SELECT COUNT(*) FROM likes WHERE postid = ? AND userid = ?";
        String updatePostSql = "UPDATE posts SET numberoflikes = numberoflikes + 1 WHERE id = ?";
        String insertLikeSql = "INSERT INTO likes (postid, userid, isLike) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2)) {
            connection.setAutoCommit(false);
            try (PreparedStatement checkLikeStmt = connection.prepareStatement(checkLikeSql);
                 PreparedStatement updatePostStmt = connection.prepareStatement(updatePostSql);
                 PreparedStatement insertLikeStmt = connection.prepareStatement(insertLikeSql)) {

                checkLikeStmt.setInt(1, postId);
                checkLikeStmt.setInt(2, userId);
                ResultSet resultSet = checkLikeStmt.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return false;
                }
                updatePostStmt.setInt(1, postId);
                int affectedRows = updatePostStmt.executeUpdate();
                if (affectedRows > 0) {
                    insertLikeStmt.setInt(1, postId);
                    insertLikeStmt.setInt(2, userId);
                    insertLikeStmt.setBoolean(3,true);
                    insertLikeStmt.executeUpdate();
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }



    public static boolean dislike(int postId, int userId) throws SQLException {
        String checkLikeSql = "SELECT COUNT(*) FROM likes WHERE postid = ? AND userid = ?";
        String updatePostSql = "UPDATE posts SET numberoflikes = numberoflikes - 1 WHERE id = ?";
        String insertLikeSql = "INSERT INTO likes (postid, userid, isLike) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2)) {
            connection.setAutoCommit(false);
            try (PreparedStatement checkLikeStmt = connection.prepareStatement(checkLikeSql);
                 PreparedStatement updatePostStmt = connection.prepareStatement(updatePostSql);
                 PreparedStatement insertLikeStmt = connection.prepareStatement(insertLikeSql)) {

                checkLikeStmt.setInt(1, postId);
                checkLikeStmt.setInt(2, userId);
                ResultSet resultSet = checkLikeStmt.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return false;
                }
                updatePostStmt.setInt(1, postId);
                int affectedRows = updatePostStmt.executeUpdate();
                if (affectedRows > 0) {
                    insertLikeStmt.setInt(1, postId);
                    insertLikeStmt.setInt(2, userId);
                    insertLikeStmt.setBoolean(3,false);
                    insertLikeStmt.executeUpdate();
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }



    public static List<Post> getFeed(int page) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        String sql = "SELECT * FROM posts ORDER BY createdat DESC LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 5);
            preparedStatement.setInt(2, (page-1)*5);//if page is 1 then offset is 0. If 2 then 5
             ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> posts = new ArrayList<>();

            while(resultSet.next()) {
                int postId = resultSet.getInt("id");
                int authorID = resultSet.getInt("authorID");
                String authorLogin = resultSet.getString("authorlogin");
                LocalDateTime createdat = resultSet.getTimestamp("createdat").toLocalDateTime();
                String content = resultSet.getString("content");
                int numberofLikes = resultSet.getInt("numberofLikes");

                Post post = new Post(postId, authorID, authorLogin, content, numberofLikes, createdat);
                posts.add(post);
            }
            connection.close();
            return posts;

        }
    }

    public static int getAmountOfPosts() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        String sql = "SELECT COUNT(id) FROM posts;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numberOfPosts = resultSet.getInt("count");
                return numberOfPosts;
            } else {
                return 0;
            }
        }
    }

    public static List<Post> getUserPosts(int userId, int page) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        String sql = "SELECT * FROM posts WHERE authorid = ? ORDER BY createdat LIMIT 5 OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, (page-1)*5);//if page is 1 then offset is 0. If 2 then 5
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> posts = new ArrayList<>();

            while(resultSet.next()) {
                int postId = resultSet.getInt("id");
                int authorID = resultSet.getInt("authorID");
                String authorLogin = resultSet.getString("authorlogin");
                LocalDateTime createdat = resultSet.getTimestamp("createdat").toLocalDateTime();
                String content = resultSet.getString("content");
                int numberofLikes = resultSet.getInt("numberofLikes");
                Post post = new Post(postId, authorID, authorLogin, content, numberofLikes, createdat);
                posts.add(post);
            }
            connection.close();
            return posts;

        }
    }

    public static List<LikedPost> getUserLikes(int userId, int page) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        String sql = "SELECT posts.*, likes.createdat AS likecreatedat, likes.islike FROM posts JOIN likes ON posts.id = likes.postid " +
                "WHERE likes.userid = ? ORDER BY likes.createdat LIMIT 5 OFFSET ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, (page-1)*5);//if page is 1 then offset is 0. If 2 then 5
            ResultSet resultSet = preparedStatement.executeQuery();
            List<LikedPost> posts = new ArrayList<>();

            while(resultSet.next()) {
                int postId = resultSet.getInt("id");
                int authorID = resultSet.getInt("authorID");
                String authorLogin = resultSet.getString("authorlogin");
                LocalDateTime createdat = resultSet.getTimestamp("createdat").toLocalDateTime();
                String content = resultSet.getString("content");
                int numberofLikes = resultSet.getInt("numberofLikes");
                LocalDateTime likesCreatedAt = resultSet.getTimestamp("likecreatedat").toLocalDateTime();
                boolean isLike = resultSet.getBoolean("islike");
                LikedPost likedPost = new LikedPost(postId, authorID, authorLogin, content, numberofLikes, createdat, likesCreatedAt, isLike);
                posts.add(likedPost);
            }
            connection.close();
            return posts;

        }
    }

    public static int getAmountOfUserPosts(int userId) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        String sql = "SELECT COUNT(*) AS post_count FROM users LEFT JOIN posts ON users.id = posts.authorid WHERE users.id = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numberOfPosts = resultSet.getInt("post_count");
                return numberOfPosts;
            } else {
                return 0;
            }
        }
    }



    public static boolean isPostAuthor(int postId, int userId) throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);

        String sql = "SELECT authorid FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int authorId = resultSet.getInt("authorid");
                return authorId == userId;
            } else {
                return false;
            }
        }
    }






    private static boolean isLoginTaken(String login) throws SQLException {
            String query = "SELECT COUNT(*) FROM users WHERE login = ?";
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
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


    public static boolean changeAboutMe(int userId, String text) throws SQLException {
        String sql = "UPDATE users SET aboutme = ? WHERE id = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            int affectedRows = preparedStatement.executeUpdate();
            connection.close();
            return affectedRows > 0;
        }
    }

    public static boolean hasExceededPostLimit(int userId) {
        String query = "SELECT COUNT(*) FROM posts WHERE authorid = ? AND createdat >= NOW() - INTERVAL '1 hour'";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD1 + PASSWORD2);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int postCount = rs.getInt(1);
                    return postCount <= 20;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
