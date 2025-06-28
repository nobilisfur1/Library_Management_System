package services;

import models.Book;
import models.User;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.FileReader;
import java.io.BufferedReader;

public class Library {
    private ArrayList<Book> books;
    private ArrayList<User> users;

    public Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void borrowBook(String isbn, String userId) {
        Book book = findBookByIsbn(isbn);
        User user = findUserByUserId(userId);

        if (book == null && user == null) {
            System.out.println("Book nor user exist.");
            return;
        }

        if (book == null) {
            System.out.println("Book doesn't exist.");
            return;
        }

        if (user == null) {
            System.out.println("User doesn't exist.");
            return;
        }

        if (book.isAvailable() != true) {
            System.out.println("Book is not available.");
            return;
        }

        book.borrowBook(user);
        System.out.println("Book has been borrowed!");
    }

    public void returnBook(String isbn, String id) {
        Book book = findBookByIsbn(isbn);
        User userId = findUserByUserId(id);
        
        if (book == null) {
            System.out.println("Book does not exist.");
            return;
        }
        if (userId == null) {
            System.out.println("Book does not exist.");
            return;
        }
        
        User borrower = book.getBorrowedBy();

        if (!borrower.equals(userId)) {
            System.out.println("User id given did not borrow book.");
            return;
        }

        book.returnBook();
        System.out.println("Book has been returned!");
    }

    public void addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) == null) {
            books.add(book);
            System.out.println("book successfully added!");
            return;
        }

        System.out.println("Book already exists.");


    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void addUser(User user) {
        String id = user.getUserID();

        if (findUserByUserId(id) == null) {
            users.add(user);
            System.out.println("User added.");
        }
        else {
            System.out.println("User already exists");
        }
    }

    public List<User> getAllUsers() {
        return users;
    }
    
    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)){
                return book;
            }
        }
        return null;
    }

    public User findUserByUserId(String userId) {
        for (User user : users) {
            if (user.getUserID().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public List<Book> searchBooksByTitle(String title) {
        ArrayList<Book> results = new ArrayList<>();

        for(Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    results.add(book);
            }
        }

        return results;

    }

    public List<Book> searchBooksByAuthor(String author) {
        ArrayList<Book> results = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(book);
            }
        }

        return results;
    }

    public ArrayList<User> searchUsersByName(String name) {
        ArrayList<User> results = new ArrayList<>();

        for (User user : users) {
            if (user.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(user);
            }
        }

        return results;
    }

    public String cleanComma(String entry) {
        return entry.replace("-","*-").replace(",", "-");
    }

    // Save books to books.csv
    public void saveAllBooks() {
        try (Writer bookWriter = new FileWriter("data/csv/books.csv")) {
            for (Book book : books) {
                bookWriter.write(cleanComma(book.getTitle()) + "," + cleanComma(book.getAuthor()) + "," + cleanComma(book.getIsbn()));
                if (book.getBorrowedBy() != null) {
                    bookWriter.write("," + cleanComma(book.getBorrowedBy().getUserID()));
                }
                bookWriter.write("\n");
            }
        }
        catch (IOException e) {
            System.out.println("Issue writing books :'( " + e.getMessage());
        }

    }

    // Load books from books.csv
    public void loadBooks() {
        String csvFilePath = "data/csv/books.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] data;
            int bookCount = 0;

            while ((line = br.readLine()) != null) {
                data = line.split(",");
                
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replace("-",",").replace("*,","-");
                }

                if (data.length == 3 || data.length == 4) {
                    String title = data[0].trim();
                    String author = data[1].trim();
                    String isbn = data[2].trim();

                    books.add(new Book(title, author, isbn));

                    if (data.length == 4) {
                        borrowBook(isbn, data[3].trim());
                    }

                    }

                    if (data.length < 3) {
                        System.out.println("Ran into a short line in books.csv. Skipping...");
                    }
                    else if (data.length > 4) {
                        System.out.println("Ran into a long line in books.csv. Skipping...");
                    }

                bookCount++;
            }
            System.out.println(bookCount + " books loaded!");

        }
        catch (IOException e) {
            System.out.println("Issue reading books :'( " + e.getMessage());
        }

    }

    public void saveAllUsers() {
        try (Writer userWriter = new FileWriter("data/csv/users.csv")) {
            for (User user : users) {
                userWriter.write(cleanComma(user.getName()) + "," + cleanComma(user.getUserID()) + "," + cleanComma(user.getType()) + "\n");
            }
        }
        catch (IOException e) {
            System.out.println("Issue writing users :'( " + e.getMessage());
        }
    }

    public void loadUsers() {
        String csvFilePath = "data/csv/users.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            String[] data;
            int userCount = 0;

            while ((line = br.readLine()) != null) {
                data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replace("-",",").replace("*,","-");
                }

                if (data.length == 3) {
                    String name = data[0].trim();
                    String userId = data[1].trim();
                    String type = data[2].trim();

                    users.add(new User(name, userId, type));

                    if (data.length > 3) {
                        System.out.println("Ran into a long line in users.csv. Skipping line...");
                    }
                    if (data.length < 3) {
                        System.out.println("Ran into a short line in user.csv. Skipping line...");
                    }
                }
                userCount++;
            }
            System.out.println(userCount + " users loaded!");
            
        }
        catch (IOException e) {
            System.out.println("Issue reading users :'( " + e.getMessage());
        }
    }
}
