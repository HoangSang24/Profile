<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Showtimes" %>
<%
    Showtimes showtime = (Showtimes) request.getAttribute("showtime");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delete showtime</title>
</head>
<body>
    <h2>Confirm showtime deletion</h2>

    <p>Are you sure you want to delete this show?</p>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Movie</th>
            <th>Cinema</th>
            <th>Room</th>
            <th>Time</th>
            <th>Ticket price</th>
        </tr>
        <tr>
            <td><%= showtime.getShowtimeID() %></td>
            <td><%= showtime.getMovieID() %></td>
            <td><%= showtime.getCinemaID() %></td>
            <td><%= showtime.getRoomID() %></td>
            <td><%= showtime.getStartTime() %></td>
            <td><%= showtime.getPrice() %></td>
        </tr>
    </table>

    <form action="AddShowtime" method="post">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" value="<%= showtime.getShowtimeID() %>">
        <button type="submit">Confirm deletion</button>
        <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">Cancel</a>
    </form>
</body>
</html>
