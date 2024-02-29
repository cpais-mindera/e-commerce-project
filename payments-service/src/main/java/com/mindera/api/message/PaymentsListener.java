package com.mindera.api.message;

import com.mindera.api.domain.Payment;
import com.mindera.api.enums.Status;
import com.mindera.api.model.PaymentResponse;
import com.mindera.api.repository.PaymentRepository;
import org.springframework.amqp.core.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentsListener {

    private final PaymentRepository paymentRepository;
    private final OrderMessageSender orderMessageSender;
    private final Queue queuePaymentListener;

    @RabbitListener(queues = "PAYMENT_LISTENER")
    public PaymentResponse receive(@Payload PaymentMessage paymentMessage) {
        Payment payment;

        payment = Payment.builder()
                .cartId(paymentMessage.getCartId())
                .cardHolderName(paymentMessage.getCardHolderName())
                .cardNumber(paymentMessage.getCardNumber())
                .expireDate(paymentMessage.getExpireDate())
                .cvv(paymentMessage.getCvv()).build();

        if (paymentMessage.getCvv().equals(999L)) {
            payment = passPayment(payment);

            orderMessageSender.send(paymentMessage.getCartId());
        } else {
            payment = declinePayment(payment);
        }

        try {
            paymentRepository.save(payment);
        } catch (Exception ex) {
            log.error("Error setting payment : " + payment);
        }
        return new PaymentResponse(payment);
    }

    private Payment passPayment(Payment payment) {
        payment.setPaymentStatus(Status.PAID);
        return payment;
    }

    private Payment declinePayment(Payment payment) {
        payment.setPaymentStatus(Status.FAILED_PAYMENT);
        return payment;
    }
}