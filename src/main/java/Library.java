/**
 * @author Jaime Serrano Acevedo
 * @since: 2026.04.07
 * Abstract: this is the Library class. This is primary class of the project. Is the one
 * that handles and really uses the other methods. As in, it takes books, it put the books
 * into shelves and allows readers to check out and return books. It has the init methods
 * which are to fulfill library based on information found in the csv files passed into it.
 * It offers a general book database (allowing to find books regardless of the shelf) and
 * lending limit for users borrowing books.
 * */
import Utilities.Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Library {
    public static final int LENDING_LIMIT = 5;
    private HashMap<Book, Integer> books = new HashMap<>();
    public static int libraryCard;
    private String name;
    private ArrayList<Reader> readers = new ArrayList<>();
    private HashMap<String, Shelf> shelves = new HashMap<>();

    public Library(String name){
        this.name = name;
    }

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
        if(!shelf.getSubject().equals(book.getSubject())){
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }

        Code sR = shelf.addBook(book);
        if(sR == Code.SUCCESS){
            System.out.println(book + " added to shelf");
            return Code.SUCCESS;
        }
        else{
            System.out.println("Could not add " + book + " to shelf");
        }
        return sR;
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
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        }

        shelves.put(shelf.getSubject(), shelf);

        for(Map.Entry<Book, Integer> entry : books.entrySet()){
            Book book = entry.getKey();
            int count = entry.getValue();
            if(book.getSubject().equals(shelf.getSubject())){
                for (int i = 0; i < count; i++){
                    shelf.addBook(book);
                }
            }
        }
        return Code.SUCCESS;
    }

    public Code addShelf(String shelf_str){
        if(shelves.containsKey(shelf_str)){
            System.out.println("ERROR: Shelf already exists " + getShelf(shelf_str));
            return Code.SHELF_EXISTS_ERROR;
        }
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
        Code c = read.addBook(book);
        if (c != Code.SUCCESS){
            System.out.println("Couldn't checkout " + book);
            return c;
        }
        c = shelves.get(book.getSubject()).removeBook(book);
        if(c == Code.SUCCESS) {
            System.out.println(book + " checked out successfully");
        }
        return c;
    }

    public static LocalDate convertDate(String date, Code c){
        LocalDate defaultDate = LocalDate.of(1970, 1, 1);
        if(date.length() != 10){
            return defaultDate;
        }
        if(date.charAt(4) != '-' || date.charAt(7) != '-'){
            return defaultDate;
        }
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        return LocalDate.of(parseInt(year), parseInt(month), parseInt(day));
    }

    public static int convertInt(String recordCountString, Code c) {
        try {
            return Integer.parseInt(recordCountString);
        }
        catch (NumberFormatException e){
            System.out.println("Value which caused the error:" + recordCountString);
            System.out.println("Error message: " + c.getMessage());
            if (c == Code.BOOK_COUNT_ERROR){
                System.out.println("Error: Could not read number of books");
            }
            else if( c == Code.PAGE_COUNT_ERROR){
                System.out.println("Error: could not parse page count");
            }
            else if(c == Code.DATE_CONVERSION_ERROR){
                System.out.println("Error: Could not parse date component");
            }
            else {
                System.out.println("Error: Unknown conversion error");
            }
            return c.getCode();
        }
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
        if (!shelves.containsKey(shelf_str)){
            System.out.println("No shelf for " + shelf_str + " books");
            return null;
        }
        return shelves.get(shelf_str);
    }

    public Shelf getShelf(Integer shelf_int) {
        for(Map.Entry<String, Shelf> entry : shelves.entrySet()){
            if(entry.getValue().getShelfNumber() == shelf_int.intValue()){
                return entry.getValue();
            }
        }
        System.out.println("No shelf number " + shelf_int + " found");
        return null;
    }

    public Code init(String filename) {
       File file = new File(filename);
        if (!file.exists()){
            return Code.FILE_NOT_FOUND_ERROR;
        }
        try(Scanner scan = new Scanner(file)){
            int bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
            if(bookCount < 0){
                return errorCode(bookCount);
            }
            Code ans = initBooks(bookCount, scan);

            if(ans != Code.SUCCESS){
                return ans;
            }
            listBooks();

            int shelfCount = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
            if (shelfCount < 0){
                return errorCode(shelfCount);
            }

            ans = initShelves(shelfCount, scan);
            if(ans != Code.SUCCESS){
                return ans;
            }
            listShelves();

            int readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
            if (readerCount < 0){
                return errorCode(readerCount);
            }

            ans = initReader(readerCount, scan);
            if (ans != Code.SUCCESS){
                return ans;
            }
            listReaders();

            return Code.SUCCESS;

        } catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }
    }

    public Code initBooks(int bookCount, Scanner scan){
        if(bookCount < 1){
            return Code.LIBRARY_ERROR;
        }

        for(int i = 0; i < bookCount; i++){
            String line = scan.nextLine();
            String[] values = line.split(",");
            if(values.length < Book.DUE_DATE_){
                return Code.BOOK_RECORD_COUNT_ERROR;
            }

            int pages = convertInt(values[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            if (pages <= 0){
                return Code.BOOK_COUNT_ERROR;
            }

            LocalDate tempDate = convertDate(values[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if(tempDate == null){
                return Code.DATE_CONVERSION_ERROR;
            }

            Book book = new Book(values[Book.ISBN_], values[Book.TITLE_], values[Book.SUBJECT_], pages, values[Book.AUTHOR_], tempDate);
            addBook(book);
        }
        return Code.SUCCESS;
    }

    public Code initReader(int readerCount, Scanner scan){
        if(readerCount <= 0){
            return Code.READER_COUNT_ERROR;
        }

        for(int i = 0; i < readerCount; i++){
            String line = scan.nextLine();
            String[] values = line.split(",");

            int cN = parseInt(values[Reader.CARD_NUMBER]);
            String name = values[Reader.NAME_];
            String phone = values[Reader.PHONE_];

            Reader re = new Reader(cN, name, phone);
            addReader(re);

            int bookC = parseInt(values[Reader.BOOK_COUNT_]);
            int index = Reader.BOOK_START_;

            for(int j = 0; j < bookC; j++){
                String isbn = values[index++];
                LocalDate dueDate = convertDate(values[index++], Code.DATE_CONVERSION_ERROR);

                Book book = getBookByISBN(isbn);
                if(book == null){
                    System.out.println("ERROR");
                    continue;
                }
                checkOutBook(re, book);
            }
        }

        return Code.SUCCESS;
    }

    public Code initShelves(int shelfCount, Scanner scan){
        if(shelfCount < 1){
            return Code.SHELF_COUNT_ERROR;
        }

        for(int i = 0; i < shelfCount; i++){
            String line = scan.nextLine();
            String[] values = line.split(",");
            int shelfN;
            try{
                shelfN = parseInt(values[0]);
            }catch (NumberFormatException e){
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }

            String sub = values[1];

            Shelf temp = new Shelf(shelfN, sub);
            addShelf(temp);
        }
        if(shelves.size() != shelfCount){
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }

        return Code.SUCCESS;
    }

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
        if(!books.containsKey(book)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
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
