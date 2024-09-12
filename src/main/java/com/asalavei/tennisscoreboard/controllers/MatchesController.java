package com.asalavei.tennisscoreboard.controllers;

import com.asalavei.tennisscoreboard.services.FinishedMatchesPersistenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null) {
            request.setAttribute("matches", finishedMatchesPersistenceService.findAllByPlayerName(playerName));
        } else {
            request.setAttribute("matches", finishedMatchesPersistenceService.findAll());
        }

        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }
}
