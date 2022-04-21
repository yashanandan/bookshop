package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.order.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query("update Order o set o.paymentStatus = :paymentStatus where o.id = :orderId")
    void updatePaymentStatus(long orderId, PaymentStatus paymentStatus);
}
