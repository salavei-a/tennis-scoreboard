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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));

        request.setAttribute("match", ongoingMatchesService.getOngoingMatch(uuid));
        request.setAttribute("uuid", uuid);
        request.getRequestDispatcher("match-score.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
        FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

        UUID uuid = UUID.fromString(request.getParameter("uuid"));

        Match match = ongoingMatchesService.getOngoingMatch(uuid);

        Integer pointWinnerId = Integer.valueOf(request.getParameter("player"));

        Match calculatedMatch = matchScoreCalculationService.calculate(match, pointWinnerId);

        if (calculatedMatch.getWinner() != null) {
            Match persistedMatch = finishedMatchesPersistenceService.persist(calculatedMatch);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + "Winner: " + persistedMatch.getWinner().getName() + "</h1>");
            out.println("</body></html>");

            ongoingMatchesService.removeMatch(uuid);

            return;
        }

        response.sendRedirect("/match-score?uuid=" + uuid);
    }
}
