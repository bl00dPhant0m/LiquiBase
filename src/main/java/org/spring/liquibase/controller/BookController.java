package org.spring.liquibase.controller;


import lombok.RequiredArgsConstructor;
import org.spring.liquibase.entity.Book;
import org.spring.liquibase.service.bookService.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    final private BookServiceImpl bookService;

    @PostMapping("/add")
    public Book addBook(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @PatchMapping("/{id}")
    public Book patchBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.patchBook(id, book);
    }

    @GetMapping("/getAll")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/sortByMax")
    public List<Book> sortMaxPrice() {
        return bookService.getBooksSortByMaxPrice();
    }

    @GetMapping("/sortByMin")
    public List<Book> sortMinPrice() {
        return bookService.getBooksSortByMinPrice();
    }

}
