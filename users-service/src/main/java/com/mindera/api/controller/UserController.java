package com.mindera.api.controller;

import com.mindera.api.domain.Role;
import com.mindera.api.domain.User;
import com.mindera.api.service.UserService;
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
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserById(authorization, id));
    }

    // GET_USER_ROLE.mermaid
    @GetMapping("/role")
    public ResponseEntity<Role> getUserRole(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(userService.getUserRole(authorization));
    }

    // GET_ALL_USERS.mermaid
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String authorization, @PathVariable String gender) {
        return ResponseEntity.ok().body(userService.getAllUsers(authorization, gender));
    }

    // CREATE_USER.mermaid
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    // UPDATE_USER.mermaid
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable Long id,
                                           @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(authorization, id, user));
    }

    // PATCH_USER.mermaid
    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Long id,
                                          @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.patchUser(authorization, id, user));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<User> patchUserStatus(@RequestHeader("Authorization") String authorization,
                                          @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.patchUserStatus(authorization, id));
    }

    /*
    // TODO

    // TODO All gets using cache
    // PUT PATCH DELETE Cache evict
    */
}

