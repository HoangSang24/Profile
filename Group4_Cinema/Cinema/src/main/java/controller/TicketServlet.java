/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.TicketDAO;
import dao.BookingDAO;
import model.Ticket;
import model.Booking;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TicketServlet")
public class TicketServlet extends HttpServlet {
    private TicketDAO ticketDAO;
    private BookingDAO bookingDAO;
    
    @Override
    public void init() {
        ticketDAO = new TicketDAO();
        bookingDAO = new BookingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        List<Ticket> tickets = ticketDAO.getAllTickets();
        List<Booking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("tickets", tickets);
        request.setAttribute("bookings", bookings); 
        request.getRequestDispatcher("main/addTicket.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
            } 
            else if ("update".equals(action)) {
                int ticketID = Integer.parseInt(request.getParameter("ticketID"));
                String status = request.getParameter("status");
                ticketDAO.updateTicket(ticketID, status);
            } 
            else if ("delete".equals(action)) {
                int ticketID = Integer.parseInt(request.getParameter("ticketID"));
                ticketDAO.deleteTicket(ticketID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("TicketServlet");
    }
}