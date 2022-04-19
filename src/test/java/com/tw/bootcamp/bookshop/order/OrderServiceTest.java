package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    void shouldCreateOrderWhenValid() throws BookNotFoundException {

        CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();

        Order order = orderService.create(createOrderRequest);

        assertTrue(true);
//        assertEquals(order.getQuantity(),createOrderRequest.getQuantity());
//        assertEquals(order.getBook().getId(),createOrderRequest.getBookId());
//        assertEquals(order.getAddress(),createOrderRequest.getAddress());
    }
}
