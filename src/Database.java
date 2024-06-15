import java.sql.*;
import java.time.LocalDateTime;

public class Database {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/AlashLibrary";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Na260206";
    private static final String ADMIN_PASSWORD = "admin123";

    public static boolean logIn(String login, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            connection.close();
            return resultSet.next();
        }
    }
    public static boolean register(String surname, String lastname, String id, String login, String password) throws SQLException {
        String sql = "INSERT INTO users (name, surname, id, login, password) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        if (isLoginTaken(login) || isIdTaken(id)) {
            return false;
        }
        else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, surname);
                preparedStatement.setString(2, lastname);
                preparedStatement.setString(3, id);
                preparedStatement.setString(4, login);
                preparedStatement.setString(5, password);
//                connection.close();
                preparedStatement.executeUpdate();
            }
        }
        return true;
    }

    public static boolean addBook (String name, String author, String genre, String isbn, String language) throws SQLException {
        String sql = "INSERT INTO books (name, author, genre, isbn, language) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        if (isIsbnTaken(isbn)) {
            return false;
        }
        else {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, genre);
                preparedStatement.setString(4, isbn);
                preparedStatement.setString(5, language);
                preparedStatement.executeUpdate();
//                connection.close();
            }
        }
        return true;
    }

//    public static ResultSet showUsers () throws SQLException {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
//            Statement st = connection.createStatement();
//            return st.executeQuery("SELECT * FROM users");
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public static void showUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                System.out.println(rs.getInt("user_id") + ", " +
                        "surname: " + rs.getString("name") + ", " +
                        "lastname: " + rs.getString("surname") + ", " +
                        "login: " + rs.getString("login") + ", " +
                        "id: " + rs.getString("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showUsers(int N) throws SQLException {
        String sql = "SELECT * FROM users";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                System.out.println(rs.getInt("user_id") + ", " +
                        "surname: " + rs.getString("name") + ", " +
                        "lastname: " + rs.getString("surname") + ", " +
                        "login: " + rs.getString("login") + ", " +
                        "id: " + rs.getString("id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void transactionHistory() {

    }

    public static void transactionHistory(int N) {

    }

    public static void displayBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (true) {
                assert rs != null;
                if (!rs.next()) break;
                System.out.println(rs.getInt("id") + ", " +
                        "name: " + rs.getString("name") + ", " +
                        "author: " + rs.getString("author") + ", " +
                        "genre: " + rs.getString("genre") + ", " +
                        "isbn: " + rs.getString("isbn") + ", " +
                        "language: " + rs.getString("language") + ", ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void displayBooks(int N) throws SQLException {
        String sql = "SELECT * FROM books LIMIT ?;";
        Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, N);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ", " +
                        "name: " + rs.getString("name") + ", " +
                        "author: " + rs.getString("author") + ", " +
                        "genre: " + rs.getString("genre") + ", " +
                        "isbn: " + rs.getString("isbn") + ", " +
                        "language: " + rs.getString("language") + ", ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    public static void returnBook() {

    }

    public static boolean findBookName(String name) throws SQLException {
        String quer="SELECT * FROM books WHERE name LIKE ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(quer)) {
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                while (true) {
                    assert rs != null;
                    if (!rs.next()) break;
                    System.out.println(rs.getInt("id") + ", " +
                            "name: " + rs.getString("name") + ", " +
                            "author: " + rs.getString("author") + ", " +
                            "genre: " + rs.getString("genre") + ", " +
                            "isbn: " + rs.getString("isbn") + ", " +
                            "language: " + rs.getString("language") + ", ");
                }
            }
        }
        return false;
    }
    public static boolean findBookIsbn(String isbn) {
        String quer="SELECT * FROM books WHERE isbn LIKE ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(quer)) {
            preparedStatement.setString(1, "%" + isbn + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                while (true) {
                    assert rs != null;
                    if (!rs.next()) break;
                    System.out.println(rs.getInt("id") + ", " +
                            "name: " + rs.getString("name") + ", " +
                            "author: " + rs.getString("author") + ", " +
                            "genre: " + rs.getString("genre") + ", " +
                            "isbn: " + rs.getString("isbn") + ", " +
                            "language: " + rs.getString("language") + ", ");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static boolean findBookAuthor(String author) throws SQLException {
        String quer="SELECT * FROM books WHERE name LIKE ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(quer)) {
            preparedStatement.setString(1, "%" + author + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                while (true) {
                    assert rs != null;
                    if (!rs.next()) break;
                    System.out.println(rs.getInt("id") + ", " +
                            "name: " + rs.getString("name") + ", " +
                            "author: " + rs.getString("author") + ", " +
                            "genre: " + rs.getString("genre") + ", " +
                            "isbn: " + rs.getString("isbn") + ", " +
                            "language: " + rs.getString("language") + ", ");
                }
            }
        }
        return false;
    }
    public static boolean findBookGenre(String genre) throws SQLException {
        String quer="SELECT * FROM books WHERE name LIKE ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(quer)) {
            preparedStatement.setString(1, "%" + genre + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                while (true) {
                    assert rs != null;
                    if (!rs.next()) break;
                    System.out.println(rs.getInt("id") + ", " +
                            "name: " + rs.getString("name") + ", " +
                            "author: " + rs.getString("author") + ", " +
                            "genre: " + rs.getString("genre") + ", " +
                            "isbn: " + rs.getString("isbn") + ", " +
                            "language: " + rs.getString("language") + ", ");
                }
            }
        }
        return false;
    }


    public static boolean findBookLanguage(String language) throws SQLException {
        String quer="SELECT * FROM books WHERE name LIKE ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(quer)) {
            preparedStatement.setString(1, "%" + language + "%");
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                while (true) {
                    assert rs != null;
                    if (!rs.next()) break;
                    System.out.println(rs.getInt("id") + ", " +
                            "name: " + rs.getString("name") + ", " +
                            "author: " + rs.getString("author") + ", " +
                            "genre: " + rs.getString("genre") + ", " +
                            "isbn: " + rs.getString("isbn") + ", " +
                            "language: " + rs.getString("language") + ", ");
                }
            }
        }
        return false;
    }








    private static void addTransaction(String name, LocalDateTime timestamp, String action, String language) throws SQLException {

    }


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
    private static boolean isIdTaken(String id) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // User exists if count > 0, so return false
                }
            }
        }
        return false; // ID is not taken so return false
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