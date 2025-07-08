<%@page import="java.util.List"%>
<%@ page import="dao.MovieDAO, dao.CinemaDAO, dao.RoomsDAO, model.Movie, model.Cinema, model.Rooms, model.Showtimes" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Showtime List</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 30px;
        }
        .table th {
            background-color: #007bff;
            color: white;
        }
        .btn-action {
            display: flex;
            gap: 5px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2 class="text-center mb-4">Showtime List</h2>

    <div class="mb-3">
        <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
        <a href="AddShowtime?action=add" class="btn btn-primary">Add New Showtime</a>
    </div>

    <%
        List<Showtimes> showtimeList = (List<Showtimes>) request.getAttribute("showtimeList");
        MovieDAO movieDAO = new MovieDAO();
        CinemaDAO cinemaDAO = new CinemaDAO();
        RoomsDAO roomDAO = new RoomsDAO();
    %>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-primary">
                <tr>
                    <th>Movie</th>
                    <th>Cinema</th>
                    <th>Room</th>
                    <th>Start Time</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Showtimes showtime : showtimeList) { 
                    Movie movie = movieDAO.getMovieById(showtime.getMovieID());
                    Cinema cinema = cinemaDAO.getCinemaById(showtime.getCinemaID());
                %>
                <tr>
                    <td><%= movie.getTitle() %></td>
                    <td><%= cinema.getName() %> - <%= showtime.getAddress() %></td>
                    <td><%= showtime.getRoomName() %></td>
                    <td><%= showtime.getStartTime() %></td>
                    <td>$<%= showtime.getPrice() %></td>
                    <td class="btn-action">
                        <a href="AddShowtime?action=edit&id=<%= showtime.getShowtimeID() %>" class="btn btn-warning btn-sm">Edit</a>
                        <a href="AddShowtime?action=delete&id=<%= showtime.getShowtimeID() %>" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
            
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
