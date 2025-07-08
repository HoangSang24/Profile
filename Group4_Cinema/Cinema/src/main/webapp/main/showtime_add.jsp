<%@page import="model.Rooms"%>
<%@page import="model.Cinema"%>
<%@page import="model.Movie"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Showtime</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="mb-4">Add New Showtime</h2>

            <% String message = (String) request.getAttribute("message");
                if (message != null) {%>
            <div class="alert alert-info"><%= message%></div>
            <% } %>

            <form action="AddShowtime?action=add" method="post">
                <div class="mb-3">
                    <label for="movieID" class="form-label">Select Movie</label>
                    <select class="form-select" id="movieID" name="movieID" required>
                        <option value="">-- Select Movie --</option>
                        <% List<Movie> movieList = (List<Movie>) request.getAttribute("movies");
                            if (movieList != null) {
                                for (Movie movie : movieList) {%>
                        <option value="<%= movie.getMovieID()%>"><%= movie.getTitle()%></option>
                        <%     }
                            } %>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="cinemaID" class="form-label">Select Cinema</label>
                    <select class="form-select" id="cinemaID" name="cinemaID" required>
                        <option value="">-- Select Cinema --</option>
                        <% List<Cinema> cinemaList = (List<Cinema>) request.getAttribute("cinemas");
                            if (cinemaList != null) {
                                for (Cinema cinema : cinemaList) {%>
                        <option value="<%= cinema.getCinemaID()%>"><%= cinema.getName()%> - <%=cinema.getLocation()%></option>
                        <%     }
                            } %>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="roomID" class="form-label">Select Room</label>
                    <select class="form-select" id="roomID" name="roomID" required>
                        <option value="">-- Select Room --</option>
                        <% List<Rooms> roomList = (List<Rooms>) request.getAttribute("rooms");
                            if (roomList != null) {
                                for (Rooms room : roomList) {%>
                        <option value="<%= room.getRoomID()%>"><%= room.getRoomName()%></option>
                        <%     }
                            }%>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="price" class="form-label">Ticket Price</label>
                    <input type="number" class="form-control" id="price" name="price" required min="0" step="0.01">
                </div>

                <div class="mb-3">
                    <label for="startTime" class="form-label">Show Time</label>
                    <input type="datetime-local" class="form-control" id="startTime" name="startTime" required>
                </div>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
                <button type="submit" class="btn btn-primary">Add Showtime</button>
            </form>
        </div>

    </body>
</html>
