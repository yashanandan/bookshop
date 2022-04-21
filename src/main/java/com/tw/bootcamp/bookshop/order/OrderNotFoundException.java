package com.tw.bootcamp.bookshop.order;

public class OrderNotFoundException extends OrderException{
    public OrderNotFoundException(String message) {
        super(message);
    }
}
