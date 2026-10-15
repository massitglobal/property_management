package com.faisal.backend.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {

    private String username;
    private String passwordHash;
    private String email;


}
