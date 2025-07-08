
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Room</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
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
            .box {
                margin-top: 100px;
                background-color: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            .form-label {
                font-weight: bold;
            }
            .btn-primary {
                background-color: #333;
                border: none;
            }
            .btn-primary:hover {
                background-color: #555;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <nav class="navbar navbar-expand-lg navbar-dark">
                <div class="container">
                    <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                        <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                    </a>
                    <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  
                </div>
            </nav>
            <div class="box">
                <h2>Add New Room</h2>
                <form action="<%= request.getContextPath()%>/ManageRoomServlet" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="mb-3">
                        <label for="cinemaID" class="form-label">Cinema ID:</label>
                        <input type="number" id="cinemaID" name="cinemaID" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="roomName" class="form-label">Room Name:</label>
                        <input type="text" id="roomName" name="roomName" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="seatCapacity" class="form-label">Seat Capacity:</label>
                        <input type="number" id="seatCapacity" name="seatCapacity" class="form-control" required>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary">Add Room</button>
                         <a class="btn btn-secondary" href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">Back</a>
                    </div>
                </form>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
