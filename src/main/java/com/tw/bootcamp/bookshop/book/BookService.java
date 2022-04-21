package com.tw.bootcamp.bookshop.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll(String bookOrAuthorName) {
        if(bookOrAuthorName.isEmpty()){
           return bookRepository.findAllByOrderByNameAsc();
        }
        return bookRepository.findAllByNameContainsIgnoreCaseOrAuthorNameContainsIgnoreCaseOrderByNameAsc( bookOrAuthorName, bookOrAuthorName);
    }

    public Book findById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book saveBookFromCsv(BookCsvModel bookCsv) {
        Book existingBook = bookRepository.findByIsbn13(bookCsv.getIsbn13());
        if(existingBook != null){
            existingBook.update(bookCsv);
            bookRepository.save(existingBook);
        } else {
            existingBook = new Book(bookCsv);
            bookRepository.save(existingBook);
        }
        return existingBook;
    }
}
