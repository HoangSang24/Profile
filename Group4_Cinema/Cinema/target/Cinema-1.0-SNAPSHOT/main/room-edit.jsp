
--%>
<%@page import="model.Rooms"%>
<%@page import="dao.RoomsDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Rooms room = (Rooms) request.getAttribute("room");
    if (room == null) {
        response.sendRedirect(request.getContextPath() + "/ManageRoomServlet?action=list");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Room</title>
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
                <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                </a>
                <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  
            </div>
        </nav>
        <div class="container mt-4 ">
            <h2 class="box">Edit Room</h2>
            <form method="post" action="<%= request.getContextPath()%>/ManageRoomServlet">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= room.getRoomID()%>"> <!-- Thêm ID vào form -->
                <div class="mb-3">
                    <label class="form-label">Cinema Name</label>
                    <select name="cinemaID" class="form-control">
                        <c:forEach var="cinema" items="${cinemas}">
                            <option value="${cinema.cinemaID}" ${cinema.cinemaID == room.cinemaID ? 'selected' : ''}>
                                ${cinema.name}  <!-- Đúng -->
                            </option>

                        </c:forEach>
                    </select>
                </div>

                <div class="mb-3">
                    <label class="form-label">Room Name</label>
                    <input type="text" name="roomName" class="form-control" value="<%= room.getRoomName()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Seat Capacity</label>
                    <input type="number" name="seatCapacity" class="form-control" value="<%= room.getSeatCapacity()%>" required>
                </div>
                <button type="submit" class="btn btn-primary">Update Room</button>
                <a href="<%= request.getContextPath()%>/ManageRoomServlet?action=list" class="btn btn-secondary">Back</a>
            </form>
        </div>
    </body>
</html>