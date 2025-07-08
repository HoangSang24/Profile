<%@ page import="dao.BookingDAO" %>
<%@ page import="model.BookingInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Booking Summary</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .ticket {
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            margin: 20px auto;
            padding: 20px;
        }
        .ticket-header {
            text-align: center;
            border-bottom: 2px dashed #ddd;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .ticket-header h2 {
            color: #007bff;
            font-size: 1.75rem;
        }
        .ticket-body table {
            width: 100%;
        }
        .ticket-body th {
            text-align: left;
            padding: 8px 0;
            color: #6c757d;
        }
        .ticket-body td {
            text-align: right;
            padding: 8px 0;
            font-weight: bold;
        }
        .btn-container {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        .btn-container .btn {
            flex: 1;
            margin: 0 5px;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Booking Summary</h1>

        <%
            // Lấy danh sách thông tin booking từ cơ sở dữ liệu
            BookingDAO bookingDAO = new BookingDAO();
            int idmovie = Integer.parseInt(request.getParameter("movieID"));
            List<BookingInfo> bookingList = bookingDAO.getBookingDetailsByMovieId(idmovie);

            // Lấy danh sách ghế từ request
            String[] selectedSeats = request.getParameterValues("selectedSeats");
            String[] IDSeats = request.getParameterValues("IDSeats");

            if (bookingList != null && !bookingList.isEmpty()) {
                // Chỉ lấy một bản ghi duy nhất thay vì lặp qua tất cả
                BookingInfo booking = bookingList.get(0);

                // Tính tổng giá vé
                double totalPrice = (selectedSeats != null) ? booking.getPrice() * selectedSeats.length : 0;

        %>

        <div class="ticket">
            <div class="ticket-header">
                <h2><%= booking.getMovieTitle()%></h2>
            </div>
            <div class="ticket-body">
                <table>
                    <tbody>
                        <tr><th>Start Time</th><td><%= booking.getShowtime()%></td></tr>
                        <tr><th>Cinema</th><td><%= booking.getCinemaName()%> - <%= booking.getLocation()%></td></tr>
                        <tr><th>Room</th><td><%= booking.getRoomName()%></td></tr>
                        <tr><th>Seats</th>
                            <td>
                                <%= (selectedSeats != null && selectedSeats.length > 0) ? String.join(", ", selectedSeats) : "No seats selected!"%>
                            </td>
                        </tr>
                        <tr><th>Price</th>
                            <td><%= totalPrice + " VNĐ"%></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <form action="<%= request.getContextPath()%>/Booking" method="post">
                <input type="hidden" name="action" value="confirm-booking">
                <input type="hidden" name="movieID" value="<%= idmovie%>">
                <input type="hidden" name="showtimeID" value="<%= booking.getShowtimeID()%>">
                <input type="hidden" name="status" value="<%= booking.getStatus()%>">
                <input type="hidden" name="discountCode" value="<%= booking.getDiscountCode()%>">
                <input type="hidden" name="roomID" value="<%= booking.getRoomID()%>">
                <input type="hidden" name="eventID" value="<%= booking.getEventID()%>">
                <input type="hidden" name="bookingDate" value="<%= booking.getBookingDate()%>">
                <input type="hidden" name="price" value="<%= totalPrice%>">

                <% if (IDSeats != null) {
                        for (String seatID : IDSeats) {%>
                <input type="hidden" name="IDSeats" value="<%= seatID%>">
                <% }
                    } %>

                <% if (selectedSeats != null) {
                        for (String seat : selectedSeats) {%>
                <input type="hidden" name="selectedSeats" value="<%= seat%>">
                <% }
                    }%>

                <div class="btn-container">
                    <a href="Booking?action=seats&movieID=<%= request.getParameter("movieID")%>&showtimeID=<%= request.getParameter("showtimeID")%>" class="btn btn-secondary">Back</a>
                    <button type="submit" class="btn btn-primary">Mua vé</button>         
                </div>
            </form>
        </div>

        <%
            } else {
        %>
        <div class="alert alert-info text-center">No bookings available.</div>
        <%
            }
        %>
    </div>
</body>
</html>
