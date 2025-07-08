/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Account;

/**
 *
 * @author HoangSang
 */
@WebServlet(name = "GrantAdminServlet", urlPatterns = {"/GrantAdmin"})
public class GrantAdminServlet extends HttpServlet {

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
            out.println("<title>Servlet GrantAdminServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GrantAdminServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("main/admin-dashboard.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDAO accountDAO = new AccountDAO();

        // Lấy danh sách userIds từ JSP
        String[] userIds = request.getParameterValues("userIds");
        // Lấy danh sách roles từ JSP
        String[] roles = request.getParameterValues("roles");

        boolean success = true;

        // Kiểm tra nếu dữ liệu hợp lệ
        if (userIds != null && roles != null && userIds.length == roles.length) {
            for (int i = 0; i < userIds.length; i++) {
                try {
                    int id = Integer.parseInt(userIds[i]); // Chuyển userId thành số nguyên
                    String selectedRole = roles[i]; // Lấy vai trò tương ứng

                    // Cập nhật role nếu khác giá trị hiện tại
                    if (!accountDAO.updateRole(id, selectedRole)) {
                        success = false;
                    }
                } catch (NumberFormatException e) {
                    success = false; // Nếu có lỗi khi parse ID, báo lỗi
                }
            }
        } else {
            success = false;
        }

        // Gửi thông báo về JSP
        if (success) {
            request.setAttribute("message", "Roles updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update roles.");
        }

        request.getRequestDispatcher("/main/admin-dashboard.jsp").forward(request, response);
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
