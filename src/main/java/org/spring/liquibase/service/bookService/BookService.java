package org.spring.liquibase.service.bookService;

import org.spring.liquibase.entity.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);

    Book getBookById(long bookId);

    void deleteBook(long bookId);

    Book updateBook(long id, Book book);

    Book patchBook(long id, Book book);

    List<Book> getAllBooks();

    List<Book> getBooksSortByMinPrice();

    List<Book> getBooksSortByMaxPrice();
}
