package com.mindera.api.controller;

import com.mindera.api.Accounts;
import com.mindera.api.domain.User;
import com.mindera.api.enums.UserStatus;
import com.mindera.api.exception.*;
import com.mindera.api.model.Error;
import com.mindera.api.repository.UserRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int PORT;

    @MockBean
    UserRepository userRepository;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String BASE_URL;
    private final String USERS = "/users";
    private final String ID_2 = "/2";

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + PORT;
    }

    /*************** GET /users/{id} ***************/

    @Test
    void getUserById_WithAdminPermissions_ShouldReturnUser() {
        // Arrange
        User adminUser = Accounts.ADMIN_ACTIVE_MALE_ACCOUNT.getUser();

        User customer1 = Accounts.CUSTOMER_ACTIVE_MALE_ACCOUNT.getUser();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(customer1));
        when(userRepository.findByUsernameAndPassword(adminUser.getUsername(), adminUser.getPassword()))
                .thenReturn(Optional.of(adminUser));
        var entity = getHeaderAuthorization(adminUser);
        var headers = new HttpEntity<>(entity);

        // Act
        ResponseEntity<User> responseEntity = restTemplate.exchange(BASE_URL + USERS + ID_2,
                HttpMethod.GET,
                headers, User.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        User response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.OK, status);
        assert response != null;
        assertEquals(customer1.toString(), response.toString());
    }

    @Test
    @ExceptionHandler(UserDoesNotExistsException.class)
    void getUserById_WithAdminPermissions_ShouldReturnUserDoesNotExistsException() {
        // Arrange
        User adminUser = Accounts.ADMIN_ACTIVE_MALE_ACCOUNT.getUser();

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());
        when(userRepository.findByUsernameAndPassword(adminUser.getUsername(), adminUser.getPassword()))
                .thenReturn(Optional.of(adminUser));

        var entity = getHeaderAuthorization(adminUser);
        var headers = new HttpEntity<>(entity);

        Error error = new Error();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        UserDoesNotExistsException ex = new UserDoesNotExistsException(2L);
        error.setMessage(ex.getMessage());

        // Act
        ResponseEntity<Error> responseEntity = restTemplate.exchange(BASE_URL + USERS + ID_2,
                HttpMethod.GET,
                headers, Error.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        Error response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, status);
        assert response != null;
        assertEquals(error.toString(), response.toString());
    }

    @Test
    @ExceptionHandler(UserDoesNotHavePermissionsException.class)
    void getUserById_WithNonAdminPermissions_ShouldReturnUserDoesNotHavePermissionsException() {
        // Arrange
        User customerUser3 = Accounts.CUSTOMER_ACTIVE_FEMALE_ACCOUNT.getUser();

        User customerUser2 = Accounts.CUSTOMER_ACTIVE_MALE_ACCOUNT.getUser();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(customerUser2));
        when(userRepository.findByUsernameAndPassword(customerUser3.getUsername(), customerUser3.getPassword()))
                .thenReturn(Optional.of(customerUser3));

        var entity = getHeaderAuthorization(customerUser3);
        var headers = new HttpEntity<>(entity);

        Error error = new Error();
        error.setErrorCode(HttpStatus.UNAUTHORIZED.value());
        UserDoesNotHavePermissionsException ex = new UserDoesNotHavePermissionsException();
        error.setMessage(ex.getMessage());

        // Act
        ResponseEntity<Error> responseEntity = restTemplate.exchange(BASE_URL + USERS + ID_2,
                HttpMethod.GET,
                headers, Error.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        Error response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, status);
        assert response != null;
        assertEquals(error.toString(), response.toString());
    }

    @Test
    @ExceptionHandler(InvalidHeaderAuthorizationException.class)
    void getUserById_WithInvalidAdminAuthorization_ShouldReturnInvalidHeaderAuthorizationException() {
        // Arrange
        User customer = Accounts.ADMIN_ACTIVE_MALE_ACCOUNT.getUser();

        when(userRepository.findByUsernameAndPassword(customer.getUsername(), customer.getPassword()))
                .thenReturn(Optional.empty());

        var entity = getHeaderAuthorization(customer);
        var headers = new HttpEntity<>(entity);

        Error error = new Error();
        error.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
        InvalidHeaderAuthorizationException ex = new InvalidHeaderAuthorizationException();
        error.setMessage(ex.getMessage());

        // Act
        ResponseEntity<Error> responseEntity = restTemplate.exchange(BASE_URL + USERS + ID_2,
                HttpMethod.GET,
                headers, Error.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        Error response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.NOT_ACCEPTABLE, status);
        assert response != null;
        assertEquals(error.toString(), response.toString());
    }

    /*************** POST /users ***************/
    @Test
    void addUser_WithCustomerRole_ShouldReturnUser() {
        // Arrange
        User user = Accounts.CUSTOMER_WITHOUT_STATUS_MALE_ACCOUNT.getUser();

        when(userRepository.save(any())).thenReturn(null);

        // Act
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(BASE_URL + USERS, user, User.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        User response = responseEntity.getBody();
        user.setStatus(UserStatus.ACTIVE);

        // Assert
        assertEquals(HttpStatus.CREATED, status);
        assert response != null;
        assertEquals(user.toString(), response.toString());
    }

    @Test
    void addUser_WithCustomerRole_ShouldReturnUserAlreadyExistsWithVatNumberException() {
        // Arrange
        User user = Accounts.CUSTOMER_WITHOUT_STATUS_MALE_ACCOUNT.getUser();

        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Constraint violation",
                        new ConstraintViolationException("User", null, "users_vatNumber_key")));

        Error error = new Error();
        error.setErrorCode(HttpStatus.CONFLICT.value());
        UserDuplicateException ex = new UserDuplicateException("vatNumber");
        error.setMessage(ex.getMessage());

        // Act
        ResponseEntity<Error> responseEntity = restTemplate.postForEntity(BASE_URL + USERS, user, Error.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        Error response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.CONFLICT, status);
        assert response != null;
        assertEquals(error.toString(), response.toString());
    }

    @Test
    @ExceptionHandler(UserNotNullPropertyException.class)
    void addUser_WithCustomerRole_ShouldReturnUserDoesNotHaveVatNumbException() {
        // Arrange
        User user = Accounts.CUSTOMER_WITHOUT_VAT_NUMBER_MALE_ACCOUNT.getUser();

        when(userRepository.save(any(User.class)))
                .thenThrow(new DataIntegrityViolationException("Property value exception",
                        new PropertyValueException("User", null, "vatNumber")));

        Error error = new Error();
        error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
        UserNotNullPropertyException ex = new UserNotNullPropertyException("vatNumber");
        error.setMessage(ex.getMessage());

        // Act
        ResponseEntity<Error> responseEntity = restTemplate.postForEntity(BASE_URL + USERS, user, Error.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        Error response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.PRECONDITION_FAILED, status);
        assert response != null;
        assertEquals(error.toString(), response.toString());
    }

    /*************** PUT /users/{id} ***************/
    @Test
    void updateUserById_WithUserPermissions_ShouldReturnUser() {
        // Arrange
        User customer = Accounts.CUSTOMER_ACTIVE_MALE_ACCOUNT.getUser();

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(customer));
        when(userRepository.findByUsernameAndPassword(customer.getUsername(), customer.getPassword()))
                .thenReturn(Optional.of(customer));
        var entity = getHeaderAuthorization(customer);

        customer.setAge(30);
        when(userRepository.save(any(User.class))).thenReturn(customer);

        HttpEntity<User> requestEntity = new HttpEntity<>(customer, entity);

        // Act
        ResponseEntity<User> responseEntity = restTemplate.exchange(BASE_URL + USERS + ID_2,
                HttpMethod.PUT,
                requestEntity, User.class);

        HttpStatusCode status = responseEntity.getStatusCode();
        User response = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.OK, status);
        assert response != null;
        assertEquals(customer.toString(), response.toString());
    }

    private HttpHeaders getHeaderAuthorization(User user) {
        HttpHeaders headers = new HttpHeaders();
        String credentials = user.getUsername() + ":" + user.getPassword();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.add("Authorization", "Basic " + encodedCredentials);
        return headers;
    }
}