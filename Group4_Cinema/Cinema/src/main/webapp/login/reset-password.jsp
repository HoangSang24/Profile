<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Reset Password</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Reset Password</h2>
            <form action="Function" name="reset" method="post">
                <input type="hidden" name="action" value="reset">
                <div class="mb-3">
                    <label for="email-reset" class="form-label">Email:</label>
                    <input type="email" class="form-control" id="email-reset" name="email-reset" required>
                </div>
                <div class="mb-3">
                    <label for="password-reset" class="form-label">New Password:</label>
                    <input type="password" class="form-control" id="password-reset" name="password-reset" required>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword-reset" class="form-label">Confirm Password:</label>
                    <input type="password" class="form-control" id="confirmPassword-reset" name="confirmPassword-reset" required>
                </div>
                <button type="submit" class="btn btn-warning">Change Password</button>
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