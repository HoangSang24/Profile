
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List of Rooms</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f8f9fa;
            }
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
            .content {
                padding: 20px;
                text-align: center;
                margin-top: 80px;
            }
            .table-container {
                margin-top: 40px;
            }
            .table th, .table td {
                text-align: center;
                vertical-align: middle;
            }
            .btn {
                margin: 5px;
            }
        </style>
    </head>
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

        <div class="container content">
            <h1>List of Rooms</h1>
            <div class="table-container">
                <table class="table table-bordered table-striped">
                    <thead class="table-dark">
                        <tr>
                            <th>Room ID</th>
                            <th>Cinema Name</th>
                            <th>Room Name</th>
                            <th>Seat Capacity</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="room" items="${rooms}">
                            <tr>
                                <td>${room.roomID}</td>
                                <td>${room.CName}</td>
                                <td>${room.roomName} - ${room.location}</td>
                                <td>${room.seatCapacity}</td>
                                <td>
                                    
                                    <a href="${pageContext.request.contextPath}/ManageRoomServlet?action=edit&id=${room.roomID}" class="btn btn-warning btn-sm">Edit</a>
                                    <a href="${pageContext.request.contextPath}/ManageRoomServlet?action=delete&id=${room.roomID}" class="btn btn-danger btn-sm">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
                <a href="${pageContext.request.contextPath}/main/room-add.jsp" class="btn btn-primary">Add New Room</a>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>