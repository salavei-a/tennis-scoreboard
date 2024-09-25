package com.asalavei.tennisscoreboard.web.filters;

import com.asalavei.tennisscoreboard.exceptions.DatabaseOperationException;
import com.asalavei.tennisscoreboard.exceptions.ForbiddenException;
import com.asalavei.tennisscoreboard.exceptions.NotFoundException;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.logging.Level;

import static jakarta.servlet.http.HttpServletResponse.*;

@Log
@WebFilter("/*")
public class ExceptionHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (ValidationException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
        } catch (NotFoundException e) {
            log.info("Not found: " + e.getMessage());
            response.sendError(SC_NOT_FOUND);
        } catch (ForbiddenException e) {
            log.info("Access denied: " + e.getMessage());
            response.sendError(SC_FORBIDDEN);
        } catch (DatabaseOperationException e) {
            log.log(Level.WARNING, "Database operation failed", e);
            response.sendError(SC_INTERNAL_SERVER_ERROR);
        }
    }
}
