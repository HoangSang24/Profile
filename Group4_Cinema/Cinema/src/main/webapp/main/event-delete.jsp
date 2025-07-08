<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dao.EventDAO" %>
<%
    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
        response.sendRedirect("Event?action=event-list&error=Missing event ID");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Delete Event</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow-lg p-4 text-center">
            <h2 class="text-danger">Are you sure?</h2>
            <p class="text-muted">This action cannot be undone. Do you really want to delete this event?</p>

            <form action="Event" method="POST">
                <input type="hidden" name="action" value="event-delete">
                <input type="hidden" name="id" value="<%= idParam %>">

                <div class="d-flex justify-content-center gap-3">
                    <button type="submit" class="btn btn-danger">Yes, Delete</button>
                    <a href="Event?action=event-list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bootstrap JS (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
