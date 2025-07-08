<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Showtimes, model.Movie, model.Cinema, model.Rooms" %>
<%
    Showtimes showtime = (Showtimes) request.getAttribute("showtime");
    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    List<Cinema> cinemas = (List<Cinema>) request.getAttribute("cinemas");
    List<Rooms> rooms = (List<Rooms>) request.getAttribute("rooms");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit showtime</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 600px;
            margin-top: 30px;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body>

<div class="container">
    <h2 class="text-center mb-4">Edit showtime</h2>

    <form action="AddShowtime" method="post">
        <input type="hidden" name="action" value="edit">
        <input type="hidden" name="showtimeID" value="<%= showtime.getShowtimeID() %>">


     
        <div class="mb-3">
            <label class="form-label">Select movie:</label>
            <select name="movieID" class="form-select" required>
                <% for (Movie movie : movies) { %>
                    <option value="<%= movie.getMovieID() %>" <%= (movie.getMovieID() == showtime.getMovieID()) ? "selected" : "" %>>
                        <%= movie.getTitle() %>
                    </option>
                <% } %>
            </select>
        </div>

  
        <div class="mb-3">
            <label class="form-label">Select Cinema:</label>
            <select name="cinemaID" class="form-select" required>
                <% for (Cinema cinema : cinemas) { %>
                    <option value="<%= cinema.getCinemaID() %>" <%= (cinema.getCinemaID() == showtime.getCinemaID()) ? "selected" : "" %>>
                        <%= cinema.getName() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Select Room</label>
            <select name="roomID" class="form-select" required>
                <% for (Rooms room : rooms) { %>
                    <option value="<%= room.getRoomID() %>" <%= (room.getRoomID() == showtime.getRoomID()) ? "selected" : "" %>>
                        <%= room.getRoomName() %>
                    </option>
                <% } %>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Start Time:</label>
            <input type="datetime-local" name="startTime" class="form-control" 
                   value="<%= showtime.getStartTime().toString().replace(" ", "T") %>" required>
        </div>

        <!-- Giá vé -->
        <div class="mb-3">
            <label class="form-label">Ticket Price:</label>
            <input type="number" name="price" class="form-control" value="<%= showtime.getPrice() %>" step="0.01" required>
        </div>

        <!-- Nút bấm -->
        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">Update</button>
            <a href="AddShowtime" class="btn btn-secondary">Back</a>
        </div>
    </form>

    <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-danger mt-3"><%= request.getAttribute("message") %></div>
    <% } %>
    </br>
    
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
