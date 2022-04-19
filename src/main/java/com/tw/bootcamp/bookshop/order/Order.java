package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "orders")
@EqualsAndHashCode
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_name")
    private String recipientName;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private int quantity;

    @Column(columnDefinition = "NUMERIC")
    private double amount = 0.0;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public Order(String recipientName, Book book, Address address, int quantity) {
        this.recipientName = recipientName;
        this.book = book;
        this.address = address;
        this.quantity = quantity;
        this.amount = quantity * book.getPrice().getAmount();
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .id(id)
                .book(book)
                .address(address)
                .createdAt(createdAt)
                .build();
    }

    public static Order create(String recipientName, Book book, Address address, int quantity) {
        return new Order(recipientName, book, address, quantity);
    }

}
