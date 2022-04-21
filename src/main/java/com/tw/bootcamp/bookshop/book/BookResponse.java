package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;
import lombok.*;
import org.springframework.lang.Nullable;

@Builder
@Getter
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String name;
    private String authorName;
    private Money price;
    private Long countAvailable;
    private String isbn;
    private Long isbn13;
    private String image;
    private Double avgRating;
    private Integer publicationYear;
}
