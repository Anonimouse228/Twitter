
import java.time.LocalDateTime;


public class Post {
    private int id;
    private int authorId;
    private String content;
    private int numberOfLikes;
    private String authorLogin;
    private LocalDateTime timestamp;
    public Post(int id, int authorID, String authorLogin, String content, int numberOfLikes, LocalDateTime timestamp) {
        this.authorId = authorID;
        this.authorLogin = authorLogin;
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
        this.numberOfLikes = numberOfLikes;
    }


    public void show() {
        System.out.println("----------------------------------------");
        System.out.printf("User's ID: %d | Login: %s | Post ID: %s%n", authorId, authorLogin, id);
        System.out.printf(" %-12s: %s%n", "Content", content);
        System.out.printf(" %-12s: %s%n", "Timestamp", timestamp);
        System.out.printf(" %-12s: %d%n", "Likes", numberOfLikes);
        System.out.println("----------------------------------------");
    }



    public int getId() {
        return id;
    }
}
