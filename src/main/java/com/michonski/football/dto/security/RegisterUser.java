package com.michonski.football.dto.security;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUser {

    String username;
    String password;
    String passwordConfirmation;

}
