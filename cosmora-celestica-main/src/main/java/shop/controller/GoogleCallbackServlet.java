/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import shop.dao.CustomerDAO;
import shop.model.Customer;
import shop.util.PasswordUtils;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(name = "GoogleCallbackServlet", urlPatterns = {"/oauth2callback"})
public class GoogleCallbackServlet extends HttpServlet {

    private static final String CLIENT_ID = "269154426187-nkt8qnsov2rjis59n8ji48lfuop0e4on.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "http://localhost:8080/CosmoraCelestica/oauth2callback";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://oauth2.googleapis.com/token",
                CLIENT_ID,
                CLIENT_SECRET,
                code,
                REDIRECT_URI
        ).execute();

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Get info from Google
        String name = (String) payload.get("name");
        String email = payload.getEmail();
        String googleId = payload.getSubject(); // Unique ID for the user from Google

        CustomerDAO cDAO = new CustomerDAO();
        Customer customer = cDAO.getAccountByEmail(email);

        if (customer != null) {
            if (!customer.isIsDeactivated()) {
                if (customer.getGoogleId() == null) {
                    // Account exists but is not linked to Google
                    customer.setGoogleId(googleId);
                    if (cDAO.bindGoogleAccountAndUpdateLastLoginTime(customer) > 0) {

                        HttpSession session = request.getSession();
                        session.setAttribute("currentCustomer", customer);

                        response.sendRedirect(request.getContextPath() + "/home");
                    } else {
                        request.setAttribute("message", "We encountered an issue with your login. Please check your credentials or contact support.");
                        request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                    }
                } else {
                    // Account exists and is already linked to google
                    if (cDAO.updateLastLoginTime(customer) > 0) {

                        HttpSession session = request.getSession();
                        session.setAttribute("currentCustomer", customer);

                        response.sendRedirect(request.getContextPath() + "/home");
                    } else {
                        request.setAttribute("message", "Your account has been deactivated. Please contact support.");
                        request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
                    }
                }
                // Else of deactivated account
            } else {
                request.setAttribute("message", "We encountered an issue with your login. Please check your credentials or contact support.");
                request.getRequestDispatcher("/WEB-INF/home/login.jsp").forward(request, response);
            }
        } else {
            // New Google user
            customer = new Customer();
            customer.setFullName(name);
            customer.setUsername(name);
            customer.setEmail(email);
            customer.setAvatarUri(request.getContextPath() + "/assets/img/avatar/avatar1.png");
            customer.setHasSetPassword(false);
            customer.setGoogleId(googleId);
            customer.setCreatedAt(Timestamp.from(Instant.now()));

            String tempPass = PasswordUtils.generateTemporaryPassword(12);
            String hashedTempPass = PasswordUtils.hashPassword(tempPass);

            customer.setPasswordHash(hashedTempPass);

            if (cDAO.createGoogleCustomerAccount(customer) > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("currentCustomer", customer);

                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute("message", "We couldn't complete your registration at the moment. Please try again later.");
                request.getRequestDispatcher("/WEB-INF/home/register.jsp").forward(request, response);
            }
        }
    }
}
