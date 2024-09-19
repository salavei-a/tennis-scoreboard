package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.web.mapper.PlayerDtoMapper;
import com.asalavei.tennisscoreboard.services.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();

    private final PlayerDtoMapper mapper = Mappers.getMapper(PlayerDtoMapper.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PlayerRequestDto firstPlayer = PlayerRequestDto.builder()
                .name(request.getParameter("firstPlayer"))
                .build();

        PlayerRequestDto secondPlayer = PlayerRequestDto.builder()
                .name(request.getParameter("secondPlayer"))
                .build();

        UUID uuid = ongoingMatchesService.create(mapper.toDto(firstPlayer), mapper.toDto(secondPlayer));

        response.sendRedirect("/match-score?uuid=" + uuid);
    }
}
