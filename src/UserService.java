import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.List;

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
    public static boolean changeAboutMe(User user, String text) throws SQLException {
        return Database.changeAboutMe(user.getId(), text);
    }

    public static void showProfile(int id) {

    }


    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password) {

        return encoder.encode(password);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static User getUserData(int id) throws SQLException {
        return Database.getUserData(id);
    }

    public static List<LikedPost> getUserLikes(int userId, int page) throws SQLException {
        return Database.getUserLikes(userId, page);
    }


}
