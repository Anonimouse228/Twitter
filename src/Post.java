
import java.time.LocalDateTime;


public class Post {
    protected int id;
    protected int authorId;
    protected String content;
    protected int numberOfLikes;
    protected String authorLogin;
    protected LocalDateTime timestamp;

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
        System.out.printf("Post ID: %d | Login: %s | User's ID: %s%n", id, authorLogin, authorId);
        System.out.printf(" %-12s: %s%n", "Content", content);
        System.out.printf(" %-12s: %d%n", "Rating", numberOfLikes);
        System.out.printf(" %-12s: %s | %s%n", "Posted at", Time.timeFormatter(timestamp), Time.timeAgo(timestamp));
        System.out.println("----------------------------------------");
    }

    public int getId() {
        return id;
    }
}
