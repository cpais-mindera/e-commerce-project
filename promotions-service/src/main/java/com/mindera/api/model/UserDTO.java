package com.mindera.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindera.api.enums.Role;
import com.mindera.api.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String username;
    private String vatNumber;
    private Role role;
    private UserStatus status;
}
