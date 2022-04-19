package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookNotFoundException;
import com.tw.bootcamp.bookshop.book.BookRepository;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookService bookService;

    @Autowired
    AddressService addressService;

    public Order create(CreateOrderRequest request) throws BookNotFoundException {
        Book book = bookService.findById(request.getBookId());
        Address newAddress = addressService.create(request.getAddress(),null);
        Order newOrder = new Order(request.getRecipientName(),book,newAddress,request.getQuantity());
        return orderRepository.save(newOrder);
    }
}
