package com.mindera.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mindera.api.domain.Contacts;
import com.mindera.api.domain.Role;
import com.mindera.api.domain.User;
import com.mindera.api.enums.Gender;
import com.mindera.api.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;
    private String username;
    private String vatNumber;
    private Integer age;
    private Gender gender;
    private Contacts contacts;
    private Role role;
    private UserStatus status;

    public UserDTO(User user) {
        this.id = user.getId();
        this.age = user.getAge();
        this.role = user.getRole();
        this.contacts = user.getContacts();
        this.gender = user.getGender();
        this.status = user.getStatus();
        this.username = user.getUsername();
        this.vatNumber = user.getVatNumber();
    }

    public static List<UserDTO> mapUserListToUserDTOList(List<User> userList) {
        return userList.stream().map(UserDTO::new).toList();
    }
}
