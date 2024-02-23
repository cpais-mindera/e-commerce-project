package com.mindera.api.controller;

import com.mindera.api.model.UserDTO;
import com.mindera.api.service.LoginUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("/users")
public class LoginUserController extends BaseController {

    private LoginUserService loginUserService;

    public LoginUserController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    // Postman
    @GetMapping("/login")
    public ResponseEntity<UserDTO> getUserLogin(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok().body(loginUserService.getUserLogin(authorization));
    }
}
