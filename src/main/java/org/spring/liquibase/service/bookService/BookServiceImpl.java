package org.spring.liquibase.service.bookService;

import lombok.RequiredArgsConstructor;
import org.spring.liquibase.entity.Book;
import org.spring.liquibase.exeption.BookNotFoundException;
import org.spring.liquibase.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book patchBook(long id, Book book) {
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + id + " не найдена."));

        if (book.getTitle() != null) {
            oldBook.setTitle(book.getTitle());
        }

        if (book.getPrice() != null){
            oldBook.setPrice(book.getPrice());
        }

        return bookRepository.save(oldBook);
    }

    @Override
    public Book getBookById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + bookId + " не найдена."));
    }

    @Override
    public void deleteBook(long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + bookId + " не найдена."));

        bookRepository.delete(book);
    }

    @Override
    public Book updateBook(long id, Book book) {
        Book oldBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + id + " не найдена."));
        oldBook.setTitle(book.getTitle());
        oldBook.setPrice(book.getPrice());

        return bookRepository.save(oldBook);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new BookNotFoundException("Книги в базе данных отсутствуют.");
        }

        return books;
    }



    @Override
    public List<Book> getBooksSortByMinPrice() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new BookNotFoundException("Книги в базе данных отсутствуют.");
        }

        books = books.stream()
                .sorted(Comparator.comparing(Book::getPrice))
                .toList();

        return books;
    }

    @Override
    public List<Book> getBooksSortByMaxPrice() {
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            throw new BookNotFoundException("Книги в базе данных отсутствуют.");
        }

        books = books.stream()
                .sorted(Comparator.comparing(Book::getPrice).reversed())
                .toList();

        return books;
    }
}
