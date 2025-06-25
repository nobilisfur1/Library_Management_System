import models.Book;
import models.User;
import services.Library;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a new Library instance
        Library library = new Library();

        // Init scanner
        Scanner scanner = new Scanner(System.in);
        
        boolean exitLoop = false;

        while(!exitLoop) {
            System.out.println("---------- Menu ----------");
            System.out.println("1. Add Book\n" +
                               "2. Add User\n" +
                               "3. List All Books\n" +
                               "4. List All Users\n" +
                               "5. Search Books by Title\n" +
                               "6. Search Users by Name\n" +
                               "7. Borrow Book\n" +
                               "8. Return Book\n" +
                               "9. Exit");
            System.out.println("--------------------------");
            System.out.print("Input: ");

            String ans = scanner.nextLine().trim();

            switch (ans) {
                case "1":
                    System.out.print("Title: ");
                    String title = scanner.nextLine().trim();
                    
                    System.out.print("Author: ");
                    String author = scanner.nextLine().trim();

                    System.out.print("ISBN: ");
                    String isbn = scanner.nextLine().trim();

                    Book userBook = new Book(title, author, isbn);
                    library.addBook(userBook);
                    break;

                case "2":
                    System.out.print("Name: ");
                    String userName = scanner.nextLine().trim();

                    System.out.print("ID: ");
                    String userId = scanner.nextLine().trim();

                    System.out.print("Type: ");
                    String userType = scanner.nextLine().trim();

                    User newUser = new User(userName, userId, userType);
                    library.addUser(newUser);

                    break;

                case "3":
                    List<Book> bookList = library.getAllBooks();

                    for (int i = 0; i < bookList.size(); i++) {
                        System.out.println((i + 1) + ". " + bookList.get(i));
                    }

                    break;

                case "4":
                    List<User> userList = library.getAllUsers();

                    for (int i = 0; i < userList.size(); i++) {
                        System.out.println((i + 1) + ". " + userList.get(i));
                    }

                    break;
                
                case "5":
                    System.out.print("Title: ");
                    String searchTitle = scanner.nextLine().trim();

                    List<Book> titleResults = library.searchBooksByTitle(searchTitle);
                    for (Book book : titleResults) {
                        System.out.print(book);
                    }

                    break;

                case "6":
                    System.out.print("User's name: ");
                    String searchName = scanner.nextLine().trim();

                    List<User> userNameResults = library.searchUsersByName(searchName);
                    for (User user : userNameResults) {
                        System.out.print(user);
                    }

                    break;

                case "7":
                    System.out.println("Must provide ISBN and user id to borrow book.");
                    System.out.print("ISBN: ");
                    String borrowIsbn = scanner.nextLine().trim();

                    System.out.print("User id: ");
                    String borrowUserId = scanner.nextLine().trim();

                    library.borrowBook(borrowIsbn, borrowUserId);
                    break;

                case "8":
                    System.out.println("Please provide the ISBN and your ID to return book.");
                    System.out.print("ISBN: ");
                    String returnIsbn = scanner.nextLine().trim();

                    System.out.print("User id: ");
                    String returnId = scanner.nextLine().trim();

                    library.returnBook(returnIsbn, returnId);
                    break;

                case "9":
                    exitLoop = true;
                    break;

            }

            if (!exitLoop) {
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }

        }
                
   }

    public static String inputCheck(String input) {
        Scanner scanner = new Scanner(System.in);
        input = input.trim()
        while (input == "") {
            System.out.println("Input can not be blank.");
            System.out.print("Enter: ");
            input = scanner.nextLine().trim();
            
        }
        
        return input;
    }
}
