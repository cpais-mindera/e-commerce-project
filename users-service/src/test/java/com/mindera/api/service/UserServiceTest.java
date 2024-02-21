package com.mindera.api.service;

import com.mindera.api.Accounts;
import com.mindera.api.domain.Role;
import com.mindera.api.domain.User;
import com.mindera.api.exception.InvalidHeaderAuthorizationException;
import com.mindera.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }
    /**
     * CHANGE ALL TESTS
     * #TODO
     */

    @Test
    void getUserById_AdminUser_Success() {
        // Arrange
        var adminUser = Accounts.ADMIN_ACTIVE_MALE_ACCOUNT.getUser();

        String userNamePasswordAuthentication = adminUser.getUsername() + ":" + adminUser.getPassword();
        var credentials = Base64.getEncoder().encodeToString(userNamePasswordAuthentication.getBytes());
        String authentication = "Basic " + credentials;

        when(userRepository.findById(adminUser.getId())).thenReturn(Optional.of(adminUser));
        when(userRepository.findByUsernameAndPassword(adminUser.getUsername(), adminUser.getPassword())).thenReturn(Optional.of(adminUser));

        // Act
        User actualUser = userService.getUserById(authentication, adminUser.getId());

        // Assert
        assertEquals(adminUser, actualUser);
    }

}