import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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


    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }


}
