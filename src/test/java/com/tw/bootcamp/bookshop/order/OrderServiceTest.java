package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.*;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Mock
    private RestTemplate restTemplate;


    @Test
    void shouldCreateOrderWhenValid() throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {

        CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
        double totalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
        when(bookService.findById(1l)).thenReturn(getBook());
        when(addressService.create(createOrderRequest.getAddress(), null)).thenReturn(new AddressTestBuilder().build());
        when(orderRepository.save(any(Order.class))).thenReturn(new OrderTestBuilder().build());

        Order order = orderService.create(createOrderRequest);

        assertEquals(order.getQuantity(), createOrderRequest.getQuantity());
        assertEquals(order.getAmount(), totalAmount);
    }

    @Test
    void shouldDecreaseBookQuantityIfOrderIsValid() throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
        CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
        double expectedTotalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
        int expectedCountAvailable = getBook().getCountAvailable().intValue() - createOrderRequest.getQuantity();
        Long expectedCountAvailableAsLong = new Long(expectedCountAvailable);

        when(bookService.findById(1l)).thenReturn(getBook());
        when(addressService.create(createOrderRequest.getAddress(), null)).thenReturn(new AddressTestBuilder().build());
        when(orderRepository.save(any(Order.class))).thenReturn(new OrderTestBuilder(expectedCountAvailableAsLong).build());
        when(bookService.save(any())).thenReturn(new BookTestBuilder().withBookCountAvailable(expectedCountAvailableAsLong).build());
        Order order = orderService.create(createOrderRequest);

        assertEquals(createOrderRequest.getQuantity(), order.getQuantity());
        assertEquals(expectedCountAvailable, order.getBook().getCountAvailable());
        assertEquals(expectedTotalAmount, order.getAmount());
    }

    private Book getBook() {
        return new BookTestBuilder().build();
    }

    @Test
    void shouldMakePaymentWhenOrderIsValid() throws OrderException {
        Long orderId = 1l;
        PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();

        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(new OrderTestBuilder().build()));
        when(restTemplate.postForObject(any(String.class), any(Object.class), any())).thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        doNothing().when(orderRepository).updatePaymentStatus(any(Integer.class), any(PaymentStatus.class));

        assertDoesNotThrow(() -> orderService.makePayment(Math.toIntExact(orderId), paymentDetails));
    }

    @Test
    void throwsOrderExceptionWhenPaymentIsUnsuccessful() throws Exception {
        Long orderId = 1l;
        PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();

        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(new OrderTestBuilder().build()));
        when(restTemplate.postForObject(any(String.class), any(Object.class), any())).thenThrow(new RestClientException("Card details are invalid"));
        doNothing().when(orderRepository).updatePaymentStatus(any(Integer.class), any(PaymentStatus.class));

        assertThrows(OrderException.class, () -> orderService.makePayment(Math.toIntExact(orderId), paymentDetails));
    }

    @Test
    void throwsOrderExceptionWhenOrderIsInvalid() throws OrderException {
        Long orderId = 1l;
        PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();

        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OrderException orderException = assertThrows(OrderException.class, () -> orderService.makePayment(Math.toIntExact(orderId), paymentDetails));
        assertEquals(orderException.getMessage(), "Order Not Found");
    }

    @Test
    void throwsOrderExceptionWhenOrderStatusIsComplete() throws OrderException {
        Long orderId = 1l;
        PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();
        Order order = new OrderTestBuilder().build();
        order.setPaymentStatus(PaymentStatus.COMPLETE);

        when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(order));

        OrderException orderException = assertThrows(OrderException.class, () -> orderService.makePayment(Math.toIntExact(orderId), paymentDetails));
        assertEquals(orderException.getMessage(), "Order is already paid");
    }
}
