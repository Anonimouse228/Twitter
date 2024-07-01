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
                getUserPosts(user);
                break;
            case "5":
                editPost(user);
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

    private static void profileMenu() {
        System.out.println("""

                1.
                2.\s
                3.\s""");
    }

    private static void postMenu() {
        System.out.println("\n" +
                "1. " +
                "2. " +
                "3. ");
    }

    private static void postsProfile() {
        System.out.println("\n" +
                "1. " +
                "2. " +
                "3. ");
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

        System.out.println("Enter the text:");
        String content = scanner.nextLine();
        if (PostService.editPost(content)) {
            System.out.println("Shit succesfully edited!");
        } else {
            System.out.println("Something went wrong! Please repeat");
            post(user);
        }
        mainMenu(user);
    }

    private static void like(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to like:");
        int id = scanner.nextInt();
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
        if (!PostService.dislikePost(id)) {
            System.out.println("You successfully liked a post!");
        } else {
            System.out.println("Something went wrong. Please repeat");
        }
        mainMenu(user);
    }

    private static void getUserPosts(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int NumberOfPages = (Database.getNumberOfUserPosts(user.getId()) / 10) + 1;
        System.out.println("There are " + NumberOfPages + " pages. Which page to show?");
        int page;
        boolean validInput = false;
        do {
            page = scanner.nextInt();
            if (page < 1 || page > NumberOfPages) {
                System.out.println("Invalid input. Try again");
            } else {
                validInput = true;
            }
        } while (!validInput);  // Loop continues until valid page number is entered
        System.out.println(user.getLogin() + "'s posts:\n");
        List<Post> posts = PostService.getUserPosts(user.getId(), page);
        for(Post post: posts) {
            post.show();
        }
    }


    private static void showProfile(User user) {

    }
}