package com.tw.bootcamp.bookshop.order.payment;

import com.tw.bootcamp.bookshop.order.OrderException;

public class PaymentAlreadyDoneException extends OrderException {
    public PaymentAlreadyDoneException(String msg) {
        super(msg);
    }
}
