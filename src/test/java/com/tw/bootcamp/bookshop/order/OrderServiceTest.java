package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.*;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService = new OrderService();

    @MockBean
    BookService bookService;

    @MockBean
    AddressService addressService;

    @MockBean
    OrderRepository orderRepository;

    @Test
    void shouldCreateOrderWhenValid() throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {

        CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
        double totalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
        when(bookService.findById(1l)).thenReturn(getBook());
        when(addressService.create(createOrderRequest.getAddress(), null)).thenReturn(new AddressTestBuilder().build());

        Order notMockedOrder = new Order(createOrderRequest.getRecipientName(), getBook(), new AddressTestBuilder().build(), createOrderRequest.getQuantity());
        when(orderRepository.save(notMockedOrder)).thenReturn(new OrderTestBuilder().build());

        Order order = orderService.create(createOrderRequest);

        assertEquals(order.getQuantity(),createOrderRequest.getQuantity());
        assertEquals(order.getAmount(),totalAmount);
    }

    @Test
    void shouldDecreaseBookQuantityIfOrderIsValid() throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
        CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
        double expectedTotalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
        int expectedCountAvailable = getBook().getCountAvailable().intValue() - createOrderRequest.getQuantity();
        Long expectedCountAvailableAsLong = new Long(expectedCountAvailable);

        when(bookService.findById(1l)).thenReturn(getBook());
        when(addressService.create(createOrderRequest.getAddress(), null)).thenReturn(new AddressTestBuilder().build());
        Order notMockedOrder = new Order(createOrderRequest.getRecipientName(), getBook(), new AddressTestBuilder().build(), createOrderRequest.getQuantity());
        when(orderRepository.save(notMockedOrder)).thenReturn(new OrderTestBuilder(expectedCountAvailableAsLong).build());
        when(bookService.save(any())).thenReturn(new BookTestBuilder().withBookCountAvailable(expectedCountAvailableAsLong).build());
        Order order = orderService.create(createOrderRequest);

        assertEquals(createOrderRequest.getQuantity(), order.getQuantity());
        assertEquals(expectedCountAvailable, order.getBook().getCountAvailable());
        assertEquals(expectedTotalAmount, order.getAmount());
    }

    private Book getBook() {
        return new BookTestBuilder().build();
    }
}
