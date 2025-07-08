<%@page import="java.io.File"%>
<%@page import="java.io.IOException"%>
<%@page import="jakarta.servlet.ServletException"%>
<%@page import="jakarta.servlet.http.Part"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.MovieDAO"%>
<%@page import="java.sql.Date"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Movie</title>
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
        body {
            background-color: #f8f9fa;
        }
        .box {

            min-width: 400px;
            max-width: 650px;
            width: 100%;
            margin-top: 70px;
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            color: #007bff;
        }
        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #004085;
        }
    </style>
    <body>

        <div class="container mt-4">
            <!-- Navbar -->
            <nav class="navbar navbar-expand-lg navbar-dark">
                <div class="container">
                    <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                        <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                    </a>
                    <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  

                </div>
            </nav>
            <div class="container d-flex justify-content-center align-items-center vh-100">
                <div class="card shadow-lg p-4 box">
                    <h2 class="text-center mb-4">Add Movie</h2>
                    <form method="post" action="<%= request.getContextPath()%>/Upload" enctype="multipart/form-data">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Title</label>
                            <input type="text" name="title" class="form-control" placeholder="Enter movie title" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Genre</label>
                            <input type="text" name="genre" class="form-control" placeholder="Enter genre" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Duration (minutes)</label>
                            <input type="number" name="duration" class="form-control" placeholder="Enter duration" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Release Date</label>
                            <input type="date" name="releaseDate" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Description</label>
                            <textarea name="description" class="form-control" rows="3" placeholder="Enter movie description" required></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Movie Image</label>
                            <input type="file" name="imageFile" class="form-control" required>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">Add Movie</button>
                        <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary mt-2 w-100">Back</a>
                    </form>
                </div>
            </div>


        </div>
    </body>
</html>