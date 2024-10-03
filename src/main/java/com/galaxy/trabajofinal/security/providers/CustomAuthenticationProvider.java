package com.galaxy.trabajofinal.security.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String userName = authentication.getName();
            String password = authentication.getCredentials().toString();
            //String principal = authentication.getPrincipal().toString();

            log.info("userName => {}", userName);
            log.info("password => {}", password);
            //log.info("principal => {}", principal);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

            if (isNull(userDetails)) {
                throw new BadCredentialsException("El usuario o contraseña son incorrectos");
            }

            log.info("password DB=> {}", userDetails.getPassword());

            if (userDetails.getUsername().equals(userName)
                    && bCryptPasswordEncoder.matches(password,userDetails.getPassword())) { // userDetails.getPassword().equals(password)
                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userName, password,
                        userDetails.getAuthorities());
                return upat;
            }
            throw new BadCredentialsException("El usuario o contraseña son incorrectos");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("El usuario o contraseña son incorrectos");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
