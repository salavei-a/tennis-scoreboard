<%@ page import="com.asalavei.tennisscoreboard.web.dto.MatchResponseDto" %>
<%@ page import="com.asalavei.tennisscoreboard.web.dto.PlayerResponseDto" %>
<%@ page import="com.asalavei.tennisscoreboard.web.dto.PlayerScoreResponseDto" %>
<%@ page import="com.asalavei.tennisscoreboard.enums.PlayerNumber" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    MatchResponseDto match = (MatchResponseDto) request.getAttribute("match");

    PlayerResponseDto firstPlayer = match.getFirstPlayer();
    PlayerResponseDto secondPlayer = match.getSecondPlayer();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Match Score - <%= firstPlayer.getName() %> vs <%= secondPlayer.getName() %></title>
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
        .navbar a i {
            margin-right: 8px;
        }
        .container {
            text-align: center;
            margin: 50px auto;
            max-width: 800px;
            padding: 40px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: center;
            font-size: 16px;
        }
        th {
            background-color: #f2f2f2;
            color: #333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .btn {
            padding: 12px 24px;
            margin: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 100%;
            max-width: 300px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .btn-form {
            display: inline-block;
            width: 100%;
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
                padding: 20px;
            }
            .btn {
                max-width: 100%;
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
    <a href="<%= request.getContextPath() %>/matches">
        <i class="matches-icon">✅</i> Finished Matches
    </a>
</div>

<div class="container">
    <h1>Match Score</h1>

    <table>
        <tr>
            <th>Players</th>
            <th>Sets</th>
            <th>Games</th>
            <th>Points</th>
        </tr>
        <%
            PlayerScoreResponseDto firstPlayerScore = firstPlayer.getPlayerScore();
            PlayerScoreResponseDto secondPlayerScore = secondPlayer.getPlayerScore();
        %>
        <tr>
            <td><%= firstPlayer.getName() %></td>
            <td><%= firstPlayerScore.getSets() %></td>
            <td><%= firstPlayerScore.getGames() %></td>
            <td>
                <%
                    Integer firstPlayerTiebreakPoints = firstPlayerScore.getTiebreakPoints();
                %>
                <%= firstPlayerTiebreakPoints == null
                    ? firstPlayerScore.getGameScore().getDisplay()
                    : firstPlayerTiebreakPoints %></td>
        </tr>
        <tr>
            <td><%= secondPlayer.getName() %></td>
            <td><%= secondPlayerScore.getSets() %></td>
            <td><%= secondPlayerScore.getGames() %></td>
            <td>
                <%
                    Integer secondPlayerTiebreakPoints = secondPlayerScore.getTiebreakPoints();
                %>
                <%= secondPlayerTiebreakPoints == null
                    ? secondPlayerScore.getGameScore().getDisplay()
                    : secondPlayerTiebreakPoints %></td>
        </tr>
    </table>

    <div class="btn-form">
        <form action="<%= request.getContextPath() %>/match-score?uuid=<%= match.getUuid() %>" method="post">
            <input type="hidden" name="point_winner_number" value="<%= PlayerNumber.FIRST_PLAYER.getNumber() %>">
            <button type="submit" class="btn" onclick="this.disabled = true; this.form.submit();">
                <%= match.getFirstPlayer().getName() %> Won the Point
            </button>
        </form>
    </div>


    <div class="btn-form">
        <form action="<%= request.getContextPath() %>/match-score?uuid=<%= match.getUuid() %>" method="post">
            <input type="hidden" name="point_winner_number" value="<%= PlayerNumber.SECOND_PLAYER.getNumber() %>">
            <button type="submit" class="btn" onclick="this.disabled = true; this.form.submit();">
                <%= match.getSecondPlayer().getName() %> Won the Point</button>
        </form>
    </div>
</div>

</body>
</html>
