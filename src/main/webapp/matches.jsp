<%@ page import="java.util.List" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="com.asalavei.tennisscoreboard.web.dto.MatchResponseDto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Finished Matches</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background-color: #4CAF50;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .navbar a {
            float: left;
            display: flex;
            align-items: center;
            color: #fff;
            text-align: center;
            padding: 14px 20px;
            text-decoration: none;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .navbar a:hover {
            background-color: #45a049;
        }
        .navbar a.active {
            background-color: #388e3c;
        }
        .navbar a i {
            margin-right: 8px;
        }
        .container {
            margin: 20px auto;
            max-width: 1200px;
            padding: 0 20px;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        form {
            margin-bottom: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
        }
        label {
            font-size: 16px;
            color: #555;
        }
        input[type="text"] {
            padding: 8px;
            width: 250px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #45a049;
        }
        .matches-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: flex-start;
        }
        .match-card {
            background-color: #fff;
            width: 300px;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        .player-name {
            font-size: 18px;
            text-align: left;
            margin: 5px 0;
        }
        .winner {
            font-weight: bold;
        }
        .winner::after {
            content: ' ✅';
        }
        .error-notification {
            position: fixed;
            top: 20px;
            right: 20px;
            background-color: #f8d7da;
            color: #721c24;
            padding: 15px 20px;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
            z-index: 1000;
            min-width: 150px;
            opacity: 0;
            transition: opacity 0.5s ease-in-out;
        }
        .error-notification.show {
            opacity: 1;
        }
        .pagination {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 5px;
        }
        .pagination a {
            padding: 8px 16px;
            border: 1px solid #4CAF50;
            border-radius: 4px;
            text-decoration: none;
            background-color: #4CAF50;
            color: white;
            transition: background-color 0.3s, color 0.3s;
        }
        .pagination a.active {
            background-color: #388e3c;
            pointer-events: none;
        }
        .pagination a:hover:not(.active) {
            background-color: #45a049;
            color: white;
        }
        @media (max-width: 600px) {
            .navbar a {
                float: none;
                width: 100%;
                text-align: left;
                padding: 14px;
            }
            .container {
                margin: 20px;
                padding: 0 10px;
            }
            button {
                width: 100%;
            }
            form {
                flex-direction: column;
                align-items: stretch;
            }
            input[type="text"] {
                width: 100%;
            }
            .match-card {
                width: 100%;
            }
        }
    </style>
</head>
<body>

<div class="navbar">
    <a href="<%= request.getContextPath() %>/">
        <i class="home-icon">🏠</i> Home
    </a>
    <a href="<%= request.getContextPath() %>/new-match">
        <i class="new-match-icon">➕</i> New Match
    </a>
    <a href="<%= request.getContextPath() %>/matches" class="active">
        <i class="finished-matches-icon">✅</i> Finished Matches
    </a>
</div>

<div class="container">
    <h1>Finished Matches</h1>

    <form action="<%= request.getContextPath() %>/matches" method="get">
        <label for="filter_by_player_name">Player Name:</label>
        <input type="text" id="filter_by_player_name" name="filter_by_player_name"
               value="<%= (request.getParameter("filter_by_player_name") != null) ?
                           request.getParameter("filter_by_player_name") : "" %>" required>
        <button type="submit">Search</button>
    </form>

    <div class="matches-grid">
        <%
            List<MatchResponseDto> matches = (List<MatchResponseDto>) request.getAttribute("matches");
            if (matches != null && !matches.isEmpty()) {
                for (MatchResponseDto match : matches) {
                    String firstPlayerName = match.getFirstPlayer().getName();
                    String secondPlayerName = match.getSecondPlayer().getName();
                    String winnerName = match.getWinner().getName();
        %>
        <div class="match-card">
            <%
                if (firstPlayerName.equals(winnerName)) {
            %>
            <div class="player-name winner"><%= firstPlayerName %></div>
            <div class="player-name"><%= secondPlayerName %></div>
            <% } else { %>
            <div class="player-name"><%= firstPlayerName %></div>
            <div class="player-name winner"><%= secondPlayerName %></div>
            <% } %>
        </div>
        <%     }
        } else {
        %>
        <p>No finished matches available</p>
        <% } %>
    </div>

    <div class="pagination">
        <%
            int currentPage = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int totalPages = 1;
            Object totalPagesAttr = request.getAttribute("totalPages");
            if (totalPagesAttr != null) {
                try {
                    totalPages = Integer.parseInt(totalPagesAttr.toString());
                    if (totalPages < 1) totalPages = 1;
                } catch (NumberFormatException e) {
                    totalPages = 1;
                }
            }

            String playerNameFilter = request.getParameter("filter_by_player_name");
            String filterParam = "";
            if (playerNameFilter != null && !playerNameFilter.trim().isEmpty()) {
                filterParam = "&filter_by_player_name=" + java.net.URLEncoder.encode(playerNameFilter, StandardCharsets.UTF_8);
            }

            if (currentPage > 1) {
        %>
        <a href="<%= request.getContextPath() %>/matches?page=<%= currentPage - 1 %><%= filterParam %>">Previous</a>
        <%
            }

            for (int i = 1; i <= totalPages; i++) {
                if (i == currentPage) {
        %>
        <a href="#" class="active"><%= i %></a>
        <%
        } else {
        %>
        <a href="<%= request.getContextPath() %>/matches?page=<%= i %><%= filterParam %>"><%= i %></a>
        <%
                }
            }

            if (currentPage < totalPages) {
        %>
        <a href="<%= request.getContextPath() %>/matches?page=<%= currentPage + 1 %><%= filterParam %>">Next</a>
        <%
            }
        %>
    </div>
</div>

<% String errorMessage = (String) request.getAttribute("errorMessage"); %>
<% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
<div id="errorNotification" class="error-notification">
    <span><%= errorMessage %></span>
</div>
<script>
    function showErrorNotification() {
        var notification = document.getElementById("errorNotification");
        notification.classList.add("show");

        setTimeout(function() {
            notification.classList.remove("show");
        }, 5000);
    }

    window.onload = function() {
        showErrorNotification();
    }
</script>
<% } %>

</body>
</html>
