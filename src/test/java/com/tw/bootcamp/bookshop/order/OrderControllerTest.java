package com.tw.bootcamp.bookshop.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.CreateAddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

        mockMvc.perform(post("/api/orders")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(new OrderResponse(order))));
        verify(orderService, times(1)).create(request);
    }


}
