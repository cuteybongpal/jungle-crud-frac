package com.example.frac;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(requestmatcher ->
                        requestmatcher.requestMatchers("/","/js/**", "/imgs/**", "/account/**", "/articles/**").permitAll())
                .formLogin(login -> login
                        .loginPage("/account/signin")
                        .loginProcessingUrl("/account/signin")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/account/signin?error")
                        .permitAll()
                )
                .logout(logout-> logout.permitAll());
        return http.build();
    }

}
