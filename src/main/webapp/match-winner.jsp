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
    <title>Match Result - <%= match.getFirstPlayer().getName() %> vs <%= match.getSecondPlayer().getName() %></title>
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
        p {
            font-size: 18px;
            color: #555;
            margin-bottom: 30px;
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
            text-decoration: none;
            display: inline-block;
        }
        .btn:hover {
            background-color: #45a049;
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
                width: 100%;
                box-sizing: border-box;
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
    <h1>Match Result</h1>

    <p>Congratulations, <strong><%= match.getWinner().getName() %></strong> won the match!</p>

    <a href="<%= request.getContextPath() %>/new-match" class="btn">New Match</a>
    <a href="<%= request.getContextPath() %>/matches" class="btn">Finished Matches</a>
</div>

</body>
</html>