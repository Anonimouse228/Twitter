import java.util.Date;


public class Post {
    private int id;
    private String authorId;
    private String content;
    private int numberOfLikes;
    private Date timestamp;
    public Post(int id, String authorID, String content, int numberOfLikes, Date timestamp) {
        this.authorId = authorID;
        this.content = content;
        this.timestamp = timestamp;
        this.id = id;
        this.numberOfLikes = numberOfLikes;
    }

}
