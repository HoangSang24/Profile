<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Account</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .register-container {
            max-width: 400px;
            margin: auto;
            padding: 20px;
            margin-top: 50px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>

<body>
    <div class="container">
        <div class="register-container bg-light p-4">
            <div class="text-center">
                <h1>Create Account</h1>
            </div>
            <% String error = (String) request.getAttribute("error"); %>
            <% if (error != null) { %>
                <div class="alert alert-danger"><%= error %></div>
            <% } %>
            <% String success = (String) request.getAttribute("success"); %>
            <% if (success != null) { %>
                <div class="alert alert-success"><%= success %></div>
            <% } %>
            <form method="post" action="<%= request.getContextPath() %>/Function">
                <input type="hidden" name="action" value="create">
                <div class="mb-3">
                    <label class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" name="confirmPassword" required>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary w-100">Register</button>
                </div>
            </form>
            <div class="text-center mt-3">
                <a href="<%= request.getContextPath() %>/Login" class="btn btn-link">Back to Login</a>
            </div </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>