import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Library {
    public static final int LENDING_LIMIT;
    private HashMap<Book, Integer> books = new HashMap<>();
    public static int libraryCard;
    private String name;
    private ArrayList<Reader> readers = new ArrayList<>();
    private HashMap<String, Shelf> shelves = new HashMap<>();

    public Code addBook(Book book){}

    public Code addBookToShelf(Book book, Shelf shelf){}

    public Code addReader(Reader read){}

    public Code addShelf(Shelf shelf) {}

    public Code addShelf(String shelf_str){}

    public Code checkOutBook(Reader read, Book book) {}

    public LocalDate convertDate(String s, Code c){}

    public int convertInt(String s, Code c) {}

    private Code errorCode(int i) {}

    public Book BookByISBN(String name){}

    public int getLibraryCardNumber (){
        return libraryCard;
    }

    public String getName() {
        return name;
    }

    public Reader getReaderByCard(int id){}

    public Shelf getShelf(String shelf_str){}

    public Shelf getShelf(Integer shelf_int) {}

    public Code init(String s) {}

    public Code initBooks(int i, Scanner scan){}

    public Code initReader(int i, Scanner scan){}

    public Code initShelves(int i, Scanner scan){}

    public int listBooks() {}

    public int listReaders() {}

    public int listReaders(boolean bool){}

    public int listShelves(boolean bool) {}

    public int listShelves() {}

    public Code removeReader(Reader read) {}

    public Code returnBook(Reader read, Book book) {}

    public Code returnBook(Book book) {}
}
