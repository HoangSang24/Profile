/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.BookingDAO;
import dao.ShowtimesDAO;
import dao.SeatsDAO;
import dao.TicketDAO;
//import models.Bookings;
//import models.Showtimes;
//import models.Seats;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.BookingInfo;
import model.Seats;
import model.Showtimes;

/**
 *
 * @author Phong
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/Booking"})
public class BookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;
    private ShowtimesDAO showtimesDAO;
    private SeatsDAO seatsDAO;

    @Override
    public void init() throws ServletException {
        bookingDAO = new BookingDAO();
        showtimesDAO = new ShowtimesDAO();
        seatsDAO = new SeatsDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet BookingServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BookingServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");
        String action = request.getParameter("action");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        try {
            switch (action) {
                case "showtime":
                    int movieId = Integer.parseInt(request.getParameter("movieID"));
                    List<Showtimes> showtimes = showtimesDAO.getShowtimesByMovie(movieId);
                    request.setAttribute("showtimes", showtimes);
                    request.getRequestDispatcher("main/select-showtime.jsp").forward(request, response);
                    break;

                case "seats":
                    int showtimeId = Integer.parseInt(request.getParameter("showtimeID"));
                    Showtimes showtime = showtimesDAO.getShowtimeById(showtimeId);
                    List<Seats> availableSeats = seatsDAO.getAvailableSeatsByShowtime(showtimeId);

                    request.setAttribute("showtimeId", showtimeId);
                    request.setAttribute("showtime", showtime);
                    request.setAttribute("availableSeats", availableSeats);
                    request.getRequestDispatcher("main/select-seat.jsp").forward(request, response);
                    break;

                case "booking":
                    int movieId1 = Integer.parseInt(request.getParameter("movieID"));
                    List<BookingInfo> bookingList = bookingDAO.getBookingDetailsByMovieId(movieId1);
                    request.setAttribute("bookingList", bookingList);
                    request.getRequestDispatcher("main/booking-summary.jsp").forward(request, response);
                    break;

                case "confirm-booking":
                    request.getRequestDispatcher("main/booking-success.jsp").forward(request, response);
                    break;

                default:
                    request.getRequestDispatcher("main/booking-summary.jsp").forward(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Lỗi: Dữ liệu không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userID");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("booking".equals(action)) {
                int showtimeId = Integer.parseInt(request.getParameter("showtimeID"));
                int movieId = Integer.parseInt(request.getParameter("movieID"));
                String[] selectedSeats = request.getParameterValues("selectedSeats");

                if (selectedSeats == null || selectedSeats.length == 0) {
                    request.setAttribute("errorMessage", "Vui lòng chọn ít nhất một ghế!");
                    request.getRequestDispatcher("Booking?action=seats&showtimeID=" + showtimeId).forward(request, response);
                    return;
                }

                request.setAttribute("selectedSeats", selectedSeats);
                request.setAttribute("showtimeID", showtimeId);
                request.setAttribute("movieID", movieId);
                request.getRequestDispatcher("main/booking-summary.jsp").forward(request, response);
            } else if ("confirm-booking".equals(action)) {
                int showtimeId = Integer.parseInt(request.getParameter("showtimeID"));
                String status = request.getParameter("status");//Confirmed
                String discountCode = request.getParameter("discountCode");
                int roomID = Integer.parseInt(request.getParameter("roomID"));

                String[] IDSeats = request.getParameterValues("IDSeats"); // ID ghế
                String[] selectedSeats = request.getParameterValues("selectedSeats"); // Tên ghế

                if (IDSeats == null || selectedSeats == null || IDSeats.length != selectedSeats.length) {
                    request.setAttribute("errorMessage", "Lỗi: Dữ liệu ghế không hợp lệ!");
                    request.getRequestDispatcher("Booking?action=seats&showtimeID=" + showtimeId).forward(request, response);
                    return;
                }

                SeatsDAO seatDAO = new SeatsDAO();
                boolean allSuccess = true;

                for (int i = 0; i < IDSeats.length; i++) {
                    try {
                        int seatId = Integer.parseInt(IDSeats[i]);
                        String seatName = selectedSeats[i]; // Tên ghế

                        // Lưu ghế vào database
                        Seats newSeat = new Seats(seatId, roomID, seatName, "Booked");
                        seatDAO.addSeat(newSeat);

//                        // Đặt vé vào database
                        boolean success = bookingDAO.insertBooking(userId, showtimeId, seatId, status, discountCode);

                        TicketDAO DAO = new TicketDAO();

                        List<BookingInfo> bookingList = bookingDAO.getBookingBySeatId(seatId);

                        if (!bookingList.isEmpty()) {

                            for (BookingInfo booking : bookingList) {
                                int a = booking.getBookingID();
                                double b = booking.getPrice();
                                DAO.addTicket(a, b, "Valid");
                            }
                        }
                        if (!success) {
                            allSuccess = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        allSuccess = false;
                        break;
                    }
                }

                if (allSuccess) {
                    response.sendRedirect("main/booking-success.jsp");
                } else {
                    request.setAttribute("errorMessage", "Đặt vé thất bại! Vui lòng thử lại.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Lỗi: Dữ liệu không hợp lệ!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
