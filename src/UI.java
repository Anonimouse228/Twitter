import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
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

                1. Show posts
                2. Take a shit(post)
                3. 
                4. Your profile
                5. 
                6. 
                7. Log out
                8. Exit""");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showFeed(user);
                break;
            case "2":
                post(user);
                break;
            case "3":
                editPost(user);
                break;
            case "4":
                showProfile(user);
                break;
            case "5":

                break;
            case "6":

                break;
            case "7":

                break;
            case "8":

                break;
        }
    }

    private static void adminMenu() {

    }

//    private static void showFeed(User user) throws SQLException {
//        Scanner scanner = new Scanner(System.in);
//        int amountOfPages = (Database.getAmountOfPosts() / 5) + 1;
//        int page = 1;
//        List<Post> posts = PostService.getFeed(page);
//        while(true) {
//            for (Post post : posts) {
//                post.show();
//            }
//            System.out.println("Page " + page + "out of " + amountOfPages);
//            System.out.println("\"l\" - like a post" +
//                               "\"d\" - dislike a post" +
//                               "\"(number)\" - go to page (number)" +
//                               "\"q\" - quit to Main Menu");
//            String choice = scanner.nextLine();
//            if (Objects.equals(choice, "l")) {
//                like(user);
//            } else if (Objects.equals(choice, "d")) {
//                dislike(user);
//            } else if (Objects.equals(choice, "q")) {
//                mainMenu(user);
//            } else if (choice.chars().allMatch( Character::isDigit )) {
//                posts = PostService.getFeed(Integer.parseInt(choice));
//                continue;
//            } else {
//                System.out.println("Invalid Input, please repeat");
//                showFeed(user);
//            }
//        }
//    }

    private static void showFeed(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int amountOfPages = (Database.getAmountOfPosts() / 5) + 1;
        int page = 1;
        List<Post> posts = PostService.getFeed(page);

        while (true) {

            for (Post post : posts) {
                post.show();
            }
            System.out.println("\nPage " + page + " out of " + amountOfPages);
            System.out.println("\"l\" - like a post | " +
                    "\"d\" - dislike a post | " +
                    "\"(number)\" - go to page (number) | " +
                    "\"q\" - quit to Main Menu");

            String choice = scanner.nextLine().trim();

            if (Objects.equals(choice, "l")) {
                like(user);
            } else if (Objects.equals(choice, "d")) {
                dislike(user);
            } else if (Objects.equals(choice, "q")) {
                mainMenu(user);
                break;
            } else if (choice.chars().allMatch(Character::isDigit)) {
                int selectedPage = Integer.parseInt(choice);


                if (selectedPage > 0 && selectedPage <= amountOfPages) {
                    page = selectedPage;
                    posts = PostService.getFeed(page);
                } else {
                    System.out.println("Invalid page number, please try again.");
                }
            } else {
                System.out.println("Invalid Input, please repeat");
            }
        }
        mainMenu(user);
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
        int amountOfPages = (Database.getAmountOfUserPosts(user.getId()) / 5) + 1;
        int page = 1;
        List<Post> posts = PostService.getUserPosts(user.getId(), page);

        while (true) {

            for (Post post : posts) {
                post.show();
            }
            System.out.println("\nPage " + page + " out of " + amountOfPages);
            System.out.println("\"e\" - edit this post | " +
                    "\"(number)\" - go to page (number) | " +
                    "\"q\" - quit to Main Menu");

            String choice = scanner.nextLine().trim();

            if (Objects.equals(choice, "e")) {
                System.out.println("Enter the id of the post:");
                String id = scanner.nextLine();
                System.out.println("Enter the text(<140 symbols):");
                String content = scanner.nextLine();
                if (content.length() > 140) {
                    System.out.println("C'mon, it's twitter(shitter). The text should be <= 140");
                    showProfile(user);
                }
                if (PostService.editPost(content, Integer.parseInt(id))) {
                    System.out.println("You successfully edited your shit!");
                } else {
                    System.out.println("Something went wrong! Please repeat");
                    showProfile(user);
                }

            } else if (Objects.equals(choice, "q")) {
                mainMenu(user);
                break;
            } else if (choice.chars().allMatch(Character::isDigit)) {
                int selectedPage = Integer.parseInt(choice);


                if (selectedPage > 0 && selectedPage <= amountOfPages) {
                    page = selectedPage;
                    posts = PostService.getFeed(page);
                } else {
                    System.out.println("Invalid page number, please try again.");
                }
            } else {
                System.out.println("Invalid Input, please repeat");
            }
        }
        showProfile(user);
    }

    private static void like(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to like:");
        int postid = scanner.nextInt();
        if (Database.isPostAuthor(postid, user.getId())) {
            System.out.println("You can't like your own post!");
            showFeed(user);
        }
        if (PostService.likePost(postid, user.getId())) {
            System.out.println("You successfully liked a post!");
        } else {
            System.out.println("Something went wrong(maybe you already liked it).");
        }
        mainMenu(user);
    }
    private static void dislike(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to dislike:");
        int postid = scanner.nextInt();
        if (Database.isPostAuthor(postid, user.getId())) {
            System.out.println("You can't dislike your own post!");
            showFeed(user);
        }
        if (PostService.dislikePost(postid, user.getId())) {
            System.out.println("You successfully disliked a post!");
        } else {
            System.out.println("Something went wrong(maybe you already disliked it).");
        }
        mainMenu(user);
    }


    private static void showProfile(User user) {// в общем нужно сделать чтобы можно было видеть кого угодно профль вот таким образом
        //показывает имя и всякое(может что то вроде about me)
        //меню:
        //1. Show my posts(потом в нём можно будет редактировать посты)
        //2. Show my likes
        //3. ???
        //4. quit


    }
}