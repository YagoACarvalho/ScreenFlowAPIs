package com.CTRLTELA.CtrlTela.common.jwtFlow;

import com.CTRLTELA.CtrlTela.common.login.AuthDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            var principal = jwtService.parseAndValidate(token);

            if ("DEVICE".equalsIgnoreCase(principal.role())) {
                var authorities = List.of(new SimpleGrantedAuthority("ROLE_DEVICE"));

                var authentication = new UsernamePasswordAuthenticationToken(
                        principal.subject(), // ex: device:<uuid>
                        null,
                        authorities
                );
                authentication.setDetails(
                        new AuthDetails(
                                principal.tenantId(),
                                principal.role(),
                                principal.deviceId(),
                                principal.screenId()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                var userDetails = userDetailsService.loadUserByUsername(principal.subject()); // aqui subject=email

                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(
                        new AuthDetails(
                                principal.tenantId(),
                                principal.role(),
                                principal.deviceId(),
                                principal.screenId()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }
}

