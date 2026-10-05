package com.careerpilot.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RoleBasedLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(
                                authority ->
                                        authority.equals("ROLE_ADMIN")
                        );

        if (isAdmin) {

            response.sendRedirect(
                    "/admin-dashboard.html"
            );

        } else {

            response.sendRedirect(
                    "/dashboard.html"
            );
        }
    }
}