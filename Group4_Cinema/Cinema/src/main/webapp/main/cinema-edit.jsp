

<%@page import="model.Cinema"%>
<%@page import="dao.CinemaDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Cinema cinema = (Cinema) request.getAttribute("cinema");
    if (cinema == null) {
        response.sendRedirect(request.getContextPath() + "/ManageCinemaServlet?action=list");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Cinema</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <style>
        .navbar {
            background-color: #333;
            padding: 10px;
            color: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            position: fixed;
            width: 100%;
            top: 0;
            left: 0;
            z-index: 1000;
        }
        .navbar a {
            color: white;
            text-decoration: none;
            margin-left: 15px;
        }

        .navbar-brand {
            font-size: 28px; /* Tăng kích thước chữ */
            font-weight: bold; /* In đậm */
        }

        .navbar-brand span.g4 {
            color: red; /* Màu đỏ cho G4 */
        }

        .navbar-brand span.cinema {
            color: #E1A95F; /* Màu vàng đất cho CINEMA */
        }
        .box{
            margin-top: 100px;
        }
    </style>
    <body>
         <!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark">
                <div class="container">
                    <a class="navbar-brand" href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">
                        <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                    </a>
                    <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  

                </div>
            </nav>
        <div class="container mt-4 ">
            <h2 class="box">Edit Cinema</h2>
            <form method="post" action="<%= request.getContextPath()%>/ManageCinemaServlet">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= cinema.getCinemaID()%>"> <!-- Thêm ID vào form -->
                <div class="mb-3">
                    <label class="form-label">Name</label>
                    <input type="text" name="name" class="form-control" value="<%= cinema.getName()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Location</label>
                    <input type="text" name="location" class="form-control" value="<%= cinema.getLocation()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Total Room</label>
                    <input type="number" name="totalRoom" class="form-control" value="<%= cinema.getTotalRoom()%>" required>
                </div>
                <button type="submit" class="btn btn-primary">Update Cinema</button>
                <a href="<%= request.getContextPath()%>/ManageCinemaServlet?action=list" class="btn btn-secondary">Back</a>
            </form>
        </div>
    </body>
</html>
