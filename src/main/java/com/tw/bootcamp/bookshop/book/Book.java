package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "books")
@EqualsAndHashCode
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String authorName;
    @Embedded
    private Money price;
    private Long countAvailable;
    private String isbn;
    private Long isbn13;
    private String image;
    @Column(columnDefinition = "NUMERIC")
    private Double avgRating = 0.0;
    @Column(columnDefinition = "NUMERIC")
    private Integer publicationYear;

    public BookResponse toResponse() {
        return BookResponse.builder()
                .id(id)
                .name(name)
                .authorName(authorName)
                .price(price)
                .countAvailable(countAvailable)
                .isbn(isbn)
                .isbn13(isbn13)
                .image(image)
                .avgRating(avgRating)
                .publicationYear(publicationYear)
                .build();
    }

    public void setCountAvailable(Long countAvailable) {
        this.countAvailable = countAvailable;
    }
}
