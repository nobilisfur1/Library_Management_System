package services;

import models.Book;
import models.User;
import java.util.ArrayList;
import java.util.List;

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

        book.borrowBook(user);
    }

    public void returnBook(String isbn) {
        Book book = findBookByIsbn(isbn);

        book.returnBook();
    }

    public void addBook(Book book) {
        books.add(book);
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
            if (book.getIsbn() == isbn){
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

    public Book searchBooksByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        return null;
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

    public User searchUsersById(String id) {
        for (User user : users) {
            if (user.getUserID().equals(id)) {
                return user;
            }
        }

        return null;
    }
}
