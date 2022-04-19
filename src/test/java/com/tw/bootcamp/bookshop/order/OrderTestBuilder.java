package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;

public class OrderTestBuilder {

    private final Order.OrderBuilder orderBuilder;

    public OrderTestBuilder() {
        this.orderBuilder = Order.builder()
                .address(new AddressTestBuilder().build())
                .book(new BookTestBuilder().build());
    }

    public Order build() {
        return orderBuilder.build();
    }
}
