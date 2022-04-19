package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;

public class OrderTestBuilder {

    private final Order.OrderBuilder orderBuilder;

    public OrderTestBuilder() {
        this.orderBuilder = Order.builder()
                .id(1l)
                .recipientName("J Doe")
                .address(new AddressTestBuilder().build())
                .book(new BookTestBuilder().build())
                .amount(300)
                .quantity(1);
    }

    public Order build() {
        return orderBuilder.build();
    }
}
