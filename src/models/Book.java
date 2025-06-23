package models;
import models.User;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private User borrowedBy;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
        this.borrowedBy = null;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() {
        if(borrowedBy == null) {
            return true;
        }
        else {
            return false;
        }
    }
    public void borrowBook(User user) { borrowedBy = user; }
    
    public void returnBook() { borrowedBy = null; }

   @Override
   public String toString() {
        return title + " by " + author + " (ISBN: " + isbn + ") - " +
            (isAvailable ? "Available" : "Borrowed") + " borrowed by: " + borrowedBy;
   }
}
