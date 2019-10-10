package com.michonski.football.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.michonski.football.dto.InfoDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        InfoDto errorInfo = InfoDto.builder().error(e.getMessage()).build();
        String json = new ObjectMapper().writeValueAsString(errorInfo);
        httpServletResponse.getWriter().write( json );
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();

    }
}
