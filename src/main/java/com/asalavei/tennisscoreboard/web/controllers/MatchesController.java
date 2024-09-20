package com.asalavei.tennisscoreboard.web.controllers;

import com.asalavei.tennisscoreboard.exceptions.ValidationException;
import com.asalavei.tennisscoreboard.validation.scenario.FindByName;
import com.asalavei.tennisscoreboard.web.dto.PlayerRequestDto;
import com.asalavei.tennisscoreboard.services.FinishedMatchesPersistenceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Set;

@WebServlet("/matches")
public class MatchesController extends HttpServlet {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    private final FinishedMatchesPersistenceService service = new FinishedMatchesPersistenceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = parsePageParam(request.getParameter("page"));

        String playerName = request.getParameter("filter_by_player_name");

        PlayerRequestDto player = PlayerRequestDto.builder()
                .name(playerName)
                .build();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<PlayerRequestDto>> violations = validator.validate(player, FindByName.class);

        if (playerName != null && !violations.isEmpty()) {
            setMatchesAttributes(request, page);

            for (ConstraintViolation<PlayerRequestDto> violation : violations) {
                throw new ValidationException(violation.getMessage(), "matches.jsp");
            }
        }

        if (!StringUtils.isBlank(playerName)) {
            setMatchesAttributesByPlayer(request, playerName, page);
        } else {
            setMatchesAttributes(request, page);
        }

        request.getRequestDispatcher("matches.jsp").forward(request, response);
    }

    private int parsePageParam(String pageParam) {
        return pageParam != null ? Integer.parseInt(pageParam) : DEFAULT_PAGE;
    }

    private void setMatchesAttributes(HttpServletRequest request, int page) {
        request.setAttribute("totalPages", service.countTotalPages(DEFAULT_SIZE));
        request.setAttribute("matches", service.findAll(page, DEFAULT_SIZE));
    }

    private void setMatchesAttributesByPlayer(HttpServletRequest request, String playerName, int page) {
        request.setAttribute("totalPages", service.countTotalPagesByPlayerName(playerName, DEFAULT_SIZE));
        request.setAttribute("matches", service.findAllByPlayerName(playerName, page, DEFAULT_SIZE));
    }
}
