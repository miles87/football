package com.michonski.football.controller;

import com.michonski.football.dto.InfoDto;
import com.michonski.football.dto.security.RegisterUser;
import com.michonski.football.security.TokenManager;
import com.michonski.football.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    // metoda /register ktora przyjmuje RegisterUser i przekazuje do
    // przygotowanej metody service
    @PostMapping("/register")
    public InfoDto register(@RequestBody RegisterUser registerUser ) {
        return InfoDto
                .builder()
                .data(securityService.registerUserName(registerUser))
                .build();
    }

    @PostMapping("/refresh")
    public InfoDto refresh(@RequestParam("token") String refreshToken) {
        return InfoDto
                .builder()
                .data(TokenManager.parseRefreshToken(refreshToken))
                .build();
    }
}
