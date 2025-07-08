<%-- 
    Document   : forgot-password
    Created on : Feb 25, 2025, 8:08:48 PM
    Author     : HoangSang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Forgot password</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Forgot password</h2>
            <form action="Function" method="post">
                <input type="hidden" name="action" value="forgot">
                <div class="mb-3">
                    <label for="email" class="form-label">Enter Email:</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <button type="submit" class="btn btn-primary">Send</button>
            </form>
            <% if (request.getAttribute("error") != null) {%>
            <p class="text-danger"><%= request.getAttribute("error")%></p>
            <% } %>

            <% if (request.getAttribute("success") != null) {%>
            <p class="text-success"><%= request.getAttribute("success")%></p>
            <% }%>
        </div>
    </body>
</html>
