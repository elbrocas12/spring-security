package com.galaxy.trabajofinal.security.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class GlobalSecurityFilter {
    private final SecurityTokenFilter securityTokenFilter;
    private final SpecialAuthorizationFilter specialAuthorizationFilter;
    private final String[] PUBLIC={
        "/api/v1/auth/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(
                authz-> authz.requestMatchers(PUBLIC).permitAll()
        );
        http.authorizeHttpRequests(
                authz-> authz.requestMatchers("/api/v1/archive/**").hasAnyRole("ADMIN","USER")
                        .anyRequest()
                        .authenticated()
        );
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(securityTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(specialAuthorizationFilter, SecurityTokenFilter.class);
        return http.build();
    }
    @Bean
    AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }
}
