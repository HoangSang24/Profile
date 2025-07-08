
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Cinema</title>
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
             <!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark">
                <div class="container">
                    <a class="navbar-brand" href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">
                        <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                    </a>
                    <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  

                </div>
            </nav>
            <div class="box">
                <h2>Add New Cinema</h2>
                <form action="<%= request.getContextPath()%>/ManageCinemaServlet" method="post">
                    <input type="hidden" name="action" value="add">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="location" class="form-label">Location:</label>
                        <input type="text" id="location" name="location" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="totalRoom" class="form-label">Total Room:</label>
                        <input type="number" id="totalRoom" name="totalRoom" class="form-control" required>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-primary">Add Cinema</button>
                        <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
                    </div>
                </form>
            </div>
        </div>
                    
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
