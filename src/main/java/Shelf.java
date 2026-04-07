import Utilities.Code;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jaime Serrano Acevedo
 * @since: 2026.03.28
 * Abstract: this is the Shelf class. This class stores books from the Book class
 * in a hashmap which acts as the shelf and the class controls the rest of the access
 * methods from the shell. Such as the book count, display the list of books, adding and
 * removing books from the shelf.
 * */

public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;
    private HashMap<Book, Integer> books = new HashMap<>();
    private int shelfNumber;
    private String subject;

    @Deprecated
    public Shelf(){}

    public Shelf(int shelfNumber, String subject){
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    public Code addBook(Book my_book){
        if(books.containsKey(my_book)){
            Integer k = books.get(my_book) + 1;
            books.put(my_book,k);
            System.out.println(my_book + "added to shelf " + this);
            return Code.SUCCESS;
        }
        if(my_book.getSubject().equals(getSubject())){
            books.put(my_book,1);
            System.out.println(my_book + " added to shelf " + this);
            return  Code.SUCCESS;
        }
        return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }

    public Integer getBookCount(Book book){
        if(getBooks().containsKey(book)){
            return getBooks().get(book);
        }
        return -1;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return shelfNumber == shelf.shelfNumber && Objects.equals(subject, shelf.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shelfNumber, subject);
    }

    public String listBooks(){
        String line1 = getBooks().size() + " books on shelf: " + getShelfNumber() + " : " + getSubject();
        StringBuilder line2 = new StringBuilder();
        line2.append(line1);
        line2.append("\n");
        for(Map.Entry<Book, Integer> entry : getBooks().entrySet()){
            String temp = entry.getKey() + " " + entry.getValue();
            line2.append(temp);
            line2.append("\n");
        }
        return line2.toString();
    }

    public Code removeBook(Book book){
        if(!getBooks().containsKey(book)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(getBooks().containsKey(book) && getBookCount(book) <= 0){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(getBooks().containsKey(book) && getBookCount(book) >= 1){
            int k = getBookCount(book) - 1;
            books.put(book,k);
        }
        return Code.SUCCESS;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString(){
        return getShelfNumber() + " : " + getSubject();
    }
}
