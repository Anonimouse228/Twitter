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
    public static List<Post> getAllUserPosts(int id) throws SQLException {
        return Database.getAllUserPosts(id);
    }
    public static boolean likePost(int id) throws SQLException {
        Database.like(id);
        return false;
    }
    public static boolean dislikePost(int id) throws SQLException {
        Database.dislike(id);
        return false;
    }

}
