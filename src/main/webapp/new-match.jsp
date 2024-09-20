<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Start a New Match</title>
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
            text-align: center;
            margin: 50px auto;
            max-width: 600px;
            padding: 40px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
        }
        form {
            display: flex;
            flex-direction: column;
            align-items: stretch;
            gap: 15px;
        }
        label {
            font-size: 16px;
            color: #555;
            text-align: left;
        }
        input[type="text"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
        }
        button {
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
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
            button {
                width: 100%;
            }
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
            min-width: 250px;
            opacity: 0;
            transition: opacity 0.5s ease-in-out;
        }
        .error-notification.show {
            opacity: 1;
        }
    </style>
</head>
<body>

<div class="navbar">
    <a href="<%= request.getContextPath() %>/">
        <i class="home-icon">🏠</i> Home
    </a>
    <a href="<%= request.getContextPath() %>/new-match" class="active">
        <i class="new-match-icon">➕</i> New Match
    </a>
    <a href="<%= request.getContextPath() %>/matches">
        <i class="new-match-icon">✅</i> Finished Matches
    </a>
</div>

<div class="container">
    <form action="<%= request.getContextPath() %>/new-match" method="post">
        <label for="firstPlayer">First Player:</label>
        <input type="text" id="firstPlayer" name="firstPlayer" required>

        <label for="secondPlayer">Second Player:</label>
        <input type="text" id="secondPlayer" name="secondPlayer" required>

        <button type="submit">Start Match</button>
    </form>
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
