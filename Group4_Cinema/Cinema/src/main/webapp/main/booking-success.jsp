<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ticket Booking Successful</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; }
        .container {
            max-width: 500px;
            background: white;
            padding: 30px;
            margin-top: 100px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        h1 { color: #28a745; }
    </style>
</head>
<body>
    <div class="container">
        <h1>🎉 Ticket Booking Successful! 🎉</h1>
        <p>Thank you for booking your tickets.</p>

        <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-primary">Back to Home Page</a>
    </div>
</body>
</html>
