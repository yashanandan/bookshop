package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public Order(Book book, Address address) {
        this.book = book;
        this.address = address;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .id(id)
                .book(book)
                .address(address)
                .createdAt(createdAt)
                .build();
    }

    public static Order create(Book book, Address address) {
        return new Order(book, address);
    }

}
