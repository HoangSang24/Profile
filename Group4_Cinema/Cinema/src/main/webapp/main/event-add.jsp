<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, dao.EventDAO" %>
<%@ page import="java.util.List, model.Cinema" %>
<%@ page import="dao.CinemaDAO" %>
<%
    // Lấy danh sách rạp chiếu để chọn
    CinemaDAO cinemaDAO = new CinemaDAO();
    List<Cinema> cinemaList = cinemaDAO.getAllCinemas();

    // Kiểm tra thông báo lỗi hoặc thành công
    String error = request.getParameter("error");
    String message = request.getParameter("message");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Event</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .container {
                max-width: 600px;
                margin-top: 50px;
                background: white;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            h2 {
                color: #007bff;
            }
            .btn-primary {
                background-color: #007bff;
                border-color: #007bff;
            }
            .btn-primary:hover {
                background-color: #0056b3;
                border-color: #004085;
            }
        </style>
        
    </head>
    
    <body>

        <div class="container">
           
            <h2 class="text-center mb-4">Add New Event</h2>

            <% if (error != null) {%>
            <div class="alert alert-danger"><%= error%></div>
            <% } %>
            <% if (message != null) {%>
            <div class="alert alert-success"><%= message%></div>
            <% }%>

            <form action="<%= request.getContextPath()%>/Event?action=event-add" method="post" enctype="multipart/form-data">
                <div class="mb-3">
                    <label class="form-label">Event Name:</label>
                    <input type="text" name="title" class="form-control" placeholder="Enter event name" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Description:</label>
                    <textarea name="description" class="form-control" rows="3" placeholder="Enter event description" required></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Event Date:</label>
                    <input type="date" name="eventDate" class="form-control" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Cinema:</label>
                    <select name="cinemaID" class="form-select" required>
                        <% for (Cinema c : cinemaList) {%>
                        <option value="<%= c.getCinemaID()%>"><%= c.getName()%></option>
                        <% }%>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Discount Percentage:</label>
                    <input type="number" name="discountPercentage" class="form-control" step="0.01" min="0" max="100" placeholder="Enter discount %" required>
                </div>

                <div class="mb-3">
                    <label class="form-label">Event Image:</label>
                    <input type="file" name="imageFile" class="form-control" required>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Add Event</button>
                    <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
                </div>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
