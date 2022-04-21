package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.order.payment.PaymentMode;
import com.tw.bootcamp.bookshop.user.address.CreateAddressRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class CreateOrderRequest {

    private Long bookId;

    private CreateAddressRequest address;

    private int quantity;

    private String recipientName;

    private PaymentMode paymentMode;

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "bookId=" + bookId +
                ", address=" + address +
                ", quantity=" + quantity +
                ", recipientName='" + recipientName + '\'' +
                ", paymentMode=" + paymentMode +
                '}';
    }

}
