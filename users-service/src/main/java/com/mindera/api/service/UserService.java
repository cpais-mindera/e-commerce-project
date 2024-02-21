package com.mindera.api.service;

import com.mindera.api.domain.*;
import com.mindera.api.enums.Gender;
import com.mindera.api.enums.UserStatus;
import com.mindera.api.exception.*;
import com.mindera.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    
    public Role getUserRole(String authorization) {
        if (isAdminUser(authorization)) {
            String[] parts = getAuthorizationParts(authorization);
            return userRepository.findByUsernameAndPassword(parts[0], parts[1]).orElseThrow(UserDoesNotExistsException::new).getRole();
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public User getUserLogin(String authorization) {
        String[] parts = getAuthorizationParts(authorization);
        return userRepository.findByUsernameAndPassword(parts[0], parts[1]).orElseThrow(UserDoesNotExistsException::new);
    }

    public User getUserById(String authorization, Long id) {
        if (isAdminUser(authorization)) {
            return userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public List<User> getAllUsers(String authorization, String gender) {
        if (isAdminUser(authorization)) {
            if (gender != null) {
                return userRepository.findAllByGender(Gender.valueOf(gender));
            }
            return userRepository.findAll();
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public User addUser(User user) {
        try {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
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
        return user;
    }

    public User updateUser(String authorization, Long id, User user) {
        boolean sameUser = isSameAuthorizationUser(authorization, user, id);
        if (sameUser) {
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
                    .status(user.getStatus())
                    .build();
            try {
                userRepository.save(updateUser);
            } catch (DataIntegrityViolationException ex) {
                if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
                    String constraintName = constraintEx.getConstraintName();
                    throw new UserDuplicateException(constraintName);
                } else if (ex.getCause() instanceof PropertyValueException propertyEx) {
                    String nullableValue = propertyEx.getPropertyName();
                    throw new UserNotNullPropertyException(nullableValue);
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }

    public User patchUser(String authorization, Long id, User user) {
        boolean sameUser = isSameAuthorizationUser(authorization, user, id);
        if (sameUser) {
            // that throw will never reach because if the user does not exists we will throw error in isSameAuthorizationUser
            User userDatabase = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistsException(id));
            userDatabase = User.patchUser(user, userDatabase);

            try {
                userRepository.save(userDatabase);
            } catch (DataIntegrityViolationException ex) {
                if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
                    String constraintName = constraintEx.getConstraintName();
                    throw new UserDuplicateException(constraintName);
                } else if (ex.getCause() instanceof PropertyValueException propertyEx) {
                    String nullableValue = propertyEx.getPropertyName();
                    throw new UserNotNullPropertyException(nullableValue);
                }
            }
        }
        throw new UserDoesNotHavePermissionsException();
    }



    private boolean isSameAuthorizationUser(String authorization, User user, Long id) {
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

    private String[] getAuthorizationParts(String authorization) {
        String base64Credentials = authorization.substring(6).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        return credentials.split(":");
    }
}
