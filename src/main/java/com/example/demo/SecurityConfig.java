package com.example.demo;

import com.example.demo.filter.JwtSecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtSecurityFilter jwtSecurityFilter;

    public SecurityConfig(JwtSecurityFilter jwtSecurityFilter) {
        this.jwtSecurityFilter = jwtSecurityFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers("/auth").permitAll()
//                .anyRequest().authenticated()
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
