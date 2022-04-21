package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.order.payment.PaymentMode;
import com.tw.bootcamp.bookshop.order.payment.PaymentStatus;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Order(String recipientName, Book book, Address address, int quantity, PaymentMode paymentMode) {
        this.recipientName = recipientName;
        this.book = book;
        this.address = address;
        this.quantity = quantity;
        this.amount = quantity * book.getPrice().getAmount();
        this.paymentMode = paymentMode;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .id(id)
                .book(book)
                .address(address)
                .createdAt(createdAt)
                .build();
    }

    public static Order create(String recipientName, Book book, Address address, int quantity, PaymentMode paymentMode) {
        return new Order(recipientName, book, address, quantity, paymentMode);
    }

}
