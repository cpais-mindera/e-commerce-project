package com.mindera.api.controller;

import com.mindera.api.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/payments")
public class PaymentController extends ExceptionsController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


}
