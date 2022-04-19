package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldFetchAllBooks() {
        Book book = new BookTestBuilder().withName("title").build();
        bookRepository.save(book);

        List<Book> books = bookService.fetchAll("");

        assertEquals(1, books.size());
        assertEquals("title", books.get(0).getName());
    }

    //Dummmy test(can be removed)
    @Test
    void dummyTest() {
        assertTrue(false);
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll("");

        assertEquals(2, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }

    @Test
    void shouldBeEmptyIfThereAreNoBooks() {
        List<Book> books = bookService.fetchAll("");
        assertEquals(0, books.size());
    }

    @Test
    void shouldFetchAllBooksWithMatchingNameBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        List<Book> books = bookService.fetchAll("Animal");

        assertEquals(1, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }
}