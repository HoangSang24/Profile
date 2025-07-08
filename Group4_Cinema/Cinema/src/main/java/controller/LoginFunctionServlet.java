/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDAO;
import dao.MovieDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;
import model.Account;
import model.Movie;
import utils.EmailUtility;

/**
 *
 * @author HoangSang
 */
@WebServlet(name = "LoginFunctionServlet", urlPatterns = {"/Function"})
public class LoginFunctionServlet extends HttpServlet {

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
            out.println("<title>Servlet LoginFunctionServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginFunctionServlet at " + request.getContextPath() + "</h1>");
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

            case "delete":
                 request.getRequestDispatcher("/login/delete-account.jsp").forward(request, response);
                break;
            case "forgot":
                request.getRequestDispatcher("/login/forgot-password.jsp").forward(request, response);
                break;

            case "verify":
                request.getRequestDispatcher("/login/verify-code.jsp").forward(request, response);
                break;

            case "create":
                request.getRequestDispatcher("/login/create-account.jsp").forward(request, response);
                break;

            case "reset":
                request.getRequestDispatcher("/login/reset-password.jsp").forward(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/login/login.jsp");
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
        HttpSession session = request.getSession();
        AccountDAO dao = new AccountDAO();
        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String code = request.getParameter("code");
        if (action == null) {
            response.sendRedirect("Login");
            return;
        }
        switch (action.toLowerCase()) {

            case "create":
                if (!password.equals(confirmPassword)) {
                    request.setAttribute("error", "Passwords do not match!");
                } else if (dao.isUsernameExists(username)) {
                    request.setAttribute("error", "Username already exists!");
                } else if (dao.isEmailExists(email)) {
                    request.setAttribute("error", "Email already exists!");
                } else {
                    Account newAcc = new Account(username, password, email);
                    boolean created = dao.createAccount(newAcc);
                    if (!created) {
                        request.setAttribute("error", "Error creating account!");
                    } else {
                        request.setAttribute("success", "Account created successfully!");
                    }
                }
                request.getRequestDispatcher("/login/create-account.jsp").forward(request, response);
                return;

            case "forgot": {
                if (email == null || !dao.isEmailExists(email)) {
                    request.setAttribute("error", "Email does not exist!");
                    request.getRequestDispatcher("/login/forgot-password.jsp").forward(request, response);
                    return;
                }

                session.setAttribute("email", email);

                String verificationCode = generateRandomCode();
                session.setAttribute("verificationCode", verificationCode);

                EmailUtility.sendEmail(email, "Your confirmation code", "Verify Code: " + verificationCode);

                request.setAttribute("success", "Check your email for the verification code!");
                request.getRequestDispatcher("/login/verify-code.jsp").forward(request, response);
                return;
            }

            case "verify": {
                String storedCode = (String) session.getAttribute("verificationCode");

                if (storedCode == null || code == null || !code.equals(storedCode)) {
                    request.setAttribute("error", "Verification code is incorrect!");
                    request.getRequestDispatcher("/login/verify-code.jsp").forward(request, response);
                    return;
                }

                session.removeAttribute("verificationCode");
                response.sendRedirect(request.getContextPath() + "/Function?action=reset");
                return;
            }

            case "reset": {
                // Lấy email từ form
                String emailreset = request.getParameter("email-reset");

                // Kiểm tra email hợp lệ
                if (emailreset == null || emailreset.isEmpty() || !dao.isEmailExists(emailreset)) {
                    request.setAttribute("error", "Email không hợp lệ hoặc không tồn tại!");
                    request.getRequestDispatcher("/login/reset-password.jsp").forward(request, response);
                    return;
                }

                // Lấy mật khẩu từ form
                String passwordreset = request.getParameter("password-reset");
                String confirmPasswordreset = request.getParameter("confirmPassword-reset");

                // Kiểm tra mật khẩu rỗng hoặc không khớp
                if (passwordreset == null || confirmPasswordreset == null || !passwordreset.equals(confirmPasswordreset)) {
                    request.setAttribute("error", "Mật khẩu không khớp! Vui lòng nhập lại.");
                    request.getRequestDispatcher("/login/reset-password.jsp").forward(request, response);
                    return;
                }

                // Cập nhật mật khẩu mới
                boolean updateSuccess = dao.updatePassword(emailreset, dao.hashMD5(passwordreset));
                if (!updateSuccess) {
                    request.setAttribute("error", "Đã xảy ra lỗi, vui lòng thử lại sau!");
                    request.getRequestDispatcher("/login/reset-password.jsp").forward(request, response);
                    return;
                }

                // Chuyển hướng về trang đăng nhập với thông báo thành công
                request.setAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("/login/login.jsp").forward(request, response);
                return;
            }

            default:
                response.sendRedirect("Login");
                return;
        }
    }

// Hàm tạo mã ngẫu nhiên 6 chữ số
    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
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
