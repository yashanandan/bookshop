package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/books")
    @Operation(summary = "Search and List all books", description = "Lists all books in bookshop matching with search criteria", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List all books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponse.class))})
    })
    List<BookResponse> list(@RequestParam String bookOrAuthorName) {
        List<Book> books = bookService.fetchAll(bookOrAuthorName);
        return books.stream()
                .map(Book::toResponse)
                .collect(toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/books/upload")
    @Operation(summary = "Upload books", description = "Upload the given book csv to the system", tags = {"Books Service"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created")
            }
    )
    List<BookResponse> upload(@RequestParam("file") MultipartFile file) throws BookCsvFileFormatException {

        List<BookResponse> bookResponses = new ArrayList<>();
        if(file.isEmpty()) {
            throw new RuntimeException("Please attach a CSV file");
        } else {
            List<Book> addedBooks = new ArrayList<>();
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<BookCsvModel> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(BookCsvModel.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withVerifier(new BookCsvVerifier())
                        .withThrowExceptions(false)
                        .build();

                List<BookCsvModel> books = csvToBean.parse();
                List<CsvException> exceptions = csvToBean.getCapturedExceptions();

                if(exceptions != null && exceptions.size() > 0) {
                    throw new BookCsvFileFormatException("Uploaded CSV is invalid");
                }
                for(BookCsvModel book : books) {
                    addedBooks.add(bookService.saveBookFromCsv(book));
                }
                bookResponses = addedBooks.stream()
                        .map(Book::toResponse)
                        .collect(toList());
            } catch (IOException e) {
                throw new BookCsvFileFormatException("Uploaded CSV is invalid");
            }
        }
        return bookResponses;
    }
}
