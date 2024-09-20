package com.asalavei.tennisscoreboard.web.filters;

import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (ValidationException e) {
            log.warning("Validation error: " + e.getMessage());

            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(e.getPagePath()).forward(request, response);
        }
    }
}
