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
            System.out.println("1. Add Book\n" +
                               "2. Add User\n" +
                               "3. List All Books\n" +
                               "4. List All Users\n" +
                               "5. Search Books by Title\n" +
                               "6. Search Users by Name\n" +
                               "7. Borrow Book\n" +
                               "8. Return Book\n" +
                               "9. Exit");

            String ans = scanner.nextLine();

            switch (ans) {
                case "1":
                    System.out.println("Title: ");
                    String title = scanner.nextLine();
                    
                    System.out.println("Author: ");
                    String author = scanner.nextLine();

                    System.out.println("ISBN: ");
                    String isbn = scanner.nextLine();

                    Book userBook = new Book(title, author, isbn);
                    library.addBook(userBook);
                    
                    System.out.println("Book successfully added!");
                    break;

                case "2":
                    System.out.println("Name: ");
                    String userName = scanner.nextLine();

                    System.out.println("ID: ");
                    String userId = scanner.nextLine();

                    System.out.println("Type: ");
                    String userType = scanner.nextLine();

                    User newUser = new User(userName, userId, userType);
                    library.addUser(newUser);

                    break;

                case "3":
                    System.out.println(library.getAllBooks());
                    break;

                case "4":
                System.out.println(library.getAllUsers());
                    break;
                
                case "5":
                    System.out.println("Title: ");
                    String searchTitle = scanner.nextLine();

                    List<Book> titleResults = library.searchBooksByTitle(searchTitle);
                    for (Book book : titleResults) {
                        System.out.println(book);
                    }

                    break;

                case "6":
                    System.out.println("User's name: ");
                    String searchName = scanner.nextLine();

                    List<User> userNameResults = library.searchUsersByName(searchName);
                    for (User user : userNameResults) {
                        System.out.println(user);
                    }

                    break;

                case "7":
                    System.out.println("Must provide ISBN and user id to borrow book.");
                    System.out.println("ISBN: ");
                    String borrowIsbn = scanner.nextLine();

                    System.out.println("user id: ");
                    String borrowUserId = scanner.nextLine();

                    library.borrowBook(borrowIsbn, borrowUserId);

                    System.out.println("Book has been borrowed!");
                    break;

                case "8":
                    System.out.println("Please provide the ISBN to return book.");
                    System.out.println("ISBN: ");
                    String returnIsbn = scanner.nextLine();

                    library.returnBook(returnIsbn);
                    System.out.println("Book has been returned!");
                    break;

                case "9":
                    exitLoop = true;
                    break;
            }

        }
                
   }

}
