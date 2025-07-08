<%@page import="model.Showtimes"%>
<%@page import="java.util.List"%>
<%@page import="dao.ShowtimesDAO"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    int movieID = Integer.parseInt(request.getParameter("movieID"));

    ShowtimesDAO showtimeDAO = new ShowtimesDAO();
    List<Showtimes> showtimes = showtimeDAO.getShowtimesByMovie(movieID);

%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Select Showtime</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2 class="text-center mb-4">🎬 Select Showtime</h2>

            <% if (showtimes == null || showtimes.isEmpty()) { %>
            <div class="alert alert-warning text-center" role="alert">
                There are currently no showtimes for this movie!
            </div>
            <% } else { %>
            <ul class="list-group">
                <% for (Showtimes showtime : showtimes) {%>
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    <div>
                        <span>🕒 <%= showtime.getStartTime()%></span> <br>
                        <span>📍 <strong><%= showtime.getCinemaName()%></strong> - <%= showtime.getAddress()%></span>
                    </div>
                    <a href="Booking?action=seats&movieID=<%= movieID%>&showtimeID=<%= showtime.getShowtimeID()%>" 
                       class="btn btn-primary btn-sm">🎟 Select Ratio</a>
                </li>
                <% } %>
            </ul>
            <% }%>

            <div class="text-center mt-3">
                <a href="MainFuncion?movie=main-cinema" class="btn btn-secondary mt-3">Back</a>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
