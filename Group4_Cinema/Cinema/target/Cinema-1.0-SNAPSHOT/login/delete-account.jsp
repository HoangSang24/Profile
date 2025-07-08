<%@page import="dao.AccountDAO"%>
<%@ page import="dao.EventDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    AccountDAO accountDAO = new AccountDAO();
    accountDAO.deleteAccount(id);
    response.sendRedirect("GrantAdmin");
%>