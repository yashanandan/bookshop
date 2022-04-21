package com.tw.bootcamp.bookshop.order.payment;

import com.tw.bootcamp.bookshop.order.OrderException;

public class PaymentException extends OrderException {
    public PaymentException(String message) {
        super(message);
    }
}
