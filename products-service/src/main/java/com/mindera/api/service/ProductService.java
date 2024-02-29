package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.Category;
import com.mindera.api.enums.ProductStatus;
import com.mindera.api.exception.*;
import com.mindera.api.message.UserRequestAndReceive;
import com.mindera.api.model.ProductDTO;
import com.mindera.api.model.ProductSimplifiedDTO;
import com.mindera.api.enums.Role;
import com.mindera.api.model.UserDTO;
import com.mindera.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRequestAndReceive userRequestAndReceive;

    @Cacheable("products")
    public ProductDTO getProduct(UUID uuid) {
        Product product = getProductDatabase(uuid);
        return new ProductDTO(product);
    }

    @Cacheable("products")
    public List<ProductSimplifiedDTO> getAllProductsSimplified(String category) {
        if (category != null) {
            List<Product> products = productRepository.findAllByCategory(Category.valueOf(category));
            return products.stream().map(ProductSimplifiedDTO::new).collect(Collectors.toList());
        }
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductSimplifiedDTO::new).collect(Collectors.toList());
    }

    @CacheEvict("products")
    public ProductDTO addProduct(String authorization, Product product) {
        if (isAdminUser(authorization)) {
            try {
                product.setProductStatus(ProductStatus.ACTIVE);
                productRepository.save(product);
                return new ProductDTO(product);
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

    @CacheEvict("products")
    public ProductDTO updateProduct(String authorization, Product product, UUID uuid) {
        if (isAdminUser(authorization)) {
            try {
                getProduct(uuid);
                Product updatedProduct = Product.builder()
                                .id(uuid)
                        .description(product.getDescription())
                        .defaultPrice(product.getDefaultPrice())
                        .name(product.getName())
                        .colour(product.getColour())
                        .size(product.getSize())
                        .stock(product.getStock())
                        .category(product.getCategory())
                        .build();

                productRepository.save(updatedProduct);
                return new ProductDTO(updatedProduct);
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

    @CacheEvict("products")
    public ProductDTO patchProduct(String authorization, Product product, UUID uuid) {
        if (isAdminUser(authorization)) {
            try {
                Product productDatabase = getProductDatabase(uuid);

                var productPatched = Product.patchProduct(product, productDatabase);

                productRepository.save(productPatched);

                return new ProductDTO(productPatched);
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

    @CacheEvict("products")
    public ProductDTO disableProduct(String authorization, UUID uuid) {
        if (isAdminUser(authorization)) {
            try {
                Product productDatabase = getProductDatabase(uuid);
                productDatabase.setProductStatus(ProductStatus.INACTIVE);
                productRepository.save(productDatabase);

                return new ProductDTO(productDatabase);
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

    private Product getProductDatabase(UUID uuid) {
        return productRepository.findById(uuid).orElseThrow(() -> new ProductDoesNotExistsException(uuid));
    }

    private boolean isAdminUser(String authorization) {
        try {
            var test = userRequestAndReceive.sendRequestAndReceiveResponse(authorization);
            return test.getRole().equals(Role.ADMIN);
        } catch (Exception ex) {
            throw new UserInternalServerErrorException();
        }
    }
}
