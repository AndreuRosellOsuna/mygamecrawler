package com.mycrawler.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetails() {
        return new InMemoryUserDetailsManager(List.of(
            User.withUsername("admin").password("{noop}admin").roles("ADMIN").build(),
            User.withUsername("andreu").password("{noop}andreu").roles("USER").build(),
            User.withUsername("mario").password("{noop}mario").roles("USER").build()
        ));
    }
}
