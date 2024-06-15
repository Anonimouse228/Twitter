import java.sql.Timestamp;


public class Post {
    private int id;
    private String authorId;
    private String content;
    private int numberOfLikes;
    private Timestamp timestamp;
    public Post(int id, String authorID, String content, int numberOfLikes, Timestamp timestamp) {
        this.authorId = authorID;
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
        this.numberOfLikes = numberOfLikes;
    }

}
