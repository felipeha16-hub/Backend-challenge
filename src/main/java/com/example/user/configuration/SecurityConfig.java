package com.example.user.configuration;


import com.example.user.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final HandlerExceptionResolver resolver;


    public SecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
       return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users/login", "/api/v1/users/register","/api/v1/users/login",
                                "/api/v1/users/register",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**,","https://takehomechallenge-d883931b8e0b.herokuapp.com").permitAll()
                        // Allow access to heroku URL where APP was deployed
                        .requestMatchers("https://takehomechallenge-d883931b8e0b.herokuapp.com").permitAll()
                        .anyRequest().authenticated()
                )


               // Exception management of security, if the token is invalid or expired, return a 401 with a message
               .exceptionHandling(exception -> exception
                       .authenticationEntryPoint((request, response, authException) -> {
                           resolver.resolveException(request, response, null, authException);
                       })
               )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
