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
        .pagination {
            margin-top: 20px;
            display: flex;
            justify-content: center;
        }
        .pagination a {
            padding: 8px 16px;
            border: 1px solid black;
            margin: 0 4px;
            text-decoration: none;
        }
        .pagination a.active {
            background-color: #4CAF50;
            color: white;
        }
    </style>
</head>
<body>

<h1>Matches</h1>

<h2>Search Matches by Player Name</h2>
<form action="${pageContext.request.contextPath}/matches" method="get">
    <label for="filter_by_player_name">Player Name:</label>
    <input type="text" id="filter_by_player_name" name="filter_by_player_name"
           value="<%= request.getParameter("filter_by_player_name") != null ?
           request.getParameter("filter_by_player_name") : "" %>" required>
    <button type="submit">Search</button>
</form>

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

<div class="pagination">
    <%
        int currentPage = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
        int totalPages = (int) request.getAttribute("totalPages");
        String playerNameFilter = request.getParameter("filter_by_player_name");

        if (playerNameFilter != null) {

        // Previous page
        if (currentPage > 1) {
    %>
    <a href="${pageContext.request.contextPath}/matches?page=<%= currentPage - 1 %>&filter_by_player_name=<%= playerNameFilter %>">Previous</a>
    <%
        }

        // Number of pages
        for (int i = 1; i <= totalPages; i++) {
            if (i == currentPage) {
    %>
    <a href="#" class="active"><%= i %></a>
    <%
    } else {
    %>
    <a href="${pageContext.request.contextPath}/matches?page=<%= i %>&filter_by_player_name=<%= playerNameFilter %>"><%= i %></a>
    <%
            }
        }

        // Next page
        if (currentPage < totalPages) {
    %>
    <a href="${pageContext.request.contextPath}/matches?page=<%= currentPage + 1 %>&filter_by_player_name=<%= playerNameFilter %>">Next</a>
    <%
        }
    } else {
        // Previous page
        if (currentPage > 1) {
        %>
        <a href="${pageContext.request.contextPath}/matches?page=<%= currentPage - 1 %>">Previous</a>
            <%
        }

        // Number of pages
        for (int i = 1; i <= totalPages; i++) {
            if (i == currentPage) {
    %>
        <a href="#" class="active"><%= i %></a>
            <%
    } else {
    %>
        <a href="${pageContext.request.contextPath}/matches?page=<%= i %>"><%= i %></a>
            <%
            }
        }

        // Next page
        if (currentPage < totalPages) {
    %>
        <a href="${pageContext.request.contextPath}/matches?page=<%= currentPage + 1 %>">Next</a>
            <%
        }
    }
            %>
</div>

</body>
</html>