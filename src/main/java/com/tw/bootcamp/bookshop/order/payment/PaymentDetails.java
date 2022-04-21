package com.tw.bootcamp.bookshop.order.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PaymentDetails {
    private int amount;
    private String creditCardNumber;
    private String creditCardExpiration;
    private int cardSecurityCode;
}
