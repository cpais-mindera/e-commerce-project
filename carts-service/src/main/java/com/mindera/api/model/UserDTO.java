package com.mindera.api.model;

import lombok.*;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String vatNumber;
    private Contacts contacts;

}
