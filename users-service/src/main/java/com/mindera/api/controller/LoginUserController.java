package com.mindera.api.controller;

import com.mindera.api.domain.User;
import com.mindera.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public class LoginUserController extends UserController {

    private UserService userService;

    public LoginUserController(UserService userService) {
        super(userService);
    }

    @GetMapping("/login")
    public ResponseEntity<User> getUserLogin(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(userService.getUserLogin(authorization));
    }
}
