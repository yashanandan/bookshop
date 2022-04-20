package com.tw.bootcamp.bookshop.order;

public class OrderNotPlacedException extends Exception {
    public OrderNotPlacedException(String message) {
        super(message);
    }
}
