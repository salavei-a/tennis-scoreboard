package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.validation.scenario.DtoValidator;
import com.asalavei.tennisscoreboard.validation.scenario.FindByName;
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
        int page = parsePageParam(request.getParameter("page"));

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null) {
            PlayerRequestDto player = PlayerRequestDto.builder()
                    .name(playerName)
                    .build();

            DtoValidator.validate(player, FindByName.class, "matches.jsp");

            setMatchesAttributesByPlayer(request, playerName, page);
        } else {
            setMatchesAttributes(request, page);
        }

        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }

    private int parsePageParam(String pageParam) {
        try {
            return pageParam != null ? Integer.parseInt(pageParam) : DEFAULT_PAGE;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE;
        }
    }

    private void setMatchesAttributes(HttpServletRequest request, int page) {
        request.setAttribute("totalPages", service.countTotalPages(DEFAULT_SIZE));
        request.setAttribute("matches", service.findAll(page, DEFAULT_SIZE));
    }

    private void setMatchesAttributesByPlayer(HttpServletRequest request, String playerName, int page) {
        request.setAttribute("totalPages", service.countTotalPagesByPlayerName(playerName, DEFAULT_SIZE));
        request.setAttribute("matches", service.findAllByPlayerName(playerName, page, DEFAULT_SIZE));
    }
}
