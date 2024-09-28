package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.validation.groups.Create;
import com.asalavei.tennisscoreboard.validation.validator.DataValidator;
import com.asalavei.tennisscoreboard.web.dto.MatchRequestDto;
import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.web.mapper.MatchDtoMapper;
import com.asalavei.tennisscoreboard.services.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.UUID;

@Log
@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();

    private final MatchDtoMapper mapper = Mappers.getMapper(MatchDtoMapper.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getAttribute("errorMessage") != null) {
            request.getRequestDispatcher("new-match.jsp").forward(request, response);
            return;
        }

        PlayerRequestDto firstPlayer = PlayerRequestDto.builder()
                .name(request.getParameter("firstPlayer"))
                .build();

        PlayerRequestDto secondPlayer = PlayerRequestDto.builder()
                .name(request.getParameter("secondPlayer"))
                .build();

        DataValidator.validate(firstPlayer, Create.class);
        DataValidator.validate(secondPlayer, Create.class);

        MatchRequestDto match = MatchRequestDto.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();

        DataValidator.validateMatch(match);

        UUID uuid = ongoingMatchesService.create(mapper.toDto(match));

        response.sendRedirect(request.getContextPath() + "/match-score?uuid=" + uuid);
    }
}
