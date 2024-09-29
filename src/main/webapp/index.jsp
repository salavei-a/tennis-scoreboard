<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Tennis Scoreboard</title>
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
            max-width: 900px;
            background-color: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-bottom: 40px;
        }
        .btn {
            display: inline-block;
            padding: 12px 24px;
            margin: 10px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-size: 16px;
            transition: background-color 0.3s;
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
    <a href="<%= request.getContextPath() %>/" class="active">
        <i class="home-icon">🏠</i> Home
    </a>
    <a href="<%= request.getContextPath() %>/new-match">
        <i class="new-match-icon">➕</i> New Match
    </a>
    <a href="<%= request.getContextPath() %>/matches">
        <i class="new-match-icon">✅</i> Finished Matches
    </a>
</div>

<div class="container">
    <h1>Welcome to the Tennis Scoreboard</h1>
    <a href="<%= request.getContextPath() %>/new-match" class="btn">New Match</a>
    <a href="<%= request.getContextPath() %>/matches" class="btn">Finished Matches</a>
</div>

</body>
</html>
