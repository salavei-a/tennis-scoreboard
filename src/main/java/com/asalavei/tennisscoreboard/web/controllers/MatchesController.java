package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.services.FinishedMatchesPersistenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    private final FinishedMatchesPersistenceService service = new FinishedMatchesPersistenceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PlayerRequestDto playerRequestDto = PlayerRequestDto.builder()
                .name(request.getParameter("filter_by_player_name"))
                .build();

        String playerName = playerRequestDto.getName();
        String page = request.getParameter("page");

        if (playerName != null) {
            request.setAttribute("totalPages", service.countTotalPagesByPlayerName(playerName, DEFAULT_SIZE));
            request.setAttribute("matches", service.findAllByPlayerName(
                    playerName,
                    page != null ? Integer.parseInt(request.getParameter("page")) : DEFAULT_PAGE,
                    DEFAULT_SIZE));
        } else {
            request.setAttribute("totalPages", service.countTotalPages(DEFAULT_SIZE));
            request.setAttribute("matches", service.findAll(
                    page != null ? Integer.parseInt(request.getParameter("page")) : DEFAULT_PAGE,
                    DEFAULT_SIZE));
        }

        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }
}
