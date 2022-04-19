package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private String recipientName;
    private Book book;
    private Address address;
    private int quantity;
    private Timestamp createdAt;
    private double amount;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.recipientName = order.getRecipientName();
        this.book = order.getBook();
        this.address = order.getAddress();
        this.quantity = order.getQuantity();
        this.createdAt = order.getCreatedAt();
        this.amount = order.getAmount();
    }
}
