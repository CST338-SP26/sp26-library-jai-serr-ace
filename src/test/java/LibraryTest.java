import Utilities.Code;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @version  1.3.1.
 */

class LibraryTest {

    Library csumb = null;

    String library00 = "Library00.csv";
    String library01 = "Library01.csv";
    String badBooks0 = "badBooks0.csv";
    String badBooks1 = "badBooks1.csv";
    String badShelves0 = "badShelves0.csv";
    String badShelves1 = "badShelves1.csv";
    String badReader0 = "badReader0.csv";
    String badReader1 = "badReader1.csv";

    @BeforeEach
    void setUp() {
        csumb = new Library("CSUMB");
    }

    @AfterEach
    void tearDown() {
        csumb = null;
    }

    @Test
    void init_test() {
        //Bad file
        assertEquals(Code.FILE_NOT_FOUND_ERROR, csumb.init("nope.csv"));
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks0));
        assertEquals(Code.BOOK_COUNT_ERROR, csumb.init(badBooks1) );
        assertEquals( Code.SHELF_COUNT_ERROR,csumb.init(badShelves0));
        assertEquals( Code.SHELF_NUMBER_PARSE_ERROR,csumb.init(badShelves1));
    }

    @Test
    void init_goodFile_test() {
        assertEquals(Code.SUCCESS, csumb.init(library00));
        assertEquals(9, csumb.listBooks());
        assertEquals(3, csumb.listShelves());
        assertEquals(4, csumb.listReaders());
    }

    @Test
    void addBook() {
        Shelf scifi = new Shelf(1, "sci-fi");
        csumb.addShelf(scifi);
        Book dune = new Book("34-w-34","Dune","sci-fi",235,"Frank Herber", LocalDate.of(1970,1,1));
        assertEquals(Code.SUCCESS, csumb.addBook(dune));
        assertEquals(1, csumb.listBooks());
        assertEquals(Code.SUCCESS, csumb.addBook(dune));
        assertEquals(2, csumb.listBooks());
    }

    @Test
    void returnBook() {
        csumb.init(library00);
        Reader reader = csumb.getReaderByCard(1);
        Book book = csumb.getBookByISBN("42-w-87");
        assertNotNull(reader);
        assertNotNull(book);
        assertEquals(Code.SUCCESS, csumb.returnBook(reader,book));
    }

    @Test
    void testReturnBook() {
        csumb.init(library00);
        Book book = csumb.getBookByISBN("e1337");
        csumb.listBooks();
        assertEquals(Code.SUCCESS, csumb.returnBook(book));
    }

    @Test
    void listBooks() {
        assertEquals(Code.SUCCESS, csumb.init(library00));
        assertEquals(9, csumb.listBooks());
    }

    @Test
    void checkOutBook() {
        csumb.init(library00);
        Reader reader = csumb.getReaderByCard(1);
        Book book = csumb.getBookByISBN("5297");
        assertNotNull(reader);
        assertNotNull(book);
        assertEquals(Code.SUCCESS, csumb.checkOutBook(reader, book));
    }

    @Test
    void getBookByISBN() {
        csumb.init(library00);
        Book book = csumb.getBookByISBN("42-w-87");
        assertNotNull(book);
        assertEquals("42-w-87", book.getISBN());
        assertNull(csumb.getBookByISBN("not-real"));
    }

    @Test
    void listShelves() {
        csumb.init(library00);
        assertEquals(3, csumb.listShelves());
        assertEquals(3, csumb.listShelves(true));
    }

    @Test
    void addShelf() {
        assertEquals(Code.SUCCESS, csumb.addShelf("history"));
        assertEquals(1, csumb.listShelves());
        assertNotNull(csumb.getShelf("history"));
    }

    @Test
    void testAddShelf() {
        Shelf shelf = new Shelf(7, "education");
        assertEquals(Code.SUCCESS, csumb.addShelf(shelf));
        assertEquals(1, csumb.listShelves());

        assertEquals(Code.SHELF_EXISTS_ERROR, csumb.addShelf(shelf));
    }

    @Test
    void getShelf() {
        csumb.init(library00);

        Shelf shelf = csumb.getShelf(1);
        assertNotNull(shelf);
    }

    @Test
    void testGetShelf() {
        csumb.init(library00);

        Shelf shelf = csumb.getShelf("sci-fi");
        assertNotNull(shelf);

        assertNull(csumb.getShelf("not-a-subject"));
    }

    @Test
    void listReaders() {
        csumb.init(library00);
        assertEquals(4, csumb.listReaders());
    }

    @Test
    void testListReaders() {
        csumb.init(library00);
        assertEquals(4, csumb.listReaders(true));
    }

    @Test
    void getReaderByCard() {
        csumb.init(library00);

        Reader reader = csumb.getReaderByCard(1);
        assertNotNull(reader);
        assertEquals(1, reader.getCardNumber());

        assertNull(csumb.getReaderByCard(999));
    }

    @Test
    void addReader() {
        Reader r1 = new Reader(100, "Bilbo Baggins", "831-000-0000");
        Reader r2 = new Reader(100, "Frodo Baggins", "831-111-1111");

        assertEquals(Code.SUCCESS, csumb.addReader(r1));
        assertEquals(Code.READER_ALREADY_EXISTS_ERROR, csumb.addReader(r1));
        assertEquals(Code.READER_CARD_NUMBER_ERROR, csumb.addReader(r2));
    }

    @Test
    void removeReader() {
        Reader reader = new Reader(200, "Samwise Gamgee", "831-222-2222");
        assertEquals(Code.SUCCESS, csumb.addReader(reader));
        assertEquals(Code.SUCCESS, csumb.removeReader(reader));

        assertEquals(Code.READER_NOT_IN_LIBRARY_ERROR, csumb.removeReader(reader));
    }

    @Test
    void convertInt() {
        assertEquals(9, Library.convertInt("9", Code.BOOK_COUNT_ERROR));
        assertEquals(Code.BOOK_COUNT_ERROR.getCode(), Library.convertInt("abc", Code.BOOK_COUNT_ERROR));
        assertEquals(Code.PAGE_COUNT_ERROR.getCode(), Library.convertInt("nope", Code.PAGE_COUNT_ERROR));
    }

    @Test
    void convertDate() {
        LocalDate d1 = Library.convertDate("2020-10-12", Code.DATE_CONVERSION_ERROR);
        assertEquals(LocalDate.of(2020, 10, 12), d1);

        LocalDate d2 = Library.convertDate("0000", Code.DATE_CONVERSION_ERROR);
        assertEquals(LocalDate.of(1970, 1, 1), d2);

        LocalDate d3 = Library.convertDate("bad-date", Code.DATE_CONVERSION_ERROR);
        assertEquals(LocalDate.of(1970, 1, 1), d3);
    }

    @Test
    void getLibraryCardNumber() {
        int start = Library.getLibraryCardNumber();

        Reader r = new Reader(start, "Aragorn", "831-333-3333");
        csumb.addReader(r);

        assertEquals(start + 1, Library.getLibraryCardNumber());
    }

    @Test
    void initBooks_test() {
        String data =
                "2\n" +
                        "111,Java Basics,education,300,Author One,0000\n" +
                        "222,Dune,sci-fi,500,Author Two,0000\n";

        Scanner scan = new Scanner(data);
        int count = Integer.parseInt(scan.nextLine());

        assertEquals(Code.LIBRARY_ERROR, csumb.initBooks(0, new Scanner("")));
        assertEquals(Code.SUCCESS, csumb.initBooks(count, scan));
        assertEquals(2, csumb.listBooks());
    }

    @Test
    void initShelves_test() {
        String data =
                "2\n" +
                        "1,sci-fi\n" +
                        "2,education\n";

        Scanner scan = new Scanner(data);
        int count = Integer.parseInt(scan.nextLine());

        assertEquals(Code.SHELF_COUNT_ERROR, csumb.initShelves(0, new Scanner("")));
        assertEquals(Code.SUCCESS, csumb.initShelves(count, scan));
        assertEquals(2, csumb.listShelves());
    }

    @Test
    void initReader_test() {
        csumb.addShelf(new Shelf(1, "sci-fi"));
        csumb.addShelf(new Shelf(2, "education"));

        csumb.addBook(new Book("42-w-87", "Hitchhikers Guide To the Galaxy", "sci-fi", 42, "Douglas Adams", LocalDate.of(1970,1,1)));
        csumb.addBook(new Book("1337", "Headfirst Java", "education", 1337, "Grady Booch", LocalDate.of(1970,1,1)));

        String data =
                "2\n" +
                        "1,Drew Clinkenbeard,831-582-4007,1,42-w-87,2020-10-12\n" +
                        "2,Jennifer Clinkenbeard,831-555-6284,1,1337,2020-11-01\n";

        Scanner scan = new Scanner(data);
        int count = Integer.parseInt(scan.nextLine());

        assertEquals(Code.READER_COUNT_ERROR, csumb.initReader(0, new Scanner("")));
        assertEquals(Code.SUCCESS, csumb.initReader(count, scan));
        assertEquals(2, csumb.listReaders());
    }
}