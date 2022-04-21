package com.tw.bootcamp.bookshop.book;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class BookCsvModel {

    @CsvBindByName(column = "author", required = true)
    private String author;
    @CsvBindByName(column = "title", required = true)
    private String title;
    @CsvBindByName(column = "image_url")
    private String imageUrl;
    @CsvBindByName(column = "small_image_url")
    private String smallImageUrl;
    @CsvBindByName(column = "price", required = true)
    private Double price;
    @CsvBindByName(column = "books_count", required = true)
    private Long booksCount;
    @CsvBindByName(column = "isbn")
    private String isbn;
    @CsvBindByName(column = "isbn13")
    private Long isbn13;
    @CsvBindByName(column = "original_publication_year")
    private Integer originalPublicationYear;
    @CsvBindByName(column = "original_title")
    private String originalTitle;
    @CsvBindByName(column = "language_code")
    private String languageCode;
    @CsvBindByName(column = "average_rating")
    private Double averageRating;

}
