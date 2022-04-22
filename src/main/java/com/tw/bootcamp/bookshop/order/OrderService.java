package com.tw.bootcamp.bookshop.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.*;
import com.tw.bootcamp.bookshop.order.payment.PaymentAlreadyDoneException;
import com.tw.bootcamp.bookshop.order.payment.PaymentDetails;
import com.tw.bootcamp.bookshop.order.payment.PaymentException;
import com.tw.bootcamp.bookshop.order.payment.PaymentStatus;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public void makePayment(long orderId, PaymentDetails paymentDetails) throws OrderNotFoundException, PaymentAlreadyDoneException, PaymentException {
        validateOrderForPayment(orderId);
        try {
            restTemplate.postForObject("https://tw-mock-credit-service.herokuapp.com/payments", paymentDetails, ResponseEntity.class);
        } catch (RestClientException ex) {
            orderRepository.updatePaymentStatus(orderId, PaymentStatus.FAILED);
            throw new PaymentException(getPaymentServiceErrorMessage(ex));
        }
        orderRepository.updatePaymentStatus(orderId, PaymentStatus.COMPLETE);
    }

    private String getPaymentServiceErrorMessage(RestClientException ex) {
        JSONObject json = new JSONObject("{"+ ex.getMessage()+"}");
        JSONArray jsonArray = new JSONArray(json.get("400").toString());
        JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
        String errorMessage = jsonObject.get("details").toString();
        errorMessage = errorMessage.substring(2,errorMessage.length()-2);
        return errorMessage;
    }

    private void validateOrderForPayment(long id) throws PaymentAlreadyDoneException, OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order Not Found"));
        if (order.getPaymentStatus() == PaymentStatus.COMPLETE) {
            throw new PaymentAlreadyDoneException("Payment is not processed because order is already paid.");
        }
    }
}
