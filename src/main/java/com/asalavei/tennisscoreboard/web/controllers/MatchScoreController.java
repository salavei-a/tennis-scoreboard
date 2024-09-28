package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.exceptions.NotFoundException;
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
        String uuidParameter = request.getParameter("uuid");

        if (StringUtils.isBlank(uuidParameter)) {
            throw new NotFoundException("UUID parameter is missing or empty");
        }

        UUID uuid = DataValidator.getValidatedUuid(uuidParameter);

        Match match = ongoingMatchesService.getOngoingMatch(uuid);

        request.setAttribute("match", mapper.toResponseDto(match));
        request.setAttribute("uuid", uuid);
        request.getRequestDispatcher("match-score.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID matchUuid = DataValidator.getValidatedUuid(request.getParameter("uuid"));
        UUID pointWinnerUuid = DataValidator.getValidatedUuid(request.getParameter("player"));

        Match match = ongoingMatchesService.getOngoingMatch(matchUuid);

        DataValidator.validatePointWinner(pointWinnerUuid, match);

        Match calculatedMatch = matchScoreCalculationService.calculate(match, pointWinnerUuid);

        if (calculatedMatch.getWinner() != null) {
            ongoingMatchesService.removeMatch(matchUuid);
            finishedMatchesPersistenceService.persist(calculatedMatch);

            request.setAttribute("match", mapper.toResponseDto(calculatedMatch));
            request.getRequestDispatcher("match-winner.jsp").forward(request, response);

            return;
        }

        response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + matchUuid);
    }
}
