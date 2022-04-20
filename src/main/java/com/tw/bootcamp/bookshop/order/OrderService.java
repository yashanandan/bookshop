package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.*;
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

    public Order create(CreateOrderRequest request) throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
        Book book = bookService.findById(request.getBookId());
        if(book.getCountAvailable() < request.getQuantity()) {
            throw new BookOutOfStockException("Requested quantity is more than current stock");
        }
        Address newAddress = addressService.create(request.getAddress(),null);
        Order newOrder = new Order(request.getRecipientName(),book,newAddress,request.getQuantity());
        newOrder = orderRepository.save(newOrder);
        if(newOrder == null || newOrder.getId() == null) {
            throw new OrderNotPlacedException("Order was not saved");
        }
        book.setCountAvailable(book.getCountAvailable() - request.getQuantity());
        bookService.save(book);
        return newOrder;
    }

}
