package com.mindera.api.service;

import com.mindera.api.domain.User;
import com.mindera.api.exception.UserDoesNotExistsException;
import com.mindera.api.model.UserDTO;
import com.mindera.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginUserService extends UserService {

    public LoginUserService(UserRepository userRepository) {
        super(userRepository);
    }

    public UserDTO getUserLogin(String authorization) {
        String[] parts = getAuthorizationParts(authorization);
        User user = getUserRepository().findByUsernameAndPassword(parts[0], parts[1]).orElseThrow(UserDoesNotExistsException::new);
        log.info("User logged in");
        return new UserDTO(user);
    }
}
