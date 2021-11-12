package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@WithMockUser
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAddressWhenValid() throws Exception {
        CreateAddressRequest createRequest = CreateAddressRequest.builder().build();
        Address address = new AddressTestBuilder().build();
        when(addressService.create(createRequest, any())).thenReturn(address);

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).create(createRequest, any());
    }

    @Test
    void shouldNotCreateAddressWhenInValid() throws Exception {
        CreateAddressRequest createRequest = CreateAddressRequest.builder().city(null).build();
        when(addressService.create(any(), any())).thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verify(addressService, times(1)).create(any(), any());
    }


}