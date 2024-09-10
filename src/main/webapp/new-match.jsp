<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Start a New Match</title>
</head>
<body>
<h1>Start a New Match</h1>

<form action="/new-match" method="post">
    <label for="firstPlayer">First Player:</label>
    <input type="text" id="firstPlayer" name="firstPlayer" required>
    <br><br>

    <label for="secondPlayer">Second Player:</label>
    <input type="text" id="secondPlayer" name="secondPlayer" required>
    <br><br>

    <button type="submit">Start</button>
</form>

</body>
</html>