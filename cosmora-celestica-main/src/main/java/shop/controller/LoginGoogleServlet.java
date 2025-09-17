package shop.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
@WebServlet(urlPatterns={"/login-google"})
public class LoginGoogleServlet extends HttpServlet {
    private static final String CLIENT_ID = "269154426187-nkt8qnsov2rjis59n8ji48lfuop0e4on.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:8080/CosmoraCelestica/oauth2callback";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + CLIENT_ID
                + "&response_type=code"
                + "&scope=openid%20email%20profile"
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                + "&access_type=offline";

        response.sendRedirect(oauthUrl);
    }
}
