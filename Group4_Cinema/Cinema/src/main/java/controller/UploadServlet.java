/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Movie;
import utils.DBContext;

/**
 *
 * @author HoangSang
 */
@MultipartConfig
@WebServlet(name = "UploadServlet", urlPatterns = {"/Upload"})
public class UploadServlet extends HttpServlet {

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
            out.println("<title>Servlet UploadServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadServlet at " + request.getContextPath() + "</h1>");
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
        String action = request.getParameter("action");

        switch (action.toLowerCase()) {
            case "add":
                request.getRequestDispatcher("/main/movie-add.jsp").forward(request, response);
                break;
            case "edit":
                request.getRequestDispatcher("/main/movie-edit.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("main/movie-list.jsp").forward(request, response);
                break;

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
        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        int duration = Integer.parseInt(request.getParameter("duration"));
        Date releaseDate = Date.valueOf(request.getParameter("releaseDate"));
        String description = request.getParameter("description");

        MovieDAO movieDAO = new MovieDAO();
        String uniqueFileName = null; // Khởi tạo tên file mới

        // Xử lý file upload
        Part filePart = request.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) { // Kiểm tra có chọn ảnh không
            String fileName = filePart.getSubmittedFileName();
            String uploadDir = request.getServletContext().getRealPath("/images/images-movie");

            // Đảm bảo thư mục tồn tại
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }

            // Kiểm tra và đổi tên file nếu bị trùng
            uniqueFileName = movieDAO.getUniqueFileName(uploadDir, fileName);
            String filePath = uploadDir + File.separator + uniqueFileName;

            // Lưu file vào thư mục images
            filePart.write(filePath);
        }

        if (idParam == null || idParam.isEmpty()) {
            // Thêm phim mới (phải có ảnh)
            if (uniqueFileName == null) {
                response.sendRedirect(request.getContextPath() + "/addMovie.jsp?error=missingImage");
                return;
            }
            int newId = movieDAO.getNextMovieId();
            movieDAO.addMovie(newId, title, genre, duration, releaseDate, description, uniqueFileName);
        } else {
            // Cập nhật phim
            int id = Integer.parseInt(idParam);
            Movie movie = movieDAO.getMovieById(id);
            if (movie != null) {
                movie.setTitle(title);
                movie.setGenre(genre);
                movie.setDuration(duration);
                movie.setReleaseDate(releaseDate);
                movie.setDescription(description);

                // Nếu không có ảnh mới, giữ ảnh cũ
                if (uniqueFileName == null) {
                    uniqueFileName = movie.getImagePath();
                }
                movie.setImagePath(uniqueFileName);

                movieDAO.updateMovie(movie);
            }
        }

        // Chuyển hướng sau khi hoàn tất
        response.sendRedirect(request.getContextPath() + "/MainFuncion?movie=edit-list");
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
