package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.address.AddressTestBuilder;

public class PaymentTestBuilder {


    private final PaymentDetails.PaymentDetailsBuilder paymentBuilder;


    public PaymentTestBuilder() {
        this.paymentBuilder = PaymentDetails.builder()
                .amount(100)
                .creditCardNumber("3566-0020-2036-0505")
                .creditCardExpiration("03/2100")
                .cardSecurityCode(666);
    }

    public static PaymentDetails createPaymentDetails() {
        return PaymentDetails.builder()
                .amount(100)
                .creditCardNumber("3566-0020-2036-0506")
                .creditCardExpiration("03/2100")
                .cardSecurityCode(666).build();
    }
}
