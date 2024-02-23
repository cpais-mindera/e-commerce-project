package com.mindera.api.controller;

import com.mindera.api.domain.User;
import com.mindera.api.model.UserDTO;
import com.mindera.api.service.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET_USER_BY_ID.mermaid
    // Postman
    @GetMapping("/{id}")
    @Cacheable(value = "users")
    public ResponseEntity<UserDTO> getUserById(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(authorization, id));
    }

    // GET_ALL_USERS.mermaid
    // Postman
    @GetMapping
    @Cacheable(value = "users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String authorization, @RequestParam(required = false) String gender) {
        return ResponseEntity.ok().body(userService.getAllUsers(authorization, gender));
    }

    // CREATE_USER.mermaid
    // Postman
    @PostMapping
    @CacheEvict(value = "users")
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    // UPDATE_USER.mermaid
    // Postman
    @PutMapping("/{id}")
    @CacheEvict(value = "users")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable Long id,
                                           @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(authorization, id, user));
    }

    // PATCH_USER.mermaid
    // Postman
    @PatchMapping("/{id}")
    @CachePut(value = "users")
    public ResponseEntity<UserDTO> patchUser(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Long id,
                                          @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.patchUser(authorization, id, user));
    }

    // Postman
    @PatchMapping("/{id}/disable")
    @CachePut(value = "users")
    public ResponseEntity<UserDTO> disableUser(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.disableUser(authorization, id));
    }

    // Postman
    @PatchMapping("/{id}/enable")
    @CachePut(value = "users")
    public ResponseEntity<UserDTO> enableUser(@RequestHeader("Authorization") String authorization,
                                                   @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.enableUser(authorization, id));
    }
}

