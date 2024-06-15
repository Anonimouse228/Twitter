import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;

public class UserService {
    public static boolean registerUser(User user) throws SQLException {
        return Database.register(user);

    }
    public static boolean loginUser(User user) throws SQLException {
        return Database.logIn(user);

    }
    public static void changePassword(int userId, String oldPassword, String newPassword) {

    }
    public static void getUserById(int userId) {

    }
    public static void getAllUsers() {

    }

    public static void showProfile(User user) {
        System.out.println(user.toString());
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
