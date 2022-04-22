package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    UserService userService;

    @Test
    void shouldListAllBooksWhenPresent() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        when(bookService.fetchAll("")).thenReturn(books);

        mockMvc.perform(get("/books").param("bookOrAuthorName","")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll("");
    }

    @Test
    void shouldBeEmptyListWhenNoBooksPresent() throws Exception {
        when(bookService.fetchAll("")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/books").param("bookOrAuthorName","")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        verify(bookService, times(1)).fetchAll("");
    }

    @Test
    void shouldListTheBooksWithMatchingSearchCriteria() throws Exception {
        List<Book> books = new ArrayList<>();
        Book book = new BookTestBuilder().build();
        books.add(book);
        String bookOrAuthorName = "Harry";
        when(bookService.fetchAll(any(String.class))).thenReturn(books);

        mockMvc.perform(get("/books").param("bookOrAuthorName",bookOrAuthorName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(bookService, times(1)).fetchAll(bookOrAuthorName);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUploadFileWhenValid() throws Exception {
        String csv = "id,author,title,image_url,small_image_url,price,books_count,isbn,isbn13,original_publication_year,original_title,language_code,average_rating\n" +
                "51,Cassandra Clare,\"City of Bones (The Mortal Instruments, #1)\",https://images.gr-assets.com/books/1432730315m/256683.jpg,https://images.gr-assets.com/books/1432730315s/256683.jpg,1461,178,1416914285,9781416914280,2007,City of Bones,eng,4.12";
        MockMultipartFile file = new MockMultipartFile("file",
                "csv_test_2_.csv",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                csv.getBytes());

        when(bookService.saveBookFromCsv(getBookCsvModel())).thenReturn(getBookFromCsv());
        mockMvc.perform(multipart("/books/upload").file(file)).andExpect(status().isOk());
    }

    private BookCsvModel getBookCsvModel() {
        return BookTestBuilder.getBookCsvModel();
    }

    private Book getBookFromCsv() {
        return BookTestBuilder.getBookFromCsv(getBookCsvModel());
    }


}