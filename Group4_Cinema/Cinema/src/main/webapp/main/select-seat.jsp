<%@page import="model.Rooms"%>
<%@page import="dao.RoomsDAO"%>
<%@page import="dao.ShowtimesDAO"%>
<%@page import="model.Showtimes"%>
<%@page import="java.util.Set"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="model.Seats"%>
<%@page import="dao.SeatsDAO"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Select Seat</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .seat-container {
                display: grid;
                grid-template-columns: repeat(20, 40px);
                gap: 5px;
                justify-content: center;
                margin: auto;
            }
            .seat {
                width: 40px;
                height: 40px;
                background-color: #0d6efd;
                color: white;
                text-align: center;
                line-height: 40px;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
            }
            .seat.selected {
                background-color: #198754;
            }
            .seat.booked {
                background-color: #6c757d;
                cursor: not-allowed;
            }
        </style>
    </head>
    <body>
        <div class="container mt-5 text-center">
            <h2>🎭 Select Seat</h2>

            <form action="Booking?action=booking&movieID=<%= request.getParameter("movieID")%>&showtimeID=<%= request.getParameter("showtimeID")%>"
                  method="post"
                  onsubmit="return validateSelection();">

                <div class="seat-container">
                    <%
                        int showtimeID = Integer.parseInt(request.getParameter("showtimeID"));
                        ShowtimesDAO showtimesDAO = new ShowtimesDAO();
                        Showtimes showtime = showtimesDAO.getShowtimeById(showtimeID);
                        if (showtime != null) {
                            int roomID = showtime.getRoomID();
                            RoomsDAO roomsDAO = new RoomsDAO();
                            Rooms room = roomsDAO.getRoomById(roomID);

                            SeatsDAO seatsDAO = new SeatsDAO();
                            Set<Integer> bookedSeats = seatsDAO.getBookedSeatIDs(room.getRoomID(), showtime.getShowtimeID()); // Lấy danh sách ghế đã đặt

                            int seatCapacity = room.getSeatCapacity();
                            int seatsPerRow = 20;
                            int totalRows = (int) Math.ceil((double) seatCapacity / seatsPerRow);

                            char rowLabel = 'A';
                            int seatID = 1;

                            for (int row = 0; row < totalRows; row++) {
                                for (int col = 1; col <= seatsPerRow; col++) {
                                    if (seatID > seatCapacity) {
                                        break;
                                    }
                                    String seatLabel = rowLabel + String.valueOf(col);
                                    boolean isBooked = bookedSeats.contains(seatID); // Kiểm tra ghế có trong danh sách đã đặt không
                    %>
                    <div class="seat <%= isBooked ? "booked" : ""%>"
                         id="seat-<%= seatID%>"
                         <% if (!isBooked) { %> 
                             onclick="toggleSeat(this, '<%= seatLabel%>', '<%= seatID%>')"
                         <% } %>>
                        <%= seatLabel%>
                    </div>

                    <% if (!isBooked) { %>
                        <input type="checkbox" name="selectedSeats" value="<%= seatLabel%>" id="checkbox-<%= seatLabel%>" hidden>
                        <input type="checkbox" name="IDSeats" value="<%= seatID%>" id="checkbox-<%= seatID%>" hidden>
                    <% } %>

                    <%
                                    seatID++;
                                }
                                rowLabel++;
                            }
                        }
                    %>
                </div>
                
                <a href="Booking?action=showtime&movieID=<%= request.getParameter("movieID")%>" class="btn btn-secondary mt-3">Back</a>
                <button type="submit" class="btn btn-primary mt-3">Confirm</button>
            </form>
        </div>

        <script>
            function validateSelection() {
                const selectedSeats = document.querySelectorAll('input[name="selectedSeats"]:checked');
                if (selectedSeats.length === 0) {
                    alert("Vui lòng chọn ít nhất 1 ghế để tiếp tục!");
                    return false;
                }
                return true;
            }

            function toggleSeat(seatElement, seatLabel, seatID) {
                seatElement.classList.toggle('selected');

                let seatCheckbox = document.getElementById('checkbox-' + seatLabel);
                let idSeatCheckbox = document.getElementById('checkbox-' + seatID);

                if (seatCheckbox && idSeatCheckbox) {
                    seatCheckbox.checked = !seatCheckbox.checked;
                    idSeatCheckbox.checked = !idSeatCheckbox.checked;
                }

                console.log("Ghế được chọn:", seatLabel, "ID:", seatID, "Trạng thái:", seatCheckbox.checked);
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
