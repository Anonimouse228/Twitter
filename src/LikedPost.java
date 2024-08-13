import java.time.LocalDateTime;

public class LikedPost extends Post{

    private LocalDateTime likedAt;
    private boolean isLike;
    public LikedPost(int id, int authorID, String authorLogin, String content, int numberOfLikes,
                     LocalDateTime timestamp, LocalDateTime likedAt, boolean isLike) {
        super(id, authorID, authorLogin, content, numberOfLikes, timestamp);
        this.isLike = isLike;
        this.likedAt = likedAt;
    }


    public void show() {
        System.out.println("----------------------------------------");
        System.out.printf(" Post ID: %d | Login: %s | User's ID: %s%n", id, authorLogin, authorId);
        System.out.printf("  %-15s: %s%n", "Content", content);
        System.out.printf("  %-15s: %d%n", "Rating", numberOfLikes);
        System.out.printf("  %-15s: %s | %s%n", "Posted at", Time.timeFormatter(timestamp), Time.timeAgo(timestamp));
        System.out.printf("  %-15s: %s | %s%n", isLike ? "Liked at" : "Disliked at", likedAt, Time.timeAgo(likedAt));
        System.out.println("----------------------------------------");

    }
}
