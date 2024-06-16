
import java.time.LocalDateTime;


public class Post {
    private int id;
    private int authorId;
    private String content;
    private int numberOfLikes;
    private LocalDateTime timestamp;
    public Post(int id, int authorID, String content, int numberOfLikes, LocalDateTime timestamp) {
        this.authorId = authorID;
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
        this.numberOfLikes = numberOfLikes;
    }

}
