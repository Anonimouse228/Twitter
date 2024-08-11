import java.sql.SQLException;
import java.util.List;

public class PostService {
    public static boolean createPost(int authorId, String content) throws SQLException {

        return Database.createPost(authorId, content);
    }
    public static boolean editPost(String content, int postId) throws SQLException {
        return Database.editPost(content, postId);

    }
    public static void deletePost() {

    }
    public static void getPosts() {

    }
    public static void getPosts(int n) {

    }
    public static List<Post> getUserPosts(int id, int page) throws SQLException {
        return Database.getUserPosts(id, page);
    }
    public static List<Post> getFeed(int page) throws SQLException {
        return Database.getFeed(page);
    }
    public static boolean likePost(int postid, int userid) throws SQLException {
        return Database.like(postid, userid);
    }
    public static boolean dislikePost(int postid, int userid) throws SQLException {
        return Database.dislike(postid, userid);
    }
}
