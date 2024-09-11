<%@ page import="com.asalavei.tennisscoreboard.dto.Match" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Match match = (Match) request.getAttribute("match");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Match Score</title>
</head>
<body>

<h1>Match Score</h1>

<table border="1">
    <tr>
        <th scope="col">Player</th>
        <th scope="col">Sets</th>
        <th scope="col">Games</th>
        <th scope="col">Points</th>
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

<br><br>

<h2>Update Score</h2>

<form action="${pageContext.request.contextPath}/match-score?uuid=<%= request.getParameter("uuid") %>" method="post">
    <input type="hidden" name="player" value=<%= match.getFirstPlayer().getId()%> >
    <button type="submit"><%= match.getFirstPlayer().getName() %> won the point</button>
</form>

<br>

<form action="${pageContext.request.contextPath}/match-score?uuid=<%= request.getParameter("uuid") %>" method="post">
    <input type="hidden" name="player" value=<%= match.getSecondPlayer().getId()%>>
    <button type="submit"><%= match.getSecondPlayer().getName() %> won the point</button>
</form>

</body>
</html>
