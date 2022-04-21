package com.tw.bootcamp.bookshop.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private BookRepository mockedBookRepository;

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
        when(bookRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(book));

        List<Book> books = bookService.fetchAll("");

        assertEquals(1, books.size());
        assertEquals("title", books.get(0).getName());
    }

    @Test
    void shouldFetchAllBooksBeSortedByNameAscending() {
        Book wingsOfFire = new BookTestBuilder().withName("Wings of Fire").build();
        Book animalFarm = new BookTestBuilder().withName("Animal Farm").build();
        bookRepository.save(wingsOfFire);
        bookRepository.save(animalFarm);

        when(bookRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(animalFarm, wingsOfFire));

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

        when(
                bookRepository.findAllByNameContainsIgnoreCaseOrAuthorNameContainsIgnoreCaseOrderByNameAsc( "Animal", "Animal")
        ).thenReturn(Arrays.asList(animalFarm));

        List<Book> books = bookService.fetchAll("Animal");

        assertEquals(1, books.size());
        assertEquals("Animal Farm", books.get(0).getName());
    }

    @Test
    void shouldAddBookWhenUploadedFromCSV() {
        // insert book first
        Book newBook = BookTestBuilder.getBookFromCsv(BookTestBuilder.getBookCsvModel());
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book savedBook = bookService.saveBookFromCsv(BookTestBuilder.getBookCsvModel());

        // mocking for getting books
        when(bookRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(savedBook));

        List<Book> books = bookService.fetchAll("");

        assertEquals(1, books.size());
        assertEquals("City of Bones (The Mortal Instruments, #1)", books.get(0).getName());
    }

    @Test
    void shouldIncreseBookCountWhenBookIsAvailable() {
        // mock the isbn13 book exists
        Book existingBook = BookTestBuilder.getBookFromCsv(BookTestBuilder.getBookCsvModel());
        when(mockedBookRepository.findByIsbn13(BookTestBuilder.getBookCsvModel().getIsbn13())).thenReturn(existingBook);
        when(mockedBookRepository.save(existingBook)).thenReturn(existingBook);

        Book bookToBeUploaded = bookService.saveBookFromCsv(BookTestBuilder.getBookCsvModel());

        // assert mocked book has count increased
        assertEquals(BookTestBuilder.getBookCsvModel().getBooksCount()*2, bookToBeUploaded.getCountAvailable());

    }
}