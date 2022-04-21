package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookNotFoundException;
import com.tw.bootcamp.bookshop.book.BookOutOfStockException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Creates order for anonymous user", tags = {"Order Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
            description = "Order created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseEntity.class))})}
    )
    public ResponseEntity create(@RequestBody CreateOrderRequest createOrderRequest) throws BookNotFoundException, BookOutOfStockException, OrderNotPlacedException {
        Order order = orderService.create(createOrderRequest);
        return new ResponseEntity<>(new OrderResponse(order), HttpStatus.CREATED);
    }

    @PostMapping("/{orderId}/payment")
    @Operation(summary = "Create order payment", description = "Creates payments for the order", tags = {"Order Service"})
    @ApiResponses(value = {@ApiResponse(responseCode = "201",
            description = "Order payment created", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseEntity.class))})}
    )
    public void payment(@PathVariable long orderId, @RequestBody PaymentDetails paymentDetails) throws OrderException {
        orderService.makePayment(orderId, paymentDetails);
    }
}
