package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.order.payment.PaymentMode;
import com.tw.bootcamp.bookshop.order.payment.PaymentStatus;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import com.tw.bootcamp.bookshop.user.address.CreateAddressRequest;

public class OrderTestBuilder {

    private final Order.OrderBuilder orderBuilder;

    public OrderTestBuilder() {
        this.orderBuilder = Order.builder()
                .id(1l)
                .recipientName("J Doe")
                .address(new AddressTestBuilder().build())
                .book(new BookTestBuilder().build())
                .amount(300)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMode(PaymentMode.CARD)
                .quantity(1);
    }

    public OrderTestBuilder(Long countAvailable, Long id) {
        this.orderBuilder = Order.builder()
                .id(id)
                .recipientName("J Doe")
                .address(new AddressTestBuilder().build())
                .book(new BookTestBuilder().withBookCountAvailable(countAvailable).build())
                .amount(300)
                .paymentMode(PaymentMode.COD)
                .paymentStatus(PaymentStatus.PENDING)
                .quantity(1);
    }

    public static CreateOrderRequest createOrderRequest() {
        return CreateOrderRequest.builder()
                .bookId(1l)
                .address(createAddress())
                .quantity(1)
                .recipientName("J Doe")
                .build();
    }

    public static CreateOrderRequest createOrderRequestWithQuantity(int quantity) {
        return CreateOrderRequest.builder()
                .bookId(1l)
                .address(createAddress())
                .quantity(quantity)
                .recipientName("J Doe")
                .mobileNumber(1234567890l)
                .build();
    }

    private static CreateAddressRequest createAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }


    public Order build() {
        return orderBuilder.build();
    }

    public OrderTestBuilder withBookCountAvailable(Long countAvailable) {
        return new OrderTestBuilder(countAvailable, 1l);
    }
}
