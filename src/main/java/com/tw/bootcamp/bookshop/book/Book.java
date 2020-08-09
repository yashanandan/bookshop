package com.tw.bootcamp.bookshop.book;

import javax.persistence.Entity;

@Entity
public class Book {
    private String name;
    private String authorName;
    private Integer price;

    public Book(String name, String authorName, Integer price) {
        this.name = name;
        this.authorName = authorName;
        this.price = price;
    }
}
