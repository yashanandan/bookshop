package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.money.Money;

public class BookTestBuilder {
    private final Book.BookBuilder bookBuilder;

    public BookTestBuilder() {
        bookBuilder = Book.builder().name("Harry Potter")
                .authorName("J K Rowling")
                .price(Money.rupees(300))
                .countAvailable(10l)
                .isbn("1416914285")
                .isbn13(9781416914280l)
                .image("https://images.gr-assets.com/books/1432730315m/256683.jpg")
                .avgRating(4.12)
                .publicationYear(2007);
    }

    public Book build() {
        return bookBuilder.build();
    }

    public BookTestBuilder withPrice(int price) {
        bookBuilder.price(Money.rupees(price));
        return this;
    }

    public BookTestBuilder withName(String name) {
        bookBuilder.name(name);
        return this;
    }

    public BookTestBuilder withBookCountAvailable(Long countAvailable) {
        bookBuilder.countAvailable(countAvailable);
        return this;
    }

    public static BookCsvModel getBookCsvModel(){
        return new BookCsvModel(
                "Cassandra Clare",
                "City of Bones (The Mortal Instruments, #1)",
                "https://images.gr-assets.com/books/1432730315m/256683.jpg",
                "https://images.gr-assets.com/books/1432730315s/256683.jpg",
                1461.0,
                178l,
                "1416914285",
                9781416914280l,
                2007,
                "City of Bones","eng",4.12);
    }

    public static Book getBookFromCsv(BookCsvModel bookCsvModel){
        return new Book(bookCsvModel);
    }
}
