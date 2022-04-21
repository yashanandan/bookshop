package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class BookCsvVerifier implements BeanVerifier<BookCsvModel> {
    @Override
    public boolean verifyBean(BookCsvModel book) throws CsvConstraintViolationException {
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new CsvConstraintViolationException("Author name cannot be empty");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new CsvConstraintViolationException("Book title cannot be empty");
        }
        if (book.getPrice() == null || book.getPrice() == 0) {
            throw new CsvConstraintViolationException("Price cannot be empty or 0");
        }
        if (book.getBooksCount() == null || book.getBooksCount() == 0) {
            throw new CsvConstraintViolationException("Book count cannot be empty or 0");
        }
        return true;
    }
}
