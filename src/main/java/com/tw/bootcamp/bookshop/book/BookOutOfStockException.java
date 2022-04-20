package com.tw.bootcamp.bookshop.book;

public class BookOutOfStockException extends Exception {

    public BookOutOfStockException(String message) {
        super(message);
    }
}
