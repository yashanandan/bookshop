package com.tw.bootcamp.bookshop.book;

public class BookOutOfStockException extends BookException {

    public BookOutOfStockException(String message) {
        super(message);
    }
}
