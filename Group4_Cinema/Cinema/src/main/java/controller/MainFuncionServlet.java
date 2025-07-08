/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.sql.Date;
import model.Movie;

/**
 *
 * @author HoangSang
 */
@WebServlet(name = "MainFuncionServlet", urlPatterns = {"/MainFuncion"})
public class MainFuncionServlet extends HttpServlet {

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
            out.println("<title>Servlet MainFuncionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MainFuncionServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("movie");
        switch (action.toLowerCase()) {
           
            case "main-cinema":
                request.getRequestDispatcher("/main/main-cinema.jsp").forward(request, response);
                break;

            case "main-admin":
                request.getRequestDispatcher("/main/main-admin.jsp").forward(request, response);
                break;

            case "view":
                request.getRequestDispatcher("/main/movie-list.jsp").forward(request, response);
                break;

            case "edit-list":
                request.getRequestDispatcher("/main/movie-edit-list.jsp").forward(request, response);
                break;
            case "delete":
                request.getRequestDispatcher("/main/movie-delete.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/main/main-admin.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("movie");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id")); // Lấy ID phim
                MovieDAO movieDAO = new MovieDAO();

                // Lấy thông tin phim từ database
                Movie movie = movieDAO.getMovieById(id);
                if (movie == null) {
                    request.setAttribute("errorMessage", "Movie does not exist.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                // Xác định đường dẫn file ảnh
                String imagePath = getServletContext().getRealPath("/images/images-movie") + File.separator + movie.getImagePath();

                File imageFile = new File(imagePath);

                // Xóa phim trong database
                boolean deleted = movieDAO.deleteMovie(id);

                // Nếu xóa thành công thì xóa luôn file ảnh
                if (deleted) {
                    if (imageFile.exists()) {
                        imageFile.delete(); // Xóa ảnh
                    }
                    response.sendRedirect(request.getContextPath() + "/MainFuncion?movie=edit-list");
                } else {
                    request.setAttribute("errorMessage", "Movie deletion failed. Try again!");
                    request.getRequestDispatcher("/MainFuncion?movie=delete").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/MainFuncion?movie=edit-list");
            }
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
