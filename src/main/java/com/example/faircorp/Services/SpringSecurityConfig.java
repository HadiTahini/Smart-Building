package com.example.faircorp.Services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // We create a password encoder
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user").password(encoder.encode("user")).roles("USER").build()
        );
        manager.createUser(
                User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN").build()
        );
        return manager;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/login")
                .authorizeRequests(authorize -> authorize.anyRequest().hasAnyRole())
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    public SecurityFilterChain adminUserManagerFilterChain(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/api/**")
                .authorizeRequests(authorize -> authorize.anyRequest().hasAnyRole("ADMIN", "USER","MANAGER"))
                .formLogin(withDefaults())
                .csrf().disable()
                .httpBasic(withDefaults())
                .build();
    }
    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return http
                .antMatcher("/api/security/**")
                .authorizeRequests(authorize -> authorize.anyRequest().hasRole("ADMIN"))
                .formLogin(withDefaults())
                .csrf().disable()
                .httpBasic(withDefaults())
                .build();
    }
}
