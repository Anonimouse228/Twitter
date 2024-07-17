import javax.swing.event.ListDataEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UI {
    private int userId;
    public static void start() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Welcome to the Shitter!
                1. Registration
                2. LogIn
                3. Exit""");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> register();
            case "2" -> logIn();
            case "3" -> {}

            case null, default -> {
                System.out.println("Invalid input! please type \"1\", \"2\" or \"3\"!");
                start();
            }
        }
    }
    private static void register() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your desired login(no more than 20 symbols):");
        String login = scanner.nextLine();
        if (login.length() > 20) {
            System.out.println("Length of login should be <= 20!");
            start();
        }


        System.out.println("Enter your password(no more than 20 symbols):");
        String password = scanner.nextLine();
        if (password.length() > 20 || password.length() < 4) {
            System.out.println("Length of password should be <= 20 and >= 4!");
            start();
        }
        User user = new User(login, UserService.hashPassword(password), false);
        if(!UserService.registerUser(user)) {
            System.out.println("Something went wrong! Please repeat");
            start();
        } else {
            System.out.println("Successful registration!");
            mainMenu(user);
        }

    }
    private static void logIn() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your login:");
        String login = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        User user = new User(login, password, false);

        if(UserService.loginUser(user)) {
            System.out.println("Successful logIn!");
            mainMenu(user);
        }
        else {
            System.out.println("Login or password not correct");
            start();
        }


    }
    private static void mainMenu(User user) throws SQLException {
        System.out.println("""

                1. Take a shit(post)
                2. Like a shit
                3. Dislike a shit
                4. Edit your shit
                5. Show last N shits
                6. Your profile
                7. Log out
                8. Exit""");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                post(user);
                break;
            case "2":
                like(user);
                break;
            case "3":
                dislike(user);
                break;
            case "4":
                editPost(user);
                break;
            case "5":

                break;
            case "6":
                showProfile(user);
                break;
            case "7":

                break;
            case "8":

                break;
        }
    }

    private static void adminMenu() {

    }

    private static void post(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the text(<140 symbols):");
        String content = scanner.nextLine();
        if (content.length() > 140) {
            System.out.println("C'mon, it's twitter(shitter). The text should be <= 140");
            start();
        }

        if (PostService.createPost(user.getId(), content)) {
            System.out.println("You successfully shitted!");
        } else {
            System.out.println("Something went wrong! Please repeat");
            post(user);
        }
        mainMenu(user);
    }

    private static void editPost(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        List<Post> userPosts = PostService.getAllUserPosts(user.getId()); // Fetch all posts at once

        while (true) {
            System.out.println("\nHere are your posts (enter 'q' to quit):");
            for (Post post : userPosts) {
                post.show();
            }

            System.out.println("\nEnter the post ID to edit (or 'q' to quit):");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                break;
            }
            int postId;
            try {
                postId = Integer.parseInt(input);
                if (!userPosts.stream().anyMatch(post -> post.getId() == postId)) {
                    System.out.println("Invalid post ID. Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number or 'q' to quit.");
                continue;
            }
            System.out.println("Enter the new text (<= 140 characters):");
            String content = scanner.nextLine().trim();
            if (content.length() > 140) {
                System.out.println("C'mon, it's Twitter. Keep it under 140 characters.");
                continue;
            }
            if (PostService.editPost(content, postId)) {
                System.out.println("Post successfully edited!");
                break;
            } else {
                System.out.println("Something went wrong! Please try again.");
            }
        }

        mainMenu(user);
    }

    private static void like(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to like:");
        int id = scanner.nextInt();
        if (Database.isPostAuthor(id, user.getId())) {
            System.out.println("You can't like your own post!");
            mainMenu(user);
        }
        if (!PostService.likePost(id)) {
            System.out.println("You successfully liked a post!");
        } else {
            System.out.println("Something went wrong. Please repeat");
        }
        mainMenu(user);
    }
    private static void dislike(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to dislike:");
        int id = scanner.nextInt();
        //if (Database.isPostAuthor(id, user.getId())) {
        //    System.out.println("You can't dislike your own post!");
        //}
        if (!PostService.dislikePost(id)) {
            System.out.println("You successfully liked a post!");
        } else {
            System.out.println("Something went wrong. Please repeat");
        }
        mainMenu(user);
    }

    private static List<Post> showPosts() {

    }

    private static void showProfile(User user) {

    }
}