package com.autobots.automanager.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;

public class JwtTokenFilter extends BasicAuthenticationFilter {

    private JwtTokenService tokenService;
    private UserDetailsServiceImpl userServiceImpl;

    public JwtTokenFilter(
        AuthenticationManager authenticationManager,
        JwtTokenService tokenService,
        UserDetailsServiceImpl userServiceImpl
    ) {
        super(authenticationManager);
        this.tokenService = tokenService;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        if (tokenService.validateToken(token)) {
            String username = tokenService.getUsername(token);
            UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
