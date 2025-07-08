/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
import java.nio.file.Paths;

/**
 *
 * @author HoangSang
 */
@MultipartConfig
@WebServlet(name = "UploadCarouselServlet", urlPatterns = {"/UploadCarousel"})
public class UploadCarouselServlet extends HttpServlet {

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
            out.println("<title>Servlet UploadCarouselServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadCarouselServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

   private static final String UPLOAD_DIR = "images/images-carousel";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("main/add-carousel.jsp").forward(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("main/delete-carousel.jsp").forward(request, response);
        } else {
            response.sendRedirect("main/add-carousel.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            addImage(request, response);
        } else if ("delete".equalsIgnoreCase(action)) {
            deleteImage(request, response);
        } else {
            response.sendRedirect("main/add-carousel.jsp");
        }
    }

    private void addImage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            Part filePart = request.getPart("carouselImage");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));

                if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|webp)$")) {
                    request.setAttribute("message", "Invalid file type! Only images allowed.");
                    request.getRequestDispatcher("main/add-carousel.jsp").forward(request, response);
                    return;
                }

                File file = new File(uploadPath, fileName);
                int count = 1;
                while (file.exists()) {
                    fileName = "copy_" + count + "_" + file.getName();
                    file = new File(uploadPath, fileName);
                    count++;
                }

                filePart.write(uploadPath + File.separator + fileName);
                request.setAttribute("message", "Upload successful!");
            } else {
                request.setAttribute("message", "No file selected!");
            }
        } catch (Exception e) {
            request.setAttribute("message", "File upload failed: " + e.getMessage());
        }

        request.getRequestDispatcher("main/add-carousel.jsp").forward(request, response);
    }

    private void deleteImage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fileName = request.getParameter("imageName");
        if (fileName == null || fileName.isEmpty()) {
            request.setAttribute("message", "Invalid file name!");
            request.getRequestDispatcher("main/delete-carousel.jsp").forward(request, response);
            return;
        }

        String filePath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR + File.separator + fileName;
        File file = new File(filePath);

        if (file.exists() && file.delete()) {
            request.setAttribute("message", "Image deleted successfully!");
        } else {
            request.setAttribute("message", "Failed to delete image!");
        }

        request.getRequestDispatcher("main/delete-carousel.jsp").forward(request, response);
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
