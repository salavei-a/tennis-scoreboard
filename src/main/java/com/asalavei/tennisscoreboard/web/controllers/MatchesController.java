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

        int pageNumber = parsePageNumber(request.getParameter("page"));

        String playerName = request.getParameter("filter_by_player_name");

        if (playerName != null) {
            PlayerRequestDto player = PlayerRequestDto.builder()
                    .name(playerName)
                    .build();

            DataValidator.validate(player);

            setMatchesAttributesByPlayer(request, playerName, pageNumber);
        } else {
            setMatchesAttributes(request, pageNumber);
        }

        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }

    private int parsePageNumber(String pageParam) {
        try {
            return pageParam != null ? Math.abs(Integer.parseInt(pageParam)) : DEFAULT_PAGE_NUMBER;
        } catch (NumberFormatException e) {
            return DEFAULT_PAGE_NUMBER;
        }
    }

    private void setMatchesAttributes(HttpServletRequest request, int pageNumber) {
        request.setAttribute("totalPages", service.countTotalPages(DEFAULT_PAGE_SIZE));
        request.setAttribute("matches", mapper.toResponseDto(service.findAll(pageNumber, DEFAULT_PAGE_SIZE)));
    }

    private void setMatchesAttributesByPlayer(HttpServletRequest request, String playerName, int pageNumber) {
        request.setAttribute("totalPages", service.countTotalPagesByPlayerName(playerName, DEFAULT_PAGE_SIZE));
        request.setAttribute("matches", mapper.toResponseDto(service.findAllByPlayerName(playerName, pageNumber, DEFAULT_PAGE_SIZE)));
    }
}
