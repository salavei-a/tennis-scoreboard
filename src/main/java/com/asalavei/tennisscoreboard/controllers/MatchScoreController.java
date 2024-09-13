package com.asalavei.tennisscoreboard.controllers;

import com.asalavei.tennisscoreboard.dto.Match;
import com.asalavei.tennisscoreboard.services.FinishedMatchesPersistenceService;
import com.asalavei.tennisscoreboard.services.MatchScoreCalculationService;
import com.asalavei.tennisscoreboard.services.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuidParameter = request.getParameter("uuid");

        if (StringUtils.isBlank(uuidParameter)) {
            response.sendRedirect("/");
            return;
        }

        UUID uuid = UUID.fromString(uuidParameter);

        request.setAttribute("match", ongoingMatchesService.getOngoingMatch(uuid));
        request.setAttribute("uuid", uuid);
        request.getRequestDispatcher("match-score.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));

        Match match = ongoingMatchesService.getOngoingMatch(uuid);

        Integer pointWinnerId = Integer.valueOf(request.getParameter("player"));

        Match calculatedMatch = matchScoreCalculationService.calculate(match, pointWinnerId);

        if (calculatedMatch.getWinner() != null) {
            finishedMatchesPersistenceService.persist(calculatedMatch);
            ongoingMatchesService.removeMatch(uuid);

            response.sendRedirect("/matches");
            return;
        }

        response.sendRedirect("/match-score?uuid=" + uuid);
    }
}
