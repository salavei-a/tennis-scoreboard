package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.exceptions.ForbiddenException;
import com.asalavei.tennisscoreboard.exceptions.NotFoundException;
import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import com.asalavei.tennisscoreboard.validation.validators.DataValidator;
import com.asalavei.tennisscoreboard.web.mapper.MatchDtoMapper;
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
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
    private final MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = new FinishedMatchesPersistenceService();

    private final MatchDtoMapper mapper = Mappers.getMapper(MatchDtoMapper.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = ensureUuid(request.getParameter("uuid"));
        Match match = ongoingMatchesService.getOngoingMatch(uuid);

        request.setAttribute("match", mapper.toResponseDto(match));
        request.setAttribute("uuid", uuid);
        request.getRequestDispatcher("match-score.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = ensureUuid(request.getParameter("uuid"));
        Match match = ongoingMatchesService.getOngoingMatch(uuid);

        int pointWinnerNumber = ensurePointWinnerNumber(request.getParameter("point_winner_number"), match);
        Match calculatedMatch = matchScoreCalculationService.calculate(match, pointWinnerNumber);

        if (calculatedMatch.getWinner() != null) {
            ongoingMatchesService.remove(uuid);
            finishedMatchesPersistenceService.save(calculatedMatch);

            request.setAttribute("match", mapper.toResponseDto(calculatedMatch));
            request.getRequestDispatcher("match-winner.jsp").forward(request, response);

            return;
        }

        response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);
    }

    private UUID ensureUuid(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new NotFoundException("UUID parameter is missing or empty");
        }

        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ForbiddenException(String.format("Invalid UUID=%s", uuid));
        }
    }

    private int ensurePointWinnerNumber(String pointWinnerNumberParameter, Match match) {
        int pointWinnerNumber;

        try {
            pointWinnerNumber = Integer.parseInt(pointWinnerNumberParameter);
            DataValidator.validatePlayerParticipation(pointWinnerNumber, match);
        } catch (NumberFormatException e) {
            throw new ForbiddenException(String.format("Invalid number=%s for point winner in match=%s", pointWinnerNumberParameter, match));
        } catch (ValidationException e) {
            throw new ForbiddenException(e.getMessage());
        }

        return pointWinnerNumber;
    }
}
