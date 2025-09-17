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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.VouchersDAO;
import shop.model.Checkout;
import shop.model.Voucher;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ApplyVoucherServlet", urlPatterns = {"/ApplyVoucherServlet"})
public class ApplyVoucherServlet extends HttpServlet {

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
            out.println("<title>Servlet ApplyVoucherServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApplyVoucherServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        String voucher = request.getParameter("voucher");
        VouchersDAO VD = new VouchersDAO();
        double total = (double) session.getAttribute("totalAmount");
        double vouchervalue = 0.0;
        try {
            if (VD.checkCodeIsValid(voucher)[0].equalsIgnoreCase("Valid") && total >= Double.parseDouble(VD.checkCodeIsValid(voucher)[2])) {
                vouchervalue = Double.parseDouble(VD.checkCodeIsValid(voucher)[1]);
                int voucherID = VD.getVoucherIdByCode(voucher);
                VD.decreaseLimit(voucherID);
                ArrayList<Checkout> product = (ArrayList<Checkout>) session.getAttribute("checkout");
                VouchersDAO vD = new VouchersDAO();
                ArrayList<Voucher> voucherslist = vD.getList();
                request.setAttribute("voucherslist", voucherslist);

                session.setAttribute("checkout", product);
                request.setAttribute("voucherApplied", true);
                request.setAttribute("voucherCode", voucher);
                session.setAttribute("voucherValue", vouchervalue);
                request.getRequestDispatcher("/WEB-INF/home/checkout.jsp").forward(request, response);

            } else {
                VouchersDAO vD = new VouchersDAO();
                ArrayList<Voucher> voucherslist = vD.getList();
                request.setAttribute("voucherslist", voucherslist);
                ArrayList<Checkout> product = (ArrayList<Checkout>) session.getAttribute("checkout");
                session.setAttribute("checkout", product);
                session.setAttribute("totalAmount", total);
                session.setAttribute("applyfail", "Your code is invalid or cannot be applied to this order.");
                System.out.println("Voucher apply failed. Setting session attribute 'applyfail'."); // Thêm dòng này để debug

                request.getRequestDispatcher("/WEB-INF/home/checkout.jsp").forward(request, response);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ApplyVoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
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
