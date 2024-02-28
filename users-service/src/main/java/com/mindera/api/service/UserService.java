package com.mindera.api.service;

import com.mindera.api.domain.Role;
import com.mindera.api.domain.User;
import com.mindera.api.enums.Gender;
import com.mindera.api.enums.UserStatus;
import com.mindera.api.exception.*;
import com.mindera.api.model.UserDTO;
import com.mindera.api.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

import static com.mindera.api.model.UserDTO.mapUserListToUserDTOList;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class UserService {

    private final UserRepository userRepository;

    @Cacheable("users")
    public UserDTO getUserById(String authorization, Long id) {
        if (isAdminUser(authorization)) {
            User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
            return new UserDTO(user);
        }
        throw new UserDoesNotHavePermissionsException();
    }

    @Cacheable("users")
    public List<UserDTO> getAllUsers(String authorization, String gender) {
        if (isAdminUser(authorization)) {
            if (gender != null) {
                log.info("Getting All Users By Gender: ", gender);
                List<User> userList = userRepository.findAllByGender(Gender.valueOf(gender));
                return mapUserListToUserDTOList(userList);
            }
            log.info("Getting All Users");
            return mapUserListToUserDTOList(userRepository.findAll());
        }
        throw new UserDoesNotHavePermissionsException();
    }

    @CacheEvict("users")
    public UserDTO addUser(User user) {
        try {
            user.setStatus(UserStatus.ACTIVE);
            log.info("Create new User: ", user.toString());
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.error("Failed to store in database");
            throwDataIntegrityException(ex);
        }
        return new UserDTO(user);
    }

    @CacheEvict("users")
    public UserDTO updateUser(String authorization, Long id, User user) {
        boolean sameUser = isSameAuthorizationUser(authorization, id);
        if (sameUser) {
            log.info("Updating User details");
            var updateUser = User.builder()
                    .id(id)
                    .username(user.getUsername())
                    .vatNumber(user.getVatNumber())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .contacts(user.getContacts())
                    .paymentMethods(user.getPaymentMethods())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .status(UserStatus.ACTIVE)
                    .build();
            try {
                userRepository.save(updateUser);
                return new UserDTO(updateUser);
            } catch (DataIntegrityViolationException ex) {
                log.error("Failed to store in database", ex.getMessage());
                throwDataIntegrityException(ex);
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    @CacheEvict("users")
    public UserDTO patchUser(String authorization, Long id, User user) {
        boolean sameUser = isSameAuthorizationUser(authorization, id);
        if (sameUser) {
            User userDatabase = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
            userDatabase = User.patchUser(user, userDatabase);

            try {
                userRepository.save(userDatabase);
                return new UserDTO(userDatabase);
            } catch (DataIntegrityViolationException ex) {
                throwDataIntegrityException(ex);
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    @CacheEvict("users")
    public UserDTO disableUser(String authorization, Long id) {
        boolean sameUser = isSameAuthorizationUser(authorization, id);
        if (sameUser) {
            User userDatabase = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
            userDatabase.setStatus(UserStatus.INACTIVE);
            log.info("User switch to INACTIVE", id);
            try {
                userRepository.save(userDatabase);
                return new UserDTO(userDatabase);
            } catch (DataIntegrityViolationException ex) {
                throwDataIntegrityException(ex);
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    @CacheEvict("users")
    public UserDTO enableUser(String authorization, Long id) {
        boolean sameUser = isSameAuthorizationUser(authorization, id);
        if (sameUser) {
            User userDatabase = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
            userDatabase.setStatus(UserStatus.ACTIVE);
            log.info("User switch to ACTIVE", id);

            try {
                userRepository.save(userDatabase);
                return new UserDTO(userDatabase);
            } catch (DataIntegrityViolationException ex) {
                throwDataIntegrityException(ex);
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    private void throwDataIntegrityException(DataIntegrityViolationException ex) {
        switch(ex.getCause()) {
            case ConstraintViolationException constraintEx -> {
                String constraintName = constraintEx.getConstraintName();
                throw new UserDuplicateException(constraintName);
            }
            case PropertyValueException propertyEx -> {
                String nullableValue = propertyEx.getPropertyName();
                throw new UserNotNullPropertyException(nullableValue);
            }
            default -> throw ex;
        }
    }

    private boolean isSameAuthorizationUser(String authorization, Long id) {
        String base64Credentials = authorization.substring(6).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] parts = credentials.split(":");
        String username = parts[0];
        String password = parts[1];
        User userByUsernameAndPassword = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(InvalidHeaderAuthorizationException::new);
        return userByUsernameAndPassword.getId().equals(id);
    }
    
    private boolean isAdminUser(String authorization) {
        String[] result = getAuthorizationParts(authorization);

        return userRepository.findByUsernameAndPassword(result[0], result[1])
                .orElseThrow(InvalidHeaderAuthorizationException::new)
                .getRole().equals(Role.ADMIN);
    }

    protected String[] getAuthorizationParts(String authorization) {
        String base64Credentials = authorization.substring(6).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":");
    }
}
