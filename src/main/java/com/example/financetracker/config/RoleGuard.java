package com.example.financetracker.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class RoleGuard implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String role = request.getHeader("X-Role");
        String method = request.getMethod();

        if (role == null || role.isBlank()) {
            response.setStatus(400);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"X-Role header is required\"}");
            return;
        }

        switch (role.toUpperCase()) {
            case "VIEWER":
                if (!method.equalsIgnoreCase("GET")) {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"VIEWER can only view data\"}");
                    return;
                }
                break;
            case "ANALYST":
                if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("DELETE")) {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"ANALYST cannot create or delete\"}");
                    return;
                }
                break;
            case "ADMIN":
                break;
            default:
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid role\"}");
                return;
        }

        chain.doFilter(req, res);
    }
}