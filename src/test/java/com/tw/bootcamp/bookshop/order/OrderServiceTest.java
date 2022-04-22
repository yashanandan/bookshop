package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.*;
import com.tw.bootcamp.bookshop.order.payment.PaymentAlreadyDoneException;
import com.tw.bootcamp.bookshop.order.payment.PaymentDetails;
import com.tw.bootcamp.bookshop.order.payment.PaymentException;
import com.tw.bootcamp.bookshop.order.payment.PaymentStatus;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;
import org.junit.jupiter.api.DisplayName;
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

  @MockBean BookService bookService;
  @MockBean AddressService addressService;
  @MockBean OrderRepository orderRepository;
  @InjectMocks private OrderService orderService = new OrderService();
  @Mock private RestTemplate restTemplate;

  @Test
  void shouldCreateOrderWhenValid()
      throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {

    CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
    double totalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
    when(bookService.findById(1L)).thenReturn(getBook());
    when(addressService.create(createOrderRequest.getAddress(), null))
        .thenReturn(new AddressTestBuilder().build());
    when(orderRepository.save(any(Order.class))).thenReturn(new OrderTestBuilder().build());

    Order order = orderService.create(createOrderRequest);

    assertEquals(order.getQuantity(), createOrderRequest.getQuantity());
    assertEquals(order.getAmount(), totalAmount);
  }

  @Test
  void shouldDecreaseBookQuantityIfOrderIsValid()
      throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
    CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
    double expectedTotalAmount =
        (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
    int expectedCountAvailable =
        getBook().getCountAvailable().intValue() - createOrderRequest.getQuantity();
    Long expectedCountAvailableAsLong = (long) expectedCountAvailable;

    when(bookService.findById(1L)).thenReturn(getBook());
    when(addressService.create(createOrderRequest.getAddress(), null))
        .thenReturn(new AddressTestBuilder().build());
    when(orderRepository.save(any(Order.class)))
        .thenReturn(new OrderTestBuilder(expectedCountAvailableAsLong, 1l).build());
    when(bookService.save(any()))
        .thenReturn(
            new BookTestBuilder().withBookCountAvailable(expectedCountAvailableAsLong).build());
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
    long orderId = 1L;
    PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();

    when(orderRepository.findById(any(Long.class)))
        .thenReturn(Optional.ofNullable(new OrderTestBuilder().build()));
    when(restTemplate.postForObject(any(String.class), any(Object.class), any()))
        .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
    doNothing()
        .when(orderRepository)
        .updatePaymentStatus(any(Integer.class), any(PaymentStatus.class));

    assertDoesNotThrow(() -> orderService.makePayment(Math.toIntExact(orderId), paymentDetails));
  }

  @Test
  void throwsPaymentExceptionWhenPaymentIsUnsuccessful() throws Exception {
    long orderId = 1L;
    PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();
    String paymentErrorMessage = "400 : [{\"message\":\"Validation Failed\",\"details\":[\"The credit card number is not valid\"]}]";

    when(orderRepository.findById(any(Long.class)))
        .thenReturn(Optional.ofNullable(new OrderTestBuilder().build()));
    when(restTemplate.postForObject(any(String.class), any(Object.class), any()))
        .thenThrow(new RestClientException(paymentErrorMessage));
    doNothing()
        .when(orderRepository)
        .updatePaymentStatus(any(Integer.class), any(PaymentStatus.class));

    assertThrows(PaymentException.class, () -> orderService.makePayment(orderId, paymentDetails));
  }

  @Test
  void throwsOrderExceptionWhenOrderIsInvalid() throws OrderException {
    long orderId = 1L;
    PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();

    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());

    OrderException orderException =
        assertThrows(OrderException.class, () -> orderService.makePayment(orderId, paymentDetails));
    assertEquals("Order Not Found", orderException.getMessage());
  }

  @Test
  void throwsPaymentAlreadyDoneExceptionWhenOrderStatusIsComplete() throws OrderException {
    long orderId = 1L;
    PaymentDetails paymentDetails = PaymentTestBuilder.createPaymentDetails();
    Order order = new OrderTestBuilder().build();
    order.setPaymentStatus(PaymentStatus.COMPLETE);

    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(order));

    PaymentAlreadyDoneException paymentAlreadyDoneException =
        assertThrows(
            PaymentAlreadyDoneException.class,
            () -> orderService.makePayment(orderId, paymentDetails));
    assertEquals(
        "Payment is not processed because order is already paid.",
        paymentAlreadyDoneException.getMessage());
  }

  @Test
  @DisplayName("Should throw book out of stock exception when quantiy is more than requested")
  void shouldThrowBookOutOfStockExeption() throws BookException {

    CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequestWithQuantity(20);
    double totalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
    when(bookService.findById(1L)).thenReturn(getBook());
    when(orderRepository.save(any(Order.class))).thenReturn(new OrderTestBuilder().build());

    assertThrows(BookOutOfStockException.class, () -> orderService.create(createOrderRequest));
  }

  @Test
  @DisplayName("Should throw order not placed exception when the order id is null")
  void shouldThrowOrderNotPlacedException() throws BookException, OrderException {

    CreateOrderRequest createOrderRequest = OrderTestBuilder.createOrderRequest();
    double totalAmount = (createOrderRequest.getQuantity() * getBook().getPrice().getAmount());
    when(bookService.findById(1L)).thenReturn(getBook());
    when(addressService.create(createOrderRequest.getAddress(), null))
        .thenReturn(new AddressTestBuilder().build());
    when(orderRepository.save(any(Order.class)))
        .thenReturn(new OrderTestBuilder(20l, null).build());

    assertThrows(OrderNotPlacedException.class, () -> orderService.create(createOrderRequest));
  }
}
