import java.util.Objects;

public class Reader {
    public final static int CARD_NUMBER_;
    public final static int NAME_;
    public final static int PHONE_;
    public final static int BOOK_COUNT_;
    public final static int BOOK_START_;

    private int cardNumber;
    private String name;
    private String phone;
    private ArrayList<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
    }
    public Code addBook(Book my_book){

    }

    public Code removeBook(Book my_book) {}

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
