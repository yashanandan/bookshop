package com.tw.bootcamp.bookshop.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.CreateAddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrderWhenValid() throws Exception {
        CreateOrderRequest request = OrderTestBuilder.createOrderRequest();
        Order order = new OrderTestBuilder().build();
        when(orderService.create(request)).thenReturn(order);

        mockMvc.perform(post("/orders")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(new OrderResponse(order))));
        verify(orderService, times(1)).create(request);
    }

    @Test
    void shouldMakePaymentWhenOrderIsValid() throws Exception {
        long orderId = 1;
        PaymentDetails request = PaymentTestBuilder.createPaymentDetails();
        doNothing().when(orderService).makePayment(any(Integer.class), any(PaymentDetails.class));

        mockMvc.perform(post("/orders/1/payment")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted());
    }


}
