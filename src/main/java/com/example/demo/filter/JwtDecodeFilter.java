package com.example.demo.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtDecodeFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;

    public JwtDecodeFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String accessToken = header.substring(7);
                Algorithm algorithm = Algorithm.HMAC256("ice2022");
                JWTVerifier verifier = JWT.require(algorithm).withIssuer("int-i").build();
                DecodedJWT jwt = verifier.verify(accessToken);
                String username = jwt.getSubject();
                System.out.println("Verify JWT: user_id=" + username);
                User user = (User) userDetailsService.loadUserByUsername(username);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JWTVerificationException exception) {
                exception.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}
