package com.example.demo;

import com.example.demo.filter.JwtDecodeFilter;
import com.example.demo.filter.JwtLoginFilter;
import com.example.demo.service.UserSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtDecodeFilter jwtDecodeFilter;
    private final UserSecurityService userSecurityService;

    public SecurityConfig(JwtDecodeFilter jwtDecodeFilter, UserSecurityService userSecurityService) {
        this.jwtDecodeFilter = jwtDecodeFilter;
        this.userSecurityService = userSecurityService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userSecurityService);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter();
        jwtLoginFilter.setAuthenticationManager(authenticationManager);
        jwtLoginFilter.setUsernameParameter("id");
        jwtLoginFilter.setPasswordParameter("password"); // default

        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers("/auth").permitAll()
//                .anyRequest().authenticated()
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtDecodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
