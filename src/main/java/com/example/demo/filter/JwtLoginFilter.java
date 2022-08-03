package com.example.demo.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        try {
            User user = (User) authResult.getPrincipal();
            String username = user.getUsername();
            Algorithm algorithm = Algorithm.HMAC256("ice2022");
            String accessToken = JWT.create()
                    .withIssuer("inti")
                    .withSubject(username)
                    .sign(algorithm);
            System.out.println("Create JWT: user_id=" + username);
            response.addCookie(new Cookie("access_token", accessToken));
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
    }
}
