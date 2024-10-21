package org.spring.liquibase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.liquibase.entity.Book;
import org.spring.liquibase.service.bookService.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false) // Отключает все фильтры, включая Security

class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookServiceImpl bookService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test

    void addBook_ShouldReturnCreatedBook() {
        Book bookRequest = new Book();
        bookRequest.setPrice(200);
        bookRequest.setTitle("Book Title");

        Book bookResponse = new Book();
        bookResponse.setId(1);
        bookResponse.setPrice(200);
        bookResponse.setTitle("Book Title");

        when(bookService.saveBook(bookRequest)).thenReturn(bookResponse);

        try {
            mockMvc.perform(post("/books/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(bookResponse.getId()))
                    .andExpect(jsonPath("$.title").value(bookResponse.getTitle()))
                    .andExpect(jsonPath("$.price").value(bookResponse.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService, times(1)).saveBook(bookRequest);


    }

    @Test
    void getBook_ShouldReturnBook() {
        Book book = new Book();
        book.setId(1);
        book.setPrice(200);
        book.setTitle("Book Title");

        when(bookService.getBookById(1)).thenReturn(book);

        try {
            mockMvc.perform(get("/books/{id}",book.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(book.getId()))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.price").value(book.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(bookService,times(1)).getBookById(book.getId());


    }

    @Test
    void deleteBook_ShouldReturnOkStatus() {
        long id = 1;

        try {
            mockMvc.perform(delete("/books/{id}", id))
                    .andExpect(status().isOk())
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).deleteBook(id);

    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() {
        Book book = new Book();
        book.setId(1);
        book.setPrice(700);

        when(bookService.updateBook(1, book)).thenReturn(book);

        try {
            mockMvc.perform(put("/books/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(book.getId()))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.price").value(book.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).updateBook(1, book);
    }

    @Test
    void patchBook_ShouldReturnPatchedBook() {
        Book book = new Book();
        book.setTitle("Patched Title");

        Book patchedBook = new Book();
        patchedBook.setId(1);
        patchedBook.setTitle("Patched Title");
        patchedBook.setPrice(500);

        when(bookService.patchBook(1, book)).thenReturn(patchedBook);

        try {
            mockMvc.perform(patch("/books/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(book)))
                    .andExpect(jsonPath("$.id").value(patchedBook.getId()))
                    .andExpect(jsonPath(("$.title")).value(patchedBook.getTitle()))
                    .andExpect(jsonPath("$.price").value(patchedBook.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).patchBook(1, book);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks(){
        Book book = new Book();
        book.setId(1);
        book.setTitle("Book Title 1");
        book.setPrice(200);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book Title 2");
        book2.setPrice(300);

        List<Book> books = List.of(book, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        try {
            mockMvc.perform(get("/books/getAll"))
                    .andExpect(jsonPath("$[0].id").value(book.getId()))
                    .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                    .andExpect(jsonPath("$[0].price").value(book.getPrice()))
                    .andExpect(jsonPath("$[1].id").value(book2.getId()))
                    .andExpect(jsonPath("$[1].title").value(book2.getTitle()))
                    .andExpect(jsonPath("$[1].price").value(book2.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).getAllBooks();
    }

    @Test
    void SortByMaxPrice_ShouldReturnBooksSortedByMaxPrice(){
        Book book = new Book();
        book.setId(1);
        book.setTitle("Book Title 1");
        book.setPrice(200);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book Title 2");
        book2.setPrice(300);

        List<Book> books = List.of(book2, book);

        when(bookService.getBooksSortByMaxPrice()).thenReturn(books);

        try {
            mockMvc.perform(get("/books/sortByMax"))
                    .andExpect(jsonPath("$[0].id").value(book2.getId()))
                    .andExpect(jsonPath("$[0].title").value(book2.getTitle()))
                    .andExpect(jsonPath("$[0].price").value(book2.getPrice()))
                    .andExpect(jsonPath("$[1].id").value(book.getId()))
                    .andExpect(jsonPath("$[1].title").value(book.getTitle()))
                    .andExpect(jsonPath("$[1].price").value(book.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).getBooksSortByMaxPrice();
    }

    @Test
    void SortByMinPrice_ShouldReturnBooksSortedByMinPrice(){
        Book book = new Book();
        book.setId(1);
        book.setTitle("Book Title 1");
        book.setPrice(200);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book Title 2");
        book2.setPrice(100);

        List<Book> books = List.of(book2, book);

        when(bookService.getBooksSortByMinPrice()).thenReturn(books);

        try {
            mockMvc.perform(get("/books/sortByMin"))
                    .andExpect(jsonPath("$[0].id").value(book2.getId()))
                    .andExpect(jsonPath("$[0].title").value(book2.getTitle()))
                    .andExpect(jsonPath("$[0].price").value(book2.getPrice()))
                    .andExpect(jsonPath("$[1].id").value(book.getId()))
                    .andExpect(jsonPath("$[1].title").value(book.getTitle()))
                    .andExpect(jsonPath("$[1].price").value(book.getPrice()))
                    .andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(bookService,times(1)).getBooksSortByMinPrice();
    }



}