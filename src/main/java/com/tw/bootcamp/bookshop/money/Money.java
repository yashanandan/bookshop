package com.tw.bootcamp.bookshop.money;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Money {
    private String currency;
    private double amount;

    public static Money rupees(double amount) {
        return new Money("INR", amount);
    }
}
