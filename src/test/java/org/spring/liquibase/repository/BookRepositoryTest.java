package org.spring.liquibase.repository;


import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spring.liquibase.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;


    @Test
    void save() {
        Book book = new Book();
        book.setPrice(200);
        book.setTitle("Book Title");

        bookRepository.save(book);
        Assertions.assertThat(book).isNotNull();
        Assertions.assertThat(book.getPrice()).isEqualTo(200);
        Assertions.assertThat(book.getTitle()).isEqualTo("Book Title");
    }

    @Test
    void findById() {
        Book book = new Book();
        book.setPrice(200);
        book.setTitle("Book Title");

        bookRepository.save(book);
        Optional<Book> optional = bookRepository.findById(book.getId());
        Assertions.assertThat(optional.isPresent()).isTrue();
        Assertions.assertThat(optional.get().getPrice()).isEqualTo(200);
        Assertions.assertThat(optional.get().getTitle()).isEqualTo("Book Title");
    }

    @Test
    void findById_NotFound() {
        Assertions.assertThat(bookRepository.findById(-1L)).isNotPresent();
    }


    @Test
    void delete(){
        Book book = new Book();
        book.setPrice(200);
        book.setTitle("Book Title");

        Book saveBook = bookRepository.save(book);
        bookRepository.delete(book);

        Assertions.assertThat(bookRepository.findById(saveBook.getId())).isNotPresent();
    }

    @Test
    void findAll(){
        Book book1 = new Book();
        book1.setPrice(200);
        book1.setTitle("Book Title1");

        Book book2 = new Book();
        book2.setPrice(300);
        book2.setTitle("Book Title2");

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        Assertions.assertThat(books.size()).isEqualTo(2);
        Assertions.assertThat(books.get(0).getPrice()).isEqualTo(200);
        Assertions.assertThat(books.get(0).getTitle()).isEqualTo("Book Title1");
        Assertions.assertThat(books.get(1).getPrice()).isEqualTo(300);
        Assertions.assertThat(books.get(1).getTitle()).isEqualTo("Book Title2");
    }

    @Test
    void findAll_NotFound(){
        Assertions.assertThat(bookRepository.findAll()).isEmpty();
    }

}