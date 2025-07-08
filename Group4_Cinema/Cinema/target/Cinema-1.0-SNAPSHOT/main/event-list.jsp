<%@ page import="java.util.List" %>
<%@ page import="model.Event" %>
<%@ page import="dao.EventDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Event List</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                margin-top: 50px;
            }
            .table img {
                border-radius: 5px;
            }
        </style>
    </head>
    <body>

        <div class="container">
            
            <h2 class="text-center mb-4">Event List</h2>

            <div class="d-flex justify-content-between mb-3">
                <a href="<%= request.getContextPath()%>/Event?action=event-add" class="btn btn-primary">Add New Event</a>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
            </div>

            <table class="table table-bordered table-striped text-center">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Date</th>
                        <th>Image</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        EventDAO eventDAO = new EventDAO();
                        List<Event> events = eventDAO.getAllEvents();
                        for (Event event : events) {
                    %>
                    <tr>
                        <td><%= event.getEventID()%></td>
                        <td><%= event.getName()%></td>
                        <td><%= event.getDescription()%></td>
                        <td><%= event.getEventDate()%></td>
                        <td>
                            <img src="<%= request.getContextPath()%>/images/images-event/<%= event.getImagePath()%>" width="100" class="img-thumbnail">
                        </td>
                        <td>
                            <a href="<%= request.getContextPath()%>/Event?action=event-edit&id=<%= event.getEventID()%>" class="btn btn-warning btn-sm">Edit</a>
                            <a href="<%= request.getContextPath()%>/Event?action=event-delete&id=<%= event.getEventID()%>" 
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('Are you sure you want to delete this event?');">
                                Delete
                            </a>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
