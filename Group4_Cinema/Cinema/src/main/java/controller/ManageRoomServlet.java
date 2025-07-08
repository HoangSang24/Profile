package controller;

import dao.RoomsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import model.Cinema;
import model.Rooms;

/**
 * Servlet implementation class ManageRoomServlet
 */
@WebServlet(name = "ManageRoomServlet", urlPatterns = {"/ManageRoomServlet"})
public class ManageRoomServlet extends HttpServlet {

    private RoomsDAO roomsDAO;

    @Override
    public void init() throws ServletException {
        roomsDAO = new RoomsDAO();
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
                    addRoom(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "update":
                    updateRoom(request, response);
                    break;
                case "delete":
                    deleteRoom(request, response);
                    break;
                default:
                    listRooms(request, response);
                    break;
            }
        }
    }

    private void addRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int cinemaID = Integer.parseInt(request.getParameter("cinemaID"));
        String roomName = request.getParameter("roomName");
        String location = request.getParameter("Location");
        int seatCapacity = Integer.parseInt(request.getParameter("seatCapacity"));

        Rooms room = new Rooms();
        room.setRoomID(roomsDAO.getNextRoomId());
        room.setCinemaID(cinemaID);
        room.setRoomName(roomName);
        room.setLocation(location);
        room.setSeatCapacity(seatCapacity);

        roomsDAO.addRoom(room);
        response.sendRedirect(request.getContextPath() + "/ManageRoomServlet?action=list");
    }

//    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        Rooms room = roomsDAO.viewRoom(id);
//        request.setAttribute("room", room);
//        request.getRequestDispatcher("/main/room-edit.jsp").forward(request, response);
//    }
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Rooms room = roomsDAO.viewRoom(id);
        List<Cinema> cinemas = roomsDAO.getAllCinemas(); // Lấy danh sách rạp từ DB

        request.setAttribute("room", room);
        request.setAttribute("cinemas", cinemas); // Truyền danh sách rạp sang JSP
        request.getRequestDispatcher("/main/room-edit.jsp").forward(request, response);
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int cinemaID = Integer.parseInt(request.getParameter("cinemaID"));
        String roomName = request.getParameter("roomName");
        int seatCapacity = Integer.parseInt(request.getParameter("seatCapacity"));

        Rooms room = new Rooms();
        room.setRoomID(id);
        room.setCinemaID(cinemaID);
        room.setRoomName(roomName);
        room.setSeatCapacity(seatCapacity);

        roomsDAO.updateRoom(room);
        response.sendRedirect(request.getContextPath() + "/ManageRoomServlet?action=list");
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        roomsDAO.deleteRoom(id);
        response.sendRedirect(request.getContextPath() + "/ManageRoomServlet?action=list");
    }

    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Rooms> rooms = roomsDAO.getAllRooms();
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/main/room-list.jsp").forward(request, response);
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
        return "ManageRoomServlet";
    }
}
