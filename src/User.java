import java.sql.Timestamp;
import java.util.Date;

public class User {
    private String login;
    private String password;
    private int id;
    private boolean isAdmin;
    private Date createdAt;

    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = UserService.hashPassword(password);
        this.isAdmin = isAdmin;

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", isAdmin=" + isAdmin +
                ", createdAt=" + createdAt +
                '}';
    }
}
