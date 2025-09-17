/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import shop.dao.CustomerDAO;
import shop.model.Customer;
import shop.util.EmailUtils;
import shop.util.SecurityTokenUtils;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

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
        request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp")
                .forward(request, response);
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

        // Action SendOtp
        if (action != null && action.equals("sendOtp")) {
            String email = request.getParameter("email");
            CustomerDAO cDAO = new CustomerDAO();

            String otp = SecurityTokenUtils.generateOTP(6);
            Timestamp expiry = Timestamp.from(Instant.now().plus(5, ChronoUnit.MINUTES));

            // Attempt to store OTP in the database
            if (cDAO.storeOtpForEmail(email, otp, expiry) > 0) {
                String to = email;
                String subject = "Your Cosmora Celestica OTP Code";
                String content = "Your OTP is: " + otp + "\nIt will expire in 5 minutes.";

                // Try sending the OTP email
                if (EmailUtils.sendEmail(to, subject, content)) {
                    // Set a success message and forward to a JSP page to display it
                    request.setAttribute("email", email);
                    request.setAttribute("message", "OTP sent successfully!");
                    request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
                } else {
                    // If sending OTP email fails, show error
                    request.setAttribute("email", email);
                    request.setAttribute("message", "Failed to send OTP to the email.");
                    request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
                }
            } else {
                // If storing the OTP in the database fails
                request.setAttribute("email", email);
                request.setAttribute("message", "Failed to store OTP.");
                request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
            }
        } // Action VerifyOtp
        else if (action != null && action.equals("verifyOtp")) {
            String email = request.getParameter("email");
            String otp = request.getParameter("otp");

            CustomerDAO cDAO = new CustomerDAO();
            Customer customer = cDAO.getAccountByEmailAndVerify(email);

            if (customer != null) {
                // Check if the OTP matches
                if (cDAO.checkOtpForEmail(email, otp)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("currentForgotCustomer", customer);

                    // Redirect to reset password page on success
                    response.sendRedirect(request.getContextPath() + "/reset-password");
                } else {
                    // If OTP is incorrect
                    request.setAttribute("email", email);
                    request.setAttribute("message", "We encountered an issue while processing your request. Please check your email and OTP, or contact support for assistance.");
                    request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
                }
            } else {
                // If the email doesn't exist
                request.setAttribute("email", email);
                request.setAttribute("message", "We encountered an issue while processing your request. Please check your email and OTP, or contact support for assistance.");
                request.getRequestDispatcher("/WEB-INF/home/forgot-password.jsp").forward(request, response);
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
