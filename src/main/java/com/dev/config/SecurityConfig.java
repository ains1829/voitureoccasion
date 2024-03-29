package com.dev.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception{

        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/statistic/**").permitAll() // gerer statistique
            .requestMatchers("/signinlogin/**").permitAll() // gerer categorie
            .requestMatchers("/api/user/**").permitAll() // pour login et inscription
            .requestMatchers("/api/adminmir/**").permitAll() // admin pour gestion back offic
            .requestMatchers("/categorie/**").permitAll()
            .requestMatchers("/model/**").permitAll()
            .requestMatchers("/marque/**").permitAll()
            .requestMatchers("/transmission/**").permitAll()
            .requestMatchers("/signinlogin/**").permitAll()
            .anyRequest() //Toute autre 
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
