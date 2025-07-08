<%@page import="model.BookingInfo"%>
<%@page import="dao.BookingDAO"%>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    Integer userID = (Integer) session.getAttribute("userID");

    if (userID == null) {
        out.println("<script>alert('Bạn chưa đăng nhập! Vui lòng đăng nhập trước.'); window.location='/Cinema/login/login.jsp';</script>");
        return;
    }

    BookingDAO bookingDAO = new BookingDAO();
    List<BookingInfo> tickets = bookingDAO.getTicketById(userID);
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Ticket Management</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card {
                transition: 0.3s;
                border-radius: 12px;
                overflow: hidden;
            }
            .card:hover {
                transform: scale(1.03);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            .badge {
                font-size: 1rem;
            }
        </style>
    </head>
    <body>

        <div class="container mt-5">
            <h2 class="mb-4 text-center">🎟 Booking History</h2>

            <% if (tickets == null || tickets.isEmpty()) { %>
            <div class="alert alert-warning text-center">No tickets found.</div>
            <% } else { %>
            <div class="row">
                <% for (BookingInfo ticket : tickets) {%>
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title text-primary"><%= ticket.getMovieTitle()%></h5>
                            <p><strong>Ticket Code:</strong> <%= ticket.getTicketID()%></p>
                            <p><strong>Ticket Price:</strong> <span class="text-success fw-bold"><%= ticket.getPrice()%> VND</span></p>
                            <p><strong>Seat Position:</strong> <span class="badge bg-warning text-dark"><%= ticket.getSeatName()%></span></p>
                            <p><strong>Showtime:</strong> <%= ticket.getShowtime()%></p>
                            <p><strong>Name Cinema:</strong> <%= ticket.getCinemaName()%> - <%= ticket.getLocation()%></p>
                            <p><strong>Room:</strong> <%= ticket.getRoomName()%></p>
                            <p><strong>Time To Order:</strong> <%= ticket.getBookingDate()%></p>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
            <% }%>

            <div class="text-center mt-4">
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-secondary">⬅ Back</a>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
