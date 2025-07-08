<%-- 
    Document   : logout
    Created on : Feb 25, 2025, 7:45:14 PM
    Author     : HoangSang
--%>

<%
    session.invalidate();
    response.sendRedirect("Login");
%>
