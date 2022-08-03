package com.example.demo.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entity.User;
import com.example.demo.service.UserSecurityService;
import org.springframework.core.env.Environment;
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
    private final UserSecurityService userSecurityService;
    private final Environment env;

    public JwtDecodeFilter(UserSecurityService userSecurityService, Environment env) {
        this.userSecurityService = userSecurityService;
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String accessToken = header.substring(7);
                Algorithm algorithm = Algorithm.HMAC256(env.getProperty("jwt.secret"));
                JWTVerifier verifier = JWT.require(algorithm).withIssuer(env.getProperty("jwt.issuer")).build();
                DecodedJWT jwt = verifier.verify(accessToken);
                String username = jwt.getSubject();
                System.out.println("Verify JWT: user_id=" + username);
                User user = (User) userSecurityService.loadUserByUsername(username);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(user, null);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JWTVerificationException exception) {
                exception.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}
