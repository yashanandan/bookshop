package com.tw.bootcamp.bookshop.order;

public class OrderNotPlacedException extends OrderException {
    public OrderNotPlacedException(String message) {
        super(message);
    }
}
