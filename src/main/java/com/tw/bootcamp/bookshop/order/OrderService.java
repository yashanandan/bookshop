package com.tw.bootcamp.bookshop.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.*;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookService bookService;

    @Autowired
    AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    public Order create(CreateOrderRequest request) throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
        Book book = bookService.findById(request.getBookId());
        if (book.getCountAvailable() < request.getQuantity()) {
            throw new BookOutOfStockException("Requested quantity is more than current stock");
        }
        Address newAddress = addressService.create(request.getAddress(), null);
        Order newOrder = new Order(request.getRecipientName(), book, newAddress, request.getQuantity(), request.getPaymentMode());
        newOrder = orderRepository.save(newOrder);
        if (newOrder.getId() == null) {
            throw new OrderNotPlacedException("Order was not saved");
        }
        book.setCountAvailable(book.getCountAvailable() - request.getQuantity());
        bookService.save(book);
        return newOrder;
    }

    @Transactional
    public void makePayment(long orderId, PaymentDetails paymentDetails) throws OrderException {
        validateOrderForPayment(orderId);
        try {
            restTemplate.postForObject("https://tw-mock-credit-service.herokuapp.com/payments", paymentDetails, ResponseEntity.class);
        } catch (RestClientException ex) {
            throw new OrderException(ex.getMessage());
        }
        orderRepository.updatePaymentStatus(orderId, PaymentStatus.COMPLETE);
    }

    private void validateOrderForPayment(long id) throws OrderException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderException("Order Not Found"));
        if (order.getPaymentStatus() == PaymentStatus.COMPLETE) {
            throw new OrderException("Order is already paid");
        }
    }
}
