package com.asalavei.tennisscoreboard.controllers;

import com.asalavei.tennisscoreboard.dto.Player;
import com.asalavei.tennisscoreboard.services.OngoingMatchesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("new-match.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Player firstPlayer = Player.builder()
                .name(request.getParameter("firstPlayer"))
                .build();

        Player secondPlayer = Player.builder()
                .name(request.getParameter("secondPlayer"))
                .build();

        UUID uuid = new OngoingMatchesService().create(firstPlayer, secondPlayer);

        response.sendRedirect("/match-score?uuid=" + uuid);
    }
}
