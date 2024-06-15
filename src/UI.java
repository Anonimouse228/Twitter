import java.util.Objects;
import java.util.Scanner;

public class UI {
    private int userId;
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Shitter!\n" +
                "1. Registration\n" +
                "2. LogIn\n" +
                "3. Exit");
        String choice = scanner.nextLine();
        if (Objects.equals(choice, "1")) {
            logIn();
        } else if (Objects.equals(choice, "2")) {
            register();
        } else if (Objects.equals(choice, "3")) {
            return;
        }
        else {
            System.out.println("Invalid input! please type \"1\", \"2\" or \"3\"!");
            start();
        }
    }
    private static void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your desired login:");
        String login = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        User user = new User(login, password, false);
        UserService.registerUser(user);
    }
    private static void logIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your login:");
        String login = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        User user = new User(login, password, false);
        if (UserService.loginUser(user)) {
            mainMenu(user);
        }


    }
    private static void mainMenu(User user) {
        System.out.println("\n" +
                "1. Take a shit(post)\n" +
                "2. Like a shit(post)\n" +
                "3. Dislike a shit(post)\n" +
                "4. Show last N shits\n" +
                "5. Profile\n " +
                "6. Log out\n" +
                "7. Exit");
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
                dislike();
                break;
            case "4":
                showPosts();
                break;
            case "5":
                showProfile();
                break;
            case "6":

                break;
            case "7":

                break;
        }
    }

    private static void profileMenu() {
        System.out.println("\n" +
                "1. \n" +
                "2. \n" +
                "3. ");
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

    private static void post(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the text:");
        String text = scanner.nextLine();
        if (!PostService.createPost()) {
            System.out.println("Something went wrong! Please repeat");
            post(user);
        }
        else{
            System.out.println("You successfully shitted!");
        }
        mainMenu(user);
    }

    private static void like(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the id of the post that you'd like to like:");
        String id = scanner.nextLine();

        if (!PostService.likePost()) {
            System.out.println("Something went wrong. Please repeat");
            like(user);
        } else{
            System.out.println("You successfully liked a post!");

        }
        mainMenu(user);
    }
    private static void dislike() {

    }

    private static void showPosts() {

    }
    private static void showProfile() {
 
    }
}