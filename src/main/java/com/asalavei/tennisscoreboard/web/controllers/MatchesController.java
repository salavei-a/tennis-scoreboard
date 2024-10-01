package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.validation.validators.DataValidator;
import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.services.FinishedMatchesPersistenceService;
import com.asalavei.tennisscoreboard.web.mapper.MatchDtoMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 12;

    private final FinishedMatchesPersistenceService service = new FinishedMatchesPersistenceService();

    private final MatchDtoMapper mapper = Mappers.getMapper(MatchDtoMapper.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getAttribute("errorMessage") != null) {
            request.getRequestDispatcher("matches.jsp").forward(request, response);
            return;
        }

        int pageNumber = determinePageNumber(request.getParameter("page"));

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null) {
            PlayerRequestDto player = PlayerRequestDto.builder()
                    .name(playerName)
                    .build();

            DataValidator.validate(player);
        }

        setMatchesAttributes(request, playerName, pageNumber);
        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }

    private int determinePageNumber(String pageParam) {
        try {
            return StringUtils.isBlank(pageParam) ? DEFAULT_PAGE_NUMBER : Math.abs(Integer.parseInt(pageParam));
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_NUMBER;
        }
    }

    private void setMatchesAttributes(HttpServletRequest request, String playerName, int pageNumber) {
        if (playerName == null) {
            request.setAttribute("matches", mapper.toResponseDto(service.findAllFinishedMatches(pageNumber, DEFAULT_PAGE_SIZE)));
        } else {
            request.setAttribute("matches", mapper.toResponseDto(service.findAllFinishedMatchesByPlayer(playerName, pageNumber, DEFAULT_PAGE_SIZE)));
        }
    }
}
