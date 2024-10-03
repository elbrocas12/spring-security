package com.galaxy.trabajofinal.security.filters;

import com.galaxy.trabajofinal.security.jwt.IJWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Slf4j
@Component
public class SecurityTokenFilter extends OncePerRequestFilter {
    private final IJWTService jWTService;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("SecurityTokenFilter...");
        final String authHeader = request.getHeader("Authorization");
        if (isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);// Token
        log.info("Token {}", jwt);
        final String user = jWTService.getUserNameFromJwtToken(jwt);
        log.info("User {}", user);
        if (!isNull(user) && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(user);

            log.info("userDetails {}", userDetails);

            if (jWTService.validateJwtToken(jwt)) {
                log.info("isTokenValid");
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(), userDetails.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(upat);
                SecurityContextHolder.setContext(context);
            } else {
                log.info("is not valid Token");
            }
        }
        filterChain.doFilter(request, response);
    }
}
