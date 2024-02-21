package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.Category;
import com.mindera.api.exception.*;
import com.mindera.api.model.Role;
import com.mindera.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;

    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid).orElseThrow(() -> new ProductDoesNotExistsException(uuid));
    }

    public List<Product> getAllProductsSimplified(String category) {
        if (category != null) {
            return productRepository.findAllByCategory(Category.valueOf(category));
        }
        return productRepository.findAll();
    }

    public Product addProduct(String authorization, Product product) {
        if (isAdminUser(authorization)) {
            try {
                productRepository.save(product);
                return product;
            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new ProductDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new ProductNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public Product updateProduct(String authorization, Product product, UUID uuid) {
        if (isAdminUser(authorization)) {
            try {
                getProduct(uuid);
                Product updatedProduct = Product.builder()
                                .id(uuid)
                        .description(product.getDescription())
                        .defaultPrice(product.getDefaultPrice())
                        .discountedPrice(product.getDiscountedPrice())
                        .name(product.getName())
                        .colour(product.getColour())
                        .size(product.getSize())
                        .stock(product.getStock())
                        .category(product.getCategory())
                        .build();

                productRepository.save(updatedProduct);
                return updatedProduct;
            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new ProductDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new ProductNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }
    
    public Product patchProduct(String authorization, Product product, UUID uuid) {
        if (isAdminUser(authorization)) {
            try {
                Product productDatabase = getProduct(uuid);

                var productPatched = Product.patchProduct(product, productDatabase);

                productRepository.save(productPatched);

                return productDatabase;
            } catch (DataIntegrityViolationException ex) {
                switch(ex.getCause()) {
                    case ConstraintViolationException constraintEx -> {
                        String constraintName = constraintEx.getConstraintName();
                        throw new ProductDuplicateException(constraintName);
                    }
                    case PropertyValueException propertyEx -> {
                        String nullableValue = propertyEx.getPropertyName();
                        throw new ProductNotNullPropertyException(nullableValue);
                    }
                    default -> throw ex;
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    private boolean isAdminUser(String authorization) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", authorization);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Role> role = restTemplate.exchange("http://localhost:8081/users/role", HttpMethod.GET, httpEntity, Role.class);
        if (role.getBody() == null) {
            throw new UserInternalServerErrorException();
        }
        return role.getBody().equals(Role.ADMIN);
    }
}
