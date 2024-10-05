package com.galaxy.trabajofinal.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Component
public class SpecialAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("SpecialAuthorizationFilter...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            LocalTime currentTime = LocalDateTime.now().toLocalTime();
            LocalTime startAccessTime = LocalTime.of(8, 0);
            LocalTime endAccessTime = LocalTime.of(23, 59);

            if (currentTime.isBefore(startAccessTime) || currentTime.isAfter(endAccessTime)) {
                log.info("Access denied: User tried to access outside of allowed time range.");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access is not allowed at this time.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
