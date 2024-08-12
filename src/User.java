import java.sql.SQLException;
import java.time.LocalDateTime;

public class User {
    private String login;
    private String password;
    private int id;
    private String aboutMe;
    private boolean isAdmin;
    private LocalDateTime createdAt;

    public User(String login, String password, boolean isAdmin, String aboutMe, LocalDateTime createdAt) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.aboutMe = aboutMe;
        this.createdAt = createdAt;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getId() throws SQLException {
        return Database.getUserIdByLogin(this.login);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void showProfile() {
        System.out.println("========================================");
        System.out.printf(" %-12s: %s%n", "Login", login);
        System.out.printf(" %-12s: %d%n", "User ID", id);
        System.out.printf(" %-12s: %s%n", "About Me", aboutMe);
        System.out.printf(" %-12s: %s%n", "Joined", createdAt);
        System.out.println("========================================");
    }
}
