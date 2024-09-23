<%@ page import="com.asalavei.tennisscoreboard.web.dto.MatchResponseDto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    MatchResponseDto match = (MatchResponseDto) request.getAttribute("match");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Match Score - <%= match.getFirstPlayer().getName() %> vs <%= match.getSecondPlayer().getName() %></title>
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
        <tr>
            <td><%= match.getFirstPlayer().getName() %></td>
            <td><%= match.getFirstPlayer().getSets() %></td>
            <td><%= match.getFirstPlayer().getGames() %></td>
            <td><%= match.getFirstPlayer().getGamePoints() %></td>
        </tr>
        <tr>
            <td><%= match.getSecondPlayer().getName() %></td>
            <td><%= match.getSecondPlayer().getSets() %></td>
            <td><%= match.getSecondPlayer().getGames() %></td>
            <td><%= match.getSecondPlayer().getGamePoints() %></td>
        </tr>
    </table>

    <div class="btn-form">
        <form action="<%= request.getContextPath() %>/match-score?uuid=<%= request.getParameter("uuid") %>" method="post">
            <input type="hidden" name="player" value="<%= match.getFirstPlayer().getId() %>">
            <button type="submit" class="btn" onclick="this.disabled = true; this.form.submit();">
                <%= match.getFirstPlayer().getName() %> Won the Point
            </button>
        </form>
    </div>


    <div class="btn-form">
        <form action="<%= request.getContextPath() %>/match-score?uuid=<%= request.getParameter("uuid") %>" method="post">
            <input type="hidden" name="player" value="<%= match.getSecondPlayer().getId() %>">
            <button type="submit" class="btn" onclick="this.disabled = true; this.form.submit();">
                <%= match.getSecondPlayer().getName() %> Won the Point</button>
        </form>
    </div>
</div>

</body>
</html>
