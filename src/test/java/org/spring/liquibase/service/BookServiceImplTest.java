package org.spring.liquibase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.spring.liquibase.entity.Book;
import org.spring.liquibase.exeption.BookNotFoundException;
import org.spring.liquibase.repository.BookRepository;
import org.spring.liquibase.service.bookService.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBook_ShouldReturnSavedBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setPrice(500);

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        assertEquals("Test Book", savedBook.getTitle());
        assertEquals(500, savedBook.getPrice());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void patchBook_ShouldReturnPatchBook() {
        Book oldBook = new Book();
        oldBook.setId(1);
        oldBook.setTitle("Old Title");
        oldBook.setPrice(500);

        Book newBook = new Book();
        newBook.setTitle("New Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(oldBook)).thenReturn(oldBook);

        Book patchedBook = bookService.patchBook(1, newBook);

        assertEquals("New Title", patchedBook.getTitle());
        assertEquals(500, patchedBook.getPrice());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(oldBook);
    }

    @Test
    void getBookById_ShouldReturnBookByID() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setPrice(500);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book gotBook = bookService.getBookById(1L);

        assertEquals(1, gotBook.getId());
        assertEquals("Test Book", gotBook.getTitle());
        assertEquals(500, gotBook.getPrice());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowBookNotFoundException_WhenBooNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void deleteBook_ShouldDeleteBook() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("Book to delete");
        book.setPrice(700);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        bookService.deleteBook(1);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void patchBook_ShouldUpdateOnlyNonNullFields() {
        Book oldBook = new Book();
        oldBook.setId(1);
        oldBook.setTitle("Old Title");
        oldBook.setPrice(500);

        Book newBook = new Book();
        newBook.setTitle("New Title");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(oldBook)).thenReturn(oldBook);

        Book patchedBook = bookService.patchBook(1, newBook);

        assertEquals("New Title", patchedBook.getTitle());
        assertEquals(500, patchedBook.getPrice());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(oldBook);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("Book 1");
        book1.setPrice(400);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book 2");
        book2.setPrice(300);

        List<Book> books = List.of(book1, book2);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> allBooks = bookService.getAllBooks();

        assertEquals(2, allBooks.size());
        assertEquals(book1, allBooks.get(0));
        assertEquals(book2, allBooks.get(1));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBooksSortByMinPrice_ShouldReturnBooksSortedByMin00Price() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setPrice(400);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setPrice(300);

        List<Book> allBooks = List.of(book2, book1);
        when(bookRepository.findAll()).thenReturn(allBooks);

        List<Book> sortedBooks = bookService.getBooksSortByMinPrice();

        assertEquals(2, sortedBooks.size());
        assertEquals(book2, allBooks.get(0));
        assertEquals(book1, allBooks.get(1));
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBooksSortByMaxPrice_ShouldReturnBooksSortedByMaxPrice() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setPrice(400);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setPrice(300);

        List<Book> allBooks = List.of(book1, book2);
        when(bookRepository.findAll()).thenReturn(allBooks);

        List<Book> sortedBooks = bookService.getBooksSortByMinPrice();

        assertEquals(2, sortedBooks.size());
        assertEquals(book1, allBooks.get(0));
        assertEquals(book2, allBooks.get(1));
        verify(bookRepository, times(1)).findAll();
    }
}