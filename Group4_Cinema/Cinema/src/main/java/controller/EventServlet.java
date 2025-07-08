/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.EventDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Date;
import model.Event;

/**
 *
 * @author HoangSang
 */
@MultipartConfig
@WebServlet(name = "EventServlet", urlPatterns = {"/Event"})
public class EventServlet extends HttpServlet {

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
            out.println("<title>Servlet EventServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EventServlet at " + request.getContextPath() + "</h1>");
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
        EventDAO eventDAO = new EventDAO();
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("main/event-list.jsp");
            return;
        }

        try {
            switch (action.toLowerCase()) {
                case "event-details":
                    int eventId = Integer.parseInt(request.getParameter("id"));
                    Event event = eventDAO.getEventById(eventId);
                    request.setAttribute("event", event);
                    request.getRequestDispatcher("main/event-details.jsp").forward(request, response);
                    break;
                case "event-add":
                    request.getRequestDispatcher("main/event-add.jsp").forward(request, response);
                    break;

                case "event-list":
                    request.setAttribute("eventList", eventDAO.getAllEvents());
                    request.getRequestDispatcher("main/event-list.jsp").forward(request, response);
                    break;
                case "event-edit":
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Event event1 = eventDAO.getEventById(editId);
                    if (event1 != null) {
                        request.setAttribute("event", event1);
                        request.getRequestDispatcher("main/event-edit.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("Event?action=event-list");
                    }
                    break;
                case "event-delete":
                    request.getRequestDispatcher("main/event-delete.jsp").forward(request, response);
                    break;
                default:
                    response.sendRedirect("Event?action=event-list");
                    break;
            }
        } catch (Exception e) {
            response.sendRedirect("Event?action=event-list");
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
        EventDAO eventDAO = new EventDAO();
        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("Event?action=event-list");
            return;
        }

        try {
            if ("event-add".equalsIgnoreCase(action)) {
                // Lấy dữ liệu từ form
                String title = request.getParameter("title");
                String description = request.getParameter("description");
                String dateStr = request.getParameter("eventDate");
                int cinemaID = Integer.parseInt(request.getParameter("cinemaID"));
                double discountPercentage = Double.parseDouble(request.getParameter("discountPercentage"));
                Part filePart = request.getPart("imageFile");

                if (title == null || description == null || dateStr == null || filePart == null || filePart.getSize() == 0) {
                    response.sendRedirect("Event?action=event-add");
                    return;
                }
                // Xử lý ảnh mới (nếu có)
                String uniqueFileName = null;
                Date eventDate = Date.valueOf(dateStr);

                String uploadDir = request.getServletContext().getRealPath("/images/images-event");
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                String fileName = filePart.getSubmittedFileName();
                uniqueFileName = eventDAO.getUniqueFileName(uploadDir, fileName);
                String filePath = uploadDir + File.separator + uniqueFileName;
                filePart.write(filePath);
                String relativeFilePath = fileName;
                int newId = eventDAO.getNextEventId();
                eventDAO.addEvent(new Event(newId, title, description, eventDate, cinemaID, discountPercentage, relativeFilePath));

                response.sendRedirect("Event?action=event-list");
            } else if ("event-edit".equalsIgnoreCase(action)) {
                try {
                    int eventId = Integer.parseInt(request.getParameter("id"));

                    Event existingEvent = eventDAO.getEventById(eventId);
                    if (existingEvent == null) {
                        response.sendRedirect("Event?action=event-edit");
                        return;
                    }

                    String title = request.getParameter("title");
                    title = (title == null || title.trim().isEmpty()) ? existingEvent.getName() : title;

                    String description = request.getParameter("description");
                    description = (description == null || description.trim().isEmpty()) ? existingEvent.getDescription() : description;

                    String dateStr = request.getParameter("eventDate");
                    Date eventDate = (dateStr == null || dateStr.trim().isEmpty()) ? existingEvent.getEventDate() : Date.valueOf(dateStr);

                    String cinemaIDStr = request.getParameter("cinemaID");
                    int cinemaID = (cinemaIDStr == null || cinemaIDStr.trim().isEmpty()) ? existingEvent.getCinemaID() : Integer.parseInt(cinemaIDStr);

                    String discountStr = request.getParameter("discountPercentage");
                    double discountPercentage = (discountStr == null || discountStr.trim().isEmpty()) ? existingEvent.getDiscountPercentage() : Double.parseDouble(discountStr);

                    String uniqueFileName = null;
                    Part filePart = request.getPart("imageFile");
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = filePart.getSubmittedFileName();
                        String uploadDir = request.getServletContext().getRealPath("/images/images-event");

                        File uploadFolder = new File(uploadDir);
                        if (!uploadFolder.exists()) {
                            uploadFolder.mkdirs();
                        }

                        uniqueFileName = eventDAO.getUniqueFileName(uploadDir, fileName);
                        String filePath = uploadDir + File.separator + uniqueFileName;
                        filePart.write(filePath);
                    }

                    // Nếu không có ảnh mới, giữ nguyên ảnh cũ
                    String imageToUpdate = (uniqueFileName != null) ? uniqueFileName : existingEvent.getImagePath();

                    // Cập nhật sự kiện với các giá trị mới hoặc giữ nguyên giá trị cũ nếu không nhập mới
                    eventDAO.updateEvent(new Event(eventId, title, description, eventDate, cinemaID, discountPercentage, imageToUpdate));

                    response.sendRedirect("Event?action=event-list");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Event?action=event-list");
                }

            } else if ("event-delete".equalsIgnoreCase(action)) {
                try {
                    int deleteId = Integer.parseInt(request.getParameter("id"));

                    // Lấy thông tin sự kiện cần xóa
                    Event event = eventDAO.getEventById(deleteId);
                    if (event == null) {
                        request.setAttribute("errorMessage", "Event does not exist.");
                        request.getRequestDispatcher("main/event-list.jsp").forward(request, response);
                        return;
                    }

                    // Xác định đường dẫn file ảnh
                    String imagePath = getServletContext().getRealPath("/images/images-event") + File.separator + event.getImagePath();
                    File imageFile = new File(imagePath);

                    // Xóa sự kiện trong database trước
                    boolean deleted = eventDAO.deleteEvent(deleteId);

                    // Nếu xóa trong database thành công, tiếp tục xóa ảnh
                    if (deleted) {
                        if (imageFile.exists()) {
                            boolean fileDeleted = imageFile.delete();
                            System.out.println("Image deleted: " + fileDeleted);
                        } else {
                            System.out.println("Image file not found: " + imagePath);
                        }
                        response.sendRedirect("Event?action=event-list");
                    } else {
                        request.setAttribute("errorMessage", "Event deletion failed. Try again!");
                        request.getRequestDispatcher("main/event-list.jsp").forward(request, response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("main/event-list.jsp?error=invalidRequest");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("main/event-list.jsp?error=invalidRequest");
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
