/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import shop.dao.StaffDAO;
import shop.model.Staff;
import shop.util.PasswordUtils;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "LoginDashboardServlet", urlPatterns = {"/login-dashboard"})
public class LoginDashboardServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/dashboard/login-dashboard.jsp").forward(request, response);
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

        // Retrieve form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        StaffDAO sDAO = new StaffDAO();
        Staff staff = sDAO.getOneByEmail(email);

        if (staff != null) {
            // Check password match before setting session
            boolean isPasswordMatched = PasswordUtils.checkPassword(password, staff.getPasswordHash());
            if (isPasswordMatched) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentEmployee", staff); // Session CurrentEmployee

                if (staff != null) {
                    // Check if session contains currentCustomer
                    if (staff.getRole().equalsIgnoreCase("admin")) {
                        // Logic for customer
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                    } // Check if session contains currentEmployee
                    else if (staff.getRole().equalsIgnoreCase("staff")) {
                        // Logic for employee
                        response.sendRedirect(request.getContextPath() + "/manage-orders");
                    }
                } else {
                    request.setAttribute("message", "Current session is null");
                    request.getRequestDispatcher("/WEB-INF/dashboard/login-dashboard.jsp").forward(request, response);
                }
            } else {
                // If password doesn't match, set error message and forward to login page
                request.setAttribute("email", email);
                request.setAttribute("password", password);
                request.setAttribute("message", "Email or password is incorrect. Try again.");
                request.getRequestDispatcher("/WEB-INF/dashboard/login-dashboard.jsp").forward(request, response);
            }
        } else {
            // If email doesn't exist, set error message and forward to login page
            request.setAttribute("email", email);
            request.setAttribute("password", password);
            request.setAttribute("message", "Email doesn't exist.");
            request.getRequestDispatcher("/WEB-INF/dashboard/login-dashboard.jsp").forward(request, response);
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
