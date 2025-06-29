import models.Book;
import models.User;
import services.Library;
import services.DatabaseManager;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a new Library instance
        Library library = new Library();

        //Loading from csv files.
        //library.loadUsers()
        //library.loadBooks()
        

        DatabaseManager dbm = DatabaseManager.getInstance();
        dbm.createTables();

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
                    String title = inputCheck(scanner.nextLine());
                    
                    System.out.print("Author: ");
                    String author = inputCheck(scanner.nextLine());

                    System.out.print("ISBN: ");
                    String isbn = inputCheck(scanner.nextLine());

                    Book userBook = new Book(title, author, isbn);
                    dbm.addBook(userBook);
                    break;

                case "2":
                    System.out.print("Name: ");
                    String userName = inputCheck(scanner.nextLine());

                    System.out.print("ID: ");
                    String userId = inputCheck(scanner.nextLine());

                    System.out.print("Type: ");
                    String userType = inputCheck(scanner.nextLine());

                    User newUser = new User(userName, userId, userType);
                    dbm.addUser(newUser);

                    break;

                case "3":
                    List<Book> bookList = library.getAllBooks();

                    for (int i = 0; i < bookList.size(); i++) {
                        System.out.println((i + 1) + ". " + bookList.get(i));
                    }

                    break;

                case "4":
                    List<User> userList = library.getAllUsers();
                    List<Book> borrowList = library.getAllBooks();

                    for (int i = 0; i < userList.size(); i++) {
                        System.out.println((i + 1) + ". " + userList.get(i));
                        for (int j = 0; j < borrowList.size(); j++) {
                            if (borrowList.get(j).getBorrowedBy() != null) {
                                if (borrowList.get(j).getBorrowedBy().getName().equals(userList.get(i).getName())) {
                                    System.out.println("* " + borrowList.get(j).getTitle() + " by " + borrowList.get(j).getAuthor() + " (ISBN: " + borrowList.get(j).getIsbn() + ")"); 
                                }
                            }
                        }
                    }

                    break;
                
                case "5":
                    System.out.print("Title: ");
                    String searchTitle = inputCheck(scanner.nextLine());

                    List<Book> titleResults = library.searchBooksByTitle(searchTitle);
                    for (Book book : titleResults) {
                        System.out.print(book);
                    }

                    break;

                case "6":
                    System.out.print("User's name: ");
                    String searchName = inputCheck(scanner.nextLine());

                    List<User> userNameResults = library.searchUsersByName(searchName);
                    for (User user : userNameResults) {
                        System.out.print(user);
                    }

                    break;

                case "7":
                    System.out.println("Must provide ISBN and user id to borrow book.");
                    System.out.print("ISBN: ");
                    String borrowIsbn = inputCheck(scanner.nextLine());

                    System.out.print("User id: ");
                    String borrowUserId = inputCheck(scanner.nextLine());

                    dbm.borrowBook(borrowUserId, borrowIsbn);


                    break;

                case "8":
                    System.out.println("Please provide the ISBN and your ID to return book.");
                    System.out.print("ISBN: ");
                    String returnIsbn = inputCheck(scanner.nextLine());

                    System.out.print("User id: ");
                    String returnId = inputCheck(scanner.nextLine());

                    library.returnBook(returnIsbn, returnId);
                    break;

                case "9":
                    library.saveAllUsers();
                    library.saveAllBooks();
                    System.out.println("Have a nice day!");
                    exitLoop = true;
                    break;

            }

            if (!exitLoop) {
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }

        }
        scanner.close();
                
   }

    public static String inputCheck(String input) {
        Scanner scanner = new Scanner(System.in);
        input = input.trim();
        while (input == "") {
            System.out.println("Input can not be blank.");
            System.out.print("Enter: ");
            input = scanner.nextLine().trim();
            
        }
        
        return input;
    }
}
