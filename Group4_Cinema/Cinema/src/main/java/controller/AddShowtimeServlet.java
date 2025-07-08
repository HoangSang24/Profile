/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CinemaDAO;
import dao.MovieDAO;
import dao.RoomsDAO;
import dao.ShowtimesDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import model.Cinema;
import model.Movie;
import model.Rooms;
import model.Showtimes;

/**
 *
 * @author HoangSang
 */
@WebServlet(name = "AddShowtimeServlet", urlPatterns = {"/AddShowtime"})
public class AddShowtimeServlet extends HttpServlet {

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
            out.println("<title>Servlet AddShowtimeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddShowtimeServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");
        ShowtimesDAO showtimeDAO = new ShowtimesDAO();

        if (action == null || action.equals("list")) {
            List<Showtimes> showtimes = showtimeDAO.getShowtimesByMovie();
            request.setAttribute("showtimeList", showtimes);

            request.getRequestDispatcher("/main/showtime_list.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                case "add":
                    MovieDAO movieDAO = new MovieDAO();
                    CinemaDAO cinemaDAO = new CinemaDAO();
                    RoomsDAO roomDAO = new RoomsDAO();

                    List<Movie> movies = movieDAO.getAllMovies();
                    List<Cinema> cinemas = cinemaDAO.getAllCinemas();
                    List<Rooms> rooms = roomDAO.getAllRooms();

                    request.setAttribute("movies", movies);
                    request.setAttribute("cinemas", cinemas);
                    request.setAttribute("rooms", rooms);
                    request.getRequestDispatcher("/main/showtime_add.jsp").forward(request, response);
                    break;

                case "edit":
                    int showtimeID = Integer.parseInt(request.getParameter("id"));
                    Showtimes showtime = showtimeDAO.getShowtimeById(showtimeID);

                    if (showtime == null) {
                        response.sendRedirect("AddShowtime?action=list&error=notfound");
                        return;
                    }

                    movieDAO = new MovieDAO();
                    cinemaDAO = new CinemaDAO();
                    roomDAO = new RoomsDAO();

                    List<Movie> moviesEdit = movieDAO.getAllMovies();
                    List<Cinema> cinemasEdit = cinemaDAO.getAllCinemas();
                    List<Rooms> roomsEdit = roomDAO.getAllRooms();

                    request.setAttribute("showtime", showtime);
                    request.setAttribute("movies", moviesEdit);
                    request.setAttribute("cinemas", cinemasEdit);
                    request.setAttribute("rooms", roomsEdit);
                    request.getRequestDispatcher("/main/showtime_edit.jsp").forward(request, response);
                    break;

                case "delete":
                try {
                    int deleteID = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = showtimeDAO.deleteShowtime(deleteID);

                    if (deleted) {
                        response.sendRedirect("AddShowtime?action=list&success=deleted");
                    } else {
                        response.sendRedirect("AddShowtime?action=list&error=deletefailed");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect("AddShowtime?action=list&error=invalidid");
                }
                break;

                default:
                    response.sendRedirect("AddShowtime?action=list");
                    break;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("AddShowtime?action=list&error=invalidid");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AddShowtime?action=list&error=servererror");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        ShowtimesDAO showtimeDAO = new ShowtimesDAO();
        String message;

        try {
            int movieID = Integer.parseInt(request.getParameter("movieID"));
            int cinemaID = Integer.parseInt(request.getParameter("cinemaID"));
            int roomID = Integer.parseInt(request.getParameter("roomID"));
            Timestamp startTime = Timestamp.valueOf(request.getParameter("startTime").replace("T", " ") + ":00");
            double price = Double.parseDouble(request.getParameter("price"));

            if ("add".equals(action)) {
                boolean success = showtimeDAO.addShowtime(new Showtimes(movieID, cinemaID, roomID, startTime, price));
                message = success ? "Showtime added successfully!" : "Showtime added failed!";
            } else if ("edit".equals(action)) {
                int showtimeID = Integer.parseInt(request.getParameter("showtimeID"));
                boolean success = showtimeDAO.updateShowtime(new Showtimes(showtimeID, movieID, cinemaID, roomID, startTime, price));
                message = success ? "Showtime update successfully!" : "Update failed!";
            } else {
                message = "Invalid action!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error: " + e.getMessage();
        }

        request.setAttribute("message", message);
        doGet(request, response); // Chuyển về `doGet()` để load lại trang
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
