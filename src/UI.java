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
            case "3" -> {
            }

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
        User user = new User(login, UserService.hashPassword(password), false, null, null);
        if (!UserService.registerUser(user)) {
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
        User user = new User(login, password, false, null, null);

        if (UserService.loginUser(user)) {
            System.out.println("Successful logIn!");
            mainMenu(user);
        } else {
            System.out.println("Login or password not correct");
            start();
        }


    }

    private static void mainMenu(User user) throws SQLException {
        user = UserService.getUserData(user.getId());
        System.out.println("""

                1. Show shits(posts)
                2. Take a shit(post)
                3. Your profile
                4. Log out
                5. Exit""");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                showFeed(user, 1);
                break;
            case "2":
                post(user);
                break;
            case "3":
                showYourProfile(user);
                break;
            case "4":
                start();
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
//
//        while (true) {
//            System.out.println("\n");
//
//            for (Post post : posts) {
//                post.show();
//            }
//            System.out.println("\nPage " + page + " out of " + amountOfPages);
//            System.out.println("\"l\" - like a post | " +
//                    "\"d\" - dislike a post | " +
//                    "\"(number)\" - go to page (number) | " +
//                    "\"q\" - quit to Main Menu");
//
//            String choice = scanner.nextLine().trim();
//
//            if (Objects.equals(choice, "l")) {
//                like(user);
//            } else if (Objects.equals(choice, "d")) {
//                dislike(user);
//            } else if (Objects.equals(choice, "q")) {
//                mainMenu(user);
//                break;
//            } else if (choice.chars().allMatch(Character::isDigit)) {
//                int selectedPage = Integer.parseInt(choice);
//
//
//                if (selectedPage > 0 && selectedPage <= amountOfPages) {
//                    page = selectedPage;
//                    posts = PostService.getFeed(page);
//                } else {
//                    System.out.println("Invalid page number, please try again.");
//                }
//            } else {
//                System.out.println("Invalid Input, please repeat");
//            }
//        }
//        mainMenu(user);
//    }

    private static void showFeed(User user, int page) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int postsPerPage = 5;
        int totalPosts = Database.getAmountOfPosts();
        int totalPages = (totalPosts + postsPerPage - 1) / postsPerPage; // Adjust calculation for integer division
        int currentPage = page;

        while (true) {
            List<Post> posts = PostService.getFeed(currentPage);


            for (Post post : posts) {
                post.show();
            }

            System.out.println("\nPage " + currentPage + " out of " + totalPages);
            System.out.println("\"(number)\" - Go to page (number)");
            System.out.printf("\"l\" - Like a post | ");
            System.out.printf("\"d\" - Dislike a post | ");
            System.out.printf("\"p (id)\" - See the author's profile by ID | ");
            System.out.printf("\"q\" - Quit to Main Menu\n");

            String choice = scanner.nextLine().trim();

            if (choice.equalsIgnoreCase("l")) {
                like(user, page);
            } else if (choice.equalsIgnoreCase("d")) {
                dislike(user, page);
            } else if (choice.equalsIgnoreCase("q")) {
                mainMenu(user);
                break;
            } else if (choice.startsWith("p ")) {
                try {
                    int authorId = Integer.parseInt(choice.substring(2).trim());
                    showProfile(authorId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID format. Please try again.");
                }
            } else if (choice.chars().allMatch(Character::isDigit)) {
                int selectedPage = Integer.parseInt(choice);
                if (selectedPage > 0 && selectedPage <= totalPages) {
                    currentPage = selectedPage;
                } else {
                    System.out.println("\nInvalid page number. Please try again.\n");
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
        mainMenu(user);
    }

    private static void myPosts(User user) throws SQLException {
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
                    System.out.println("C'mon, it's twitter(shitter). The text should be <= 140. " +
                            "The length of yours was " + content.length());
                    showYourProfile(user);
                }
                if (PostService.editPost(content, Integer.parseInt(id))) {
                    System.out.println("You successfully edited your shit!");
                } else {
                    System.out.println("Something went wrong! Please repeat");
                    showYourProfile(user);
                }
                System.out.println("\n");

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
        showYourProfile(user);
    }



    private static void userPosts(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int amountOfPages = (Database.getAmountOfUserPosts(user.getId()) / 5) + 1;
        int page = 1;
        List<Post> posts = PostService.getUserPosts(user.getId(), page);
        while (true) {
            for (Post post : posts) {
                post.show();
            }
            System.out.println("\nPage " + page + " out of " + amountOfPages);
            System.out.println("\"(number)\" - go to page (number) | " +
                    "\"q\" - quit to Main Menu");
            String choice = scanner.nextLine().trim();
            if (Objects.equals(choice, "q")) {
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
        showYourProfile(user);
    }

    private static void post(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        if (PostService.hasExceededPostLimit(user)) {
            System.out.println("You posted too many times in the last hour, please wait");
        }
        System.out.println("Enter the text(<140 symbols):");
        String content = scanner.nextLine();
        if (content.length() > 140) {
            System.out.println("C'mon, it's twitter(shitter). The text should be < 140. " +
                    "The length of yours was " + content.length());
            mainMenu(user);
        }

        if (PostService.createPost(user.getId(), user.getLogin(), content)) {
            System.out.println("You successfully shitted!");
        } else {
            System.out.println("Something went wrong! Please repeat");
            post(user);
        }
        mainMenu(user);
    }

    private static void like(User user, int page) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to like:");
        int postid = scanner.nextInt();
        if (Database.isPostAuthor(postid, user.getId())) {
            System.out.println("You can't like your own post!");
            showFeed(user, page);
        }
        if (PostService.likePost(postid, user.getId())) {
            System.out.println("You successfully liked a post!");
        } else {
            System.out.println("Something went wrong(maybe you already liked it).");
            showFeed(user, page);
        }
        showFeed(user, page);
    }

    private static void dislike(User user, int page) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the id of the post that you'd like to dislike:");
        int postid = scanner.nextInt();
        if (Database.isPostAuthor(postid, user.getId())) {
            System.out.println("You can't dislike your own post!");
            showFeed(user, page);
        }
        if (PostService.dislikePost(postid, user.getId())) {
            System.out.println("You successfully disliked a post!");
        } else {
            System.out.println("Something went wrong(maybe you already disliked it).");
        }
        showFeed(user, page);
    }

    private static void showYourProfile(User user) throws SQLException {
        // в общем нужно сделать чтобы можно было видеть кого угодно профль вот таким образом(cделано ихихихиих)
//        user = UserService.getUserData(user);
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n==================================\n" +
                "           My Profile             \n" +
                "==================================");
        System.out.printf(" ID: %d | Login: %s%n", user.getId(), user.getLogin());
        System.out.printf(" %-12s: %s%n", "About me", user.getAboutMe());
        System.out.printf(" %-12s: %s | %s%n", "Joined",
                Time.timeFormatter(user.getCreatedAt()), Time.timeAgo(user.getCreatedAt()));
        //System.out.printf(" %-12s: %d%n", "Likes", numberOfLikes); TODO: видеть сколько лайков в общем у его постов
        System.out.println("----------------------------------");
        System.out.println("\"1\" - My posts | " +
                "\"2\" - My likes | " +
                "\"3\" - Change 'About Me' | " +
                "\"q\" - quit to Main Menu | ");
        String choice = scanner.nextLine();
        if (Objects.equals(choice, "1")) {
            myPosts(user);
        } else if (Objects.equals(choice, "2")) {
            getUserLikes(user);
        } else if (Objects.equals(choice, "3")) {
            changeAboutMe(user);
        } else if (Objects.equals(choice, "q")) {
            mainMenu(user);
        }
        else {
            System.out.println("Invalid Input, please repeat");
        }
    }

    private static void showProfile(int id) throws SQLException {
        User user = UserService.getUserData(id);
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n==================================\n" +
                "           Profile                \n" +
                "==================================");
        System.out.printf(" ID: %d | Login: %s%n", user.getId(), user.getLogin());
        System.out.printf(" %-12s: %s%n", "About me", user.getAboutMe());
        System.out.printf(" %-12s: %s | %s%n", "Joined",
                Time.timeFormatter(user.getCreatedAt()), Time.timeAgo(user.getCreatedAt()));
        //System.out.printf(" %-12s: %d%n", "Likes", numberOfLikes); TODO: видеть сколько лайков в общем у его постов
        System.out.println("----------------------------------");
        System.out.println("\"1\" - His posts | " +
                "\"2\" - His likes | " +
                "\"q\" - quit to Main Menu | ");
        String choice = scanner.nextLine();
        if (Objects.equals(choice, "1")) {
            userPosts(user);
        } else if (Objects.equals(choice, "2")) {
            getUserLikes(user);
        } else if (Objects.equals(choice, "q")) {
            mainMenu(user);
        }
        else {
            System.out.println("Invalid Input, please repeat");
        }
    }

    private static void changeAboutMe(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your About Me is:" + user.getAboutMe());
        System.out.println("Type the new About Me(<140):");
        String text = scanner.nextLine();
        if (text.length() > 140) {
            System.out.println("The length of about me should not exceed 140. " +
                    "The length of yours was " + text.length());
        }

        if (UserService.changeAboutMe(user, text)) {
            System.out.println("You succesfully changed your About Me!");
            mainMenu(user);
        } else {
            System.out.println("Something went wrong! Please repeat");
        }
    }



    private static void getUserLikes(User user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int amountOfPages = (Database.getAmountOfUserPosts(user.getId()) / 5) + 1;
        int page = 1;
        List<LikedPost> posts = UserService.getUserLikes(user.getId(), page);

        while (true) {

            for (Post post : posts) {
                post.show();
            }
            System.out.println("\nPage " + page + " out of " + amountOfPages);
            System.out.println("\"(number)\" - go to page (number) | " +
                    "\"q\" - quit to Main Menu");

            String choice = scanner.nextLine().trim();

            if (Objects.equals(choice, "q")) {
                mainMenu(user);
                break;
            } else if (choice.chars().allMatch(Character::isDigit)) {
                int selectedPage = Integer.parseInt(choice);


                if (selectedPage > 0 && selectedPage <= amountOfPages) {
                    page = selectedPage;
                    posts = UserService.getUserLikes(user.getId(), page);
                } else {
                    System.out.println("Invalid page number, please try again.");
                }
            } else {
                System.out.println("Invalid Input, please repeat");
            }
        }
        showYourProfile(user);
    }





}