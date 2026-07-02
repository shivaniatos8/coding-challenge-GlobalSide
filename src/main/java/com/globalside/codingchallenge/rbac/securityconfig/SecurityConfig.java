package com.globalside.codingchallenge.rbac.securityconfig;


import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    /**
     * Configures Spring Security for the Product API.
     * RBAC Rules:
     * USER:Read-Only access (GET endpoints)
     * ADMIN: Full CRUD access (GET,PUT,POST,DELETE)
     * Authentication is implemented using Spring Security Basic Authentication
     * with in-memory user storage
     */

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests(
                        auth -> auth

                                //Allow both USER and ADMIN to access the products
                                .requestMatchers(HttpMethod.GET,"/products/**")
                                .hasAnyRole("USER","ADMIN")

                                //Only ADMIN can create products
                                .requestMatchers(HttpMethod.POST,"/products/**")
                                .hasRole("ADMIN")

                                //Only ADMIN can update products
                                .requestMatchers(HttpMethod.PUT,"/products/**")
                                .hasRole("ADMIN")

                                //Only ADMIN can delete products
                                .requestMatchers(HttpMethod.DELETE,"/products/**")
                                .hasRole("ADMIN")

                                //Any other request requires authentication
                                .anyRequest().authenticated())

                //Enable HTTP basic authentication
                .httpBasic(httpBasic -> {});
        return http.build();
    }

    /**
     * In-memory users for demonstration only
     * Credentials are stored locally and used by Spring Security
     * for authentication during testing
     */
    @Bean public UserDetailsService userDetailsService()
    {
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }

    /**
     * password encoder to securely store passwords
     */
    @Bean public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}