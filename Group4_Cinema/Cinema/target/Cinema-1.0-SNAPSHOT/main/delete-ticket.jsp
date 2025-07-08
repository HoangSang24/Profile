<%@page import="dao.TicketDAO"%>
<%@page import="dao.AccountDAO"%>
<%@ page import="dao.EventDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    TicketDAO TicketDAO = new TicketDAO();
    TicketDAO.deleteTicket(id);
    response.sendRedirect(request.getContextPath()+"/main/booking-history.jsp");
%>