import Utilities.Code;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Jaime Serrano Acevedo
 * @since: 2026.03.23
 * Abstract: this is Reader class which contains methods to access the books which are in list
 * Books can be added, deleted, counted from the list, which such gets assigned to a person
 * based on the following data: name, phone number, library card.
 * */

public class Reader {
    public final static int CARD_NUMBER = 0;
    public final static int NAME_= 1;
    public final static int PHONE_ = 2;
    public final static int BOOK_COUNT_ = 3;
    public final static int BOOK_START_ = 4;

    private int cardNumber;
    private String name;
    private String phone;
    private ArrayList<Book> books = new ArrayList<>();

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
    }
    public Code addBook(Book my_book){
        if(hasBook(my_book)){
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }
        getBooks().add(my_book);
        return Code.SUCCESS;
    }

    public Code removeBook(Book my_book) {
        if(!hasBook(my_book)){
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }
        books.remove(my_book);
        if(hasBook(my_book)){
            return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
        }
        return Code.SUCCESS;
    }

    public boolean hasBook(Book my_book){
        return books.contains(my_book);
    }

    public int getBookCount() {
        return books.size();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return cardNumber == reader.cardNumber && Objects.equals(name, reader.name) && Objects.equals(phone, reader.phone);
    }

    @Override
    public String toString(){
        return getName() + "(#" + getCardNumber() + ") has checked out" + getBooks();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, name, phone);
    }
}
