import Utilities.Code;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Library {
    public static final int LENDING_LIMIT = 5;
    private HashMap<Book, Integer> books = new HashMap<>();
    public static int libraryCard;
    private String name;
    private ArrayList<Reader> readers = new ArrayList<>();
    private HashMap<String, Shelf> shelves = new HashMap<>();

    public Code addBook(Book newBook){
        if(books.containsKey(newBook)){
            int k = books.get(newBook) + 1;
            books.put(newBook, k);
            String temp = k + " copies of " + newBook.getTitle() + " in the stacks";
            System.out.println(temp);
        }
        else {
            books.put(newBook, 1);
            String temp = newBook.getTitle() + " added to the stacks.";
            System.out.println(temp);
        }
        if(shelves.containsKey(newBook.getSubject())){
            shelves.get(newBook.getSubject()).addBook(newBook);
            return Code.SUCCESS;
        }

        return Code.SHELF_EXISTS_ERROR;
    }

    @Deprecated
    public Code addBookToShelf(Book book, Shelf shelf){

    }

    public Code addReader(Reader read){
        if(readers.contains(read)){
            String temp = read.getName() + " already has an account!";
            System.out.println(temp);
            return Code.READER_ALREADY_EXISTS_ERROR;
        }
        for(Reader r : readers){
            if(r.getCardNumber() == read.getCardNumber()){
                String temp = r.getName() + " and " + read.getName() + " have the same card number!";
                System.out.println(temp);
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }
        readers.add(read);
        if(readers.contains(read)){
            System.out.println(read.getName() + " added to the library!");
        }
        if(read.getCardNumber() > libraryCard){
            libraryCard = read.getCardNumber();
        }
        return Code.SUCCESS;
    }

    public Code addShelf(Shelf shelf) {
        if(shelves.containsKey(shelf.getSubject())){
            if(shelves.get(shelf.getSubject()).equals(shelf)){
                return Code.SHELF_EXISTS_ERROR;
            }
        }
        shelves.put(shelf.getSubject(), shelf);
        shelf.setShelfNumber(shelf.getShelfNumber() + 1);
        return Code.SUCCESS;
    }

    public Code addShelf(String shelf_str){
        Shelf shelf = new Shelf(1,shelf_str);
        addShelf(shelf);
        return Code.SUCCESS;
    }

    public Code checkOutBook(Reader read, Book book) {
        if(!readers.contains(read)){
            System.out.println(read.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(read.getBookCount() == LENDING_LIMIT){
            System.out.println(read.getName() + " has reached the lending limit, (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!books.containsKey(book)){
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(!shelves.containsKey(book.getSubject())){
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }
        if(shelves.get(book.getSubject()).getBookCount(book) < 1){
            System.out.println("ERROR: no copies of " + book + " remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(read.addBook(book) != Code.SUCCESS){
            System.out.println("Couldn't checkout " + book);
            return read.addBook(book);
        }
        return shelves.get(book.getSubject()).removeBook(book);
    }

    public LocalDate convertDate(String s, Code c){

    }

    public static int convertInt(String s, Code c) {

    }

    private Code errorCode(int i) {
        for(Code code : Code.values()){
            if(code.getCode() == i){
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    public Book getBookByISBN(String isbn){
        for(Map.Entry<Book, Integer> entry : books.entrySet()){
            if(entry.getKey().getISBN().equals(isbn)){
                return entry.getKey();
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public static int getLibraryCardNumber (){
        return libraryCard + 1;
    }

    public String getName() {
        return name;
    }

    public Reader getReaderByCard(int id){
        for(Reader r : readers){
            if(r.getCardNumber() == id){
                return r;
            }
        }
        return null;
    }

    public Shelf getShelf(String shelf_str){
        return shelves.getOrDefault(shelf_str, null);
    }

    public Shelf getShelf(Integer shelf_int) {
        for(Map.Entry<String, Shelf> entry : shelves.entrySet()){
            if(entry.getValue().getShelfNumber() == shelf_int){
                return entry.getValue();
            }
        }
        System.out.println("No shelf number " + shelf_int + " found");
        return null;
    }

    public Code init(String filename) {

    }

    public Code initBooks(int i, Scanner scan){}

    public Code initReader(int i, Scanner scan){}

    public Code initShelves(int i, Scanner scan){}

    public int listBooks() {
        int count = 0;
        for(Map.Entry<Book, Integer> entry : books.entrySet()){
            System.out.println(entry.getValue() + " copies of " + entry.getKey());
            count += entry.getValue();
        }
        return count;
    }

    public int listReaders() {
        for(Reader r : readers){
            System.out.println(r);
        }
        return readers.size();
    }

    public int listReaders(boolean bool){
        int count = 0;
        if(bool){
            count = 1;
            for (Reader r : readers){
                String line1 = r.getName() + "(#" + count + ") has the following books:";
                String line2 = r.getBooks().toString();
                System.out.println(line1 + "\n" + line2);
                count += 1;
            }
        }
        else{
            for (Reader r : readers){
                System.out.println(r);
            }
        }
        return readers.size();
    }

    public int listShelves(boolean bool) {
        if(bool){
            for(Map.Entry<String,Shelf> entry : shelves.entrySet()){
                entry.getValue().listBooks();
            }
        }
        else {
            for(Map.Entry<String, Shelf> entry : shelves.entrySet()){
                System.out.println(entry.getValue());
            }
        }
        return shelves.size();
    }

    public int listShelves() {
        return listShelves(false);
    }

    public Code removeReader(Reader read) {
        for(Reader r : readers){
            if(r.equals(read)){
                if(r.getBookCount() > 0){
                    System.out.println(r.getName() + " must return all books!");
                    return Code.READER_STILL_HAS_BOOKS_ERROR;
                }
                else {
                    readers.remove(read);
                    return Code.SUCCESS;
                }
            }
        }
        System.out.println(read + " is not part of this library");
        return Code.READER_NOT_IN_LIBRARY_ERROR;
    }

    public Code returnBook(Reader read, Book book) {
        if(!read.getBooks().contains(book)){
            System.out.println(read.getName() + " doesn't have " + book.getTitle() + " checked out");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        System.out.println(read.getName() + " is returning " + book);
        Code c = read.removeBook(book);
        if(c == Code.SUCCESS){
            return returnBook(book);
        }
        System.out.println("Could not return " + book);
        return c;
    }

    public Code returnBook(Book book) {
        if(!shelves.containsKey(book.getSubject())){
            System.out.println("No shelf for " + book);
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.get(book.getSubject()).addBook(book);
        return Code.SUCCESS;
    }
}
