package com.mindera.api.message;

import com.mindera.api.domain.Product;
import com.mindera.api.exception.ProductDoesNotExistsException;
import com.mindera.api.model.ProductDTO;
import com.mindera.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductsMessageReceiver {

    private final ProductRepository productRepository;

    @RabbitListener(queues = "PRODUCT_LISTENER")
    public ProductDTO receiveRequest(@Payload ProductMessage message) {
        Product product = productRepository.findById(message.getProductId()).orElseThrow(() -> new ProductDoesNotExistsException(message.getProductId()));
        log.info("Product Message reply: ", product.toString());
        return new ProductDTO(product);
    }
}
