import models.Book;
import models.User;
import services.Library;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a new Library instance
        Library library = new Library();

        // Create some books
        Book book1 = new Book("The Hobbit", "J.R.R Tolkien", "1234567890");
        Book book2 = new Book("Farenheit 451", "Ray Bradbury", "0987654321");

        // Create some users
        User user1 = new User("Kevin Baxter", "11332", "Student");
        User user2 = new User("Alayna Vera-Gonzalez", "21134", "Librarian");

        // Add books to the Library
        library.addBook(book1);
        library.addBook(book2);

        // Add users to the Library
        library.addUser(user1);
        library.addUser(user2);

        // List books
        List<Book> allBooks =library.getAllBooks();
        
        System.out.println("Library Book List:");
        int index = 1;
        for (Book book : allBooks) {
            System.out.println(index + ". " + book);
            index++;
        }

        //List all Users
        List<User> allUsers = library.getAllUsers();

        System.out.println("Library User List:");
        index = 1;
        for (User user : allUsers) {
            System.out.println(index + ". " + user);
            index++;
        }

        System.out.println("Available? " + book1.isAvailable());
        book1.borrowBook(user1);
        System.out.println("Available? " + book1.isAvailable());
        System.out.println(book1);
        book1.returnBook();
        System.out.println("Available? " + book1.isAvailable());
        System.out.println(book1);
    }

}
