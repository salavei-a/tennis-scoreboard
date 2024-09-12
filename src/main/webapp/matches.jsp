<%@ page import="com.asalavei.tennisscoreboard.dto.Match" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Matches</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }
    </style>
</head>
<body>

<h1>Matches</h1>

<h2>Search Matches by Player Name</h2>
<form action="${pageContext.request.contextPath}/matches" method="get">
    <label for="filter_by_player_name">Player Name:</label>
    <input type="text" id="filter_by_player_name" name="filter_by_player_name" required>
    <button type="submit">Search</button>
</form>

<br><br>

<h2>Finished Matches</h2>
<table>
    <tr>
        <th scope="col">First Player</th>
        <th scope="col">Second Player</th>
        <th scope="col">Winner</th>
    </tr>
    <%
        List<Match> matches = (List<Match>) request.getAttribute("matches");

        if (matches != null && !matches.isEmpty()) {
            for (Match match : matches) {
    %>
    <tr>
        <td><%= match.getFirstPlayer().getName() %></td>
        <td><%= match.getSecondPlayer().getName() %></td>
        <td><%= match.getWinner().getName() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="3">No finished matches available</td>
    </tr>
    <%
        }
    %>
</table>

</body>
</html>
