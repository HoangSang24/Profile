<%@page import="java.util.List"%>
<%@page import="model.Account"%>
<%@page import="dao.AccountDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    AccountDAO accountDAO = new AccountDAO();
    List<Account> accountList = accountDAO.getAll();
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <h2>Admin Dashboard</h2>
            <form action="GrantAdmin" method="post">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Change Role</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Account acc : accountList) {%>
                        <tr>
                            <td><%= acc.getId()%></td>
                            <td><%= acc.getUsername()%></td>
                            <td><%= acc.getEmail()%></td>
                            <td><%= acc.getRole()%></td>
                            
                            <td>
                                <select name="roles" class="form-select">
                                    <option value="Admin" <%= "Admin".equals(acc.getRole()) ? "selected" : ""%>>Admin</option>
                                    <option value="Customer" <%= "Customer".equals(acc.getRole()) ? "selected" : ""%>>Customer</option>
                                </select>
                                <input type="hidden" name="userIds" value="<%= acc.getId()%>">
                            </td>
                            <td>
                                <a href="<%= request.getContextPath()%>/Function?action=delete&id=<%= acc.getId() %>" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Are you sure you want to delete this Account?');">
                                    Delete
                                </a>
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
                <button type="submit" class="btn btn-primary">Update Roles</button>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
            </form>

            <% if (request.getAttribute("message") != null) {%>
            <p class="text-success"><%= request.getAttribute("message")%></p>
            <% } %>

            <% if (request.getAttribute("error") != null) {%>
            <p class="text-danger"><%= request.getAttribute("error")%></p>
            <% }%>
        </div>
    </body>
</html>
