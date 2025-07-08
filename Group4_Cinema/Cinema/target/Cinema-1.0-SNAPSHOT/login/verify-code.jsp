<%-- 
    Document   : verify-code
    Created on : Feb 25, 2025, 9:16:15 PM
    Author     : HoangSang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirm code</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2>Confirm code</h2>
    <form action="Function" method="post">
        <input type="hidden" name="action" value="verify">
        
        <div class="mb-3">
            <label for="code" class="form-label">Enter the confirmation code:</label>
            <input type="text" class="form-control" id="code" name="code" required>
        </div>
        
        <button type="submit" class="btn btn-success">Confirm</button>
    </form>

    <% if (request.getAttribute("error") != null) { %>
        <p class="text-danger"><%= request.getAttribute("error") %></p>
    <% } %>
</div>
</body>
</html>
