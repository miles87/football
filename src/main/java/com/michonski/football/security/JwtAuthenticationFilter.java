package com.michonski.football.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michonski.football.dto.InfoDto;
import com.michonski.football.dto.security.AuthenticationUser;
import com.michonski.football.dto.security.Tokens;
import com.michonski.football.exception.AppException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // ten filter reagowal bedzie na zadanie /login POST

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        // jezeli nie pasuje ci zeby logowanie odbywalo sie za pomoca /login
        // to tutaj w konstruktorze mozesz to zmienic
        // this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/my-login", "POST"));
    }

    // kiedy wywola sie zadanie /login wtedy ta metoda uruchomi sie i sprobuje
    // zalogowac usera po credentials ktore przeslesz w JSON body
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {

        try {
            // 1. pobieramy dane uzytkownika ktory chce sie zalogowac
            AuthenticationUser user = new ObjectMapper().readValue(request.getInputStream(), AuthenticationUser.class);

            // 2. dokonujemy proby logowania
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
            ));
        } catch ( Exception e ) {
            throw new AppException(e.getMessage());
        }

    }

    // kiedy uda sie zalogowac usera to zostanie wywolana ta metoda
    // w ktorej wygenerujesz tokens
    // w czwartym argumencie czyli authResult dostaniesz dane o zalogowanym userze
    // kiedy logowania sie powiedzie
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(InfoDto.<Tokens>builder().data(TokenManager.generateTokens(authResult)).build()));
        response.getWriter().flush();
        response.getWriter().close();

    }
}
