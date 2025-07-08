package controller;

import dao.CinemaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Cinema;

/**
 * Servlet implementation class ManageCinemaServlet
 */
@WebServlet(name = "ManageCinemaServlet", urlPatterns = {"/ManageCinemaServlet"})
public class ManageCinemaServlet extends HttpServlet {

    private CinemaDAO cinemaDAO;

    @Override
    public void init() throws ServletException {
        cinemaDAO = new CinemaDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            String action = request.getParameter("action");
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "add":
                    addCinema(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateCinema(request, response);
                    break;
                case "view":
                    viewCinema(request, response);
                    break;
                case "delete":
                    deleteCinema(request, response);
                    break;
                default:
                    listCinemas(request, response);
                    break;
            }
        }
    }

    private void addCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        int totalRoom = Integer.parseInt(request.getParameter("totalRoom"));

        Cinema cinema = new Cinema();
        cinema.setCinemaID(cinemaDAO.getNextCinemaId());
        cinema.setName(name);
        cinema.setLocation(location);
        cinema.setTotalRoom(totalRoom);

        cinemaDAO.addCinema(cinema);
        response.sendRedirect(request.getContextPath() + "/ManageCinemaServlet?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cinema cinema = cinemaDAO.viewCinema(id);
        request.setAttribute("cinema", cinema);
        request.getRequestDispatcher("/main/cinema-edit.jsp").forward(request, response);
    }

    private void updateCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        int totalRoom = Integer.parseInt(request.getParameter("totalRoom"));

        System.out.println("Updating Cinema: ID=" + id + ", Name=" + name + ", Location=" + location + ", TotalRoom=" + totalRoom);

        Cinema cinema = new Cinema();
        cinema.setCinemaID(id);
        cinema.setName(name);
        cinema.setLocation(location);
        cinema.setTotalRoom(totalRoom);

        cinemaDAO.editCinema(cinema);
        response.sendRedirect(request.getContextPath() + "/ManageCinemaServlet?action=list");
    }

    private void viewCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cinema cinema = cinemaDAO.viewCinema(id);
        request.setAttribute("cinema", cinema);
        request.getRequestDispatcher("/main/cinema-edit.jsp").forward(request, response);
    }

    private void deleteCinema(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        cinemaDAO.deleteCinema(id);
        response.sendRedirect(request.getContextPath() + "/ManageCinemaServlet?action=list");
    }

    private void listCinemas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cinema> cinemas = cinemaDAO.getAllCinemas();
        request.setAttribute("cinemas", cinemas);
        request.getRequestDispatcher("/main/cinema-list.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "ManageCinemaServlet";
    }
}
