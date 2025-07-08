<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dao.MovieDAO"%>
<%@page import="model.Movie"%>
<%@page import="java.util.List"%>
<%@page import="model.Cinema" %>
<%@page import="dao.CinemaDAO" %>

<%
    MovieDAO movieDAO = new MovieDAO();
    List<Movie> movies = movieDAO.getAllMovies();
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home-Admin</title>
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
                font-size: 28px; 
                font-weight: bold; 
            }

            .navbar-brand span.g4 {
                color: red;
            }

            .navbar-brand span.cinema {
                color: #E1A95F; 
            }

            .content {
                padding: 20px;
                text-align: center;
                margin-top: 80px;
            }
            .sidebar {
                width: 25%;
                background-color: #343a40;
                padding: 20px;
                color: white;
                position: fixed;
                top: 80px;
                left: 0;
                bottom: 0;
            }
            .sidebar h5{
                text-align: center;

            }
            .sidebar .menu-box {
                background: rgba(255, 255, 255, 0.1);
                padding: 15px;
                border-radius: 5px;
                display: flex;
                flex-direction: column;
            }
            .sidebar a {
                display: block;
                color: white;
                text-decoration: none;
                padding: 8px 0;
                text-align: center;
                border-radius: 5px;
                transition: background 0.3s;
            }
            .sidebar a:hover {
                background: rgba(255, 255, 255, 0.2);
            }
            .image-grid img {
                width: 100%;
                height: 300px;
                object-fit: cover;
                border-radius: 10px;
                transition: transform 0.3s;
            }
            .image-grid img:hover {
                transform: scale(1.05);
            }
            .main-content {
                margin-left: 25%;
                width: 75%;
                padding: 20px;
                margin-top: 60px;
            }
            .movie-title {
                text-align: center;
                font-size: 14px;
                font-weight: bold;
                text-transform: uppercase;
                margin-top: 20px;
                padding: 5px 5px 5px 5px;
                background-color: #E1A95F;
            }
            .movie-box {
                background-color: #ddd;
                padding: 10px;
                border-radius: 10px;
                text-align: center;
            }
            .add-movie-btn {
                display: flex;
                justify-content: center;
                align-items: center;
                height: 300px; 
            }
            .add-movie-btn a {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 100%;
                height: 124%;
                margin-top: 70px;
                background-color: rgba(200, 200, 200, 0.5); 
                border-radius: 10px;
                font-size: 80px;
                font-weight: bold;
                color: rgba(100, 100, 100, 0.6);
                text-decoration: none;
            }
            .add-movie-btn a:hover {
                background-color: rgba(180, 180, 180, 0.7); 
                color: rgba(80, 80, 80, 0.8);
            }

            .menu-box {
                display: none;
                padding-left: 15px;
            }

            .toggle-menu {
                cursor: pointer;
                background-color: rgba(255, 255, 255, 0.1);
                padding: 10px;
                border-radius: 5px;
                margin-top: 5px;
            }

            .toggle-menu:hover {
                background-color: #ddd;
            }


        </style>
    </head>
    <body>
 
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                    
                </a>
                <div>
                    <a href="GrantAdmin" class="btn btn-outline-light btn-sm">Update Roles</a>  
                    <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-outline-light btn-sm">Cinema</a>  
                    <a href="<%= request.getContextPath()%>/Logout" class="btn btn-outline-light btn-sm">Logout</a>    

                </div>
            </div>
        </nav>

        <div class="main">
            <div class="sidebar">
                <h5 class="toggle-menu">Manage Event</h4>
                    <div class="menu-box">
                        <a href="<%= request.getContextPath()%>/UploadCarousel?action=add">Add Movie Carousel</a>     
                        <a href="<%= request.getContextPath()%>/Event?action=event-add">Add Event</a>     
                        <a href="<%= request.getContextPath()%>/Event?action=event-list">View Event</a>
                    </div>

                    <h5 class="toggle-menu">Manage Movies</h4>
                        <div class="menu-box">
                            <a href="<%= request.getContextPath()%>/Upload?action=add">Add Movie</a>
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=edit-list">Edit Movie</a>
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=view">View Movies</a>
                        </div>

                        <h5 class="toggle-menu">Manage Cinema</h4>
                            <div class="menu-box">
                                <a href="<%= request.getContextPath()%>/main/cinema-add.jsp">Add Cinema</a>
                                <a href="<%= request.getContextPath()%>/ManageCinemaServlet?action=list">Edit Cinema</a>
                            </div>

                            <h5 class="toggle-menu">Manage Room</h4>
                                <div class="menu-box">
                                    <a href="<%= request.getContextPath()%>/main/room-add.jsp">Add Room</a>
                                    <a href="<%= request.getContextPath()%>/ManageRoomServlet?action=list">Edit Room</a>
                                </div>

                                <h5 class="toggle-menu">Show Schedule</h4>
                                    <div class="menu-box">
                                        <a href="<%= request.getContextPath()%>/AddShowtime?action=add">Add Showtime</a>
                                        <a href="<%= request.getContextPath()%>/AddShowtime">View Showtime</a>
                                    </div>

                                    <h5 class="toggle-menu">Ticket History</h4>
                                        <div class="menu-box">
                                            <a href="<%= request.getContextPath()%>/main/booking-history.jsp">List Ticket</a>   
                                        </div>
                                        </div>

                                        <div class="main-content">
                                            <div class="container image-grid">
                                                <div class="row">
                                                    <% for (Movie movie : movies) {%>
                                                    <div class="col-6 col-md-4 col-lg-3 mb-4"> 
                                                        <div class="movie-box">
                                                            <a href="<%= request.getContextPath()%>/main/MovieDetails.jsp?movieName=<%= movie.getTitle()%>">
                                                                <img src="images/images-movie/<%= movie.getImagePath()%>" alt="<%= movie.getTitle()%>" class="img-fluid">
                                                            </a>
                                                            <div class="movie-title"><%= movie.getTitle().toUpperCase()%></div> <!-- In hoa tên phim -->
                                                        </div>
                                                    </div>
                                                    <% }%>

                                                    <div class="col-6 col-md-4 col-lg-3 add-movie-btn">
                                                        <a href="<%= request.getContextPath()%>/Upload?action=add">+</a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        </div>
                                        </body>
                                        <script>
                                            document.addEventListener("DOMContentLoaded", function () {
                                                var menus = document.querySelectorAll(".menu-box");
                                                menus.forEach(function (menu) {
                                                    menu.style.display = "none"; // Ẩn tất cả menu ban đầu
                                                });

                                                var headers = document.querySelectorAll(".toggle-menu");
                                                headers.forEach(function (header) {
                                                    header.addEventListener("click", function () {
                                                        var menu = this.nextElementSibling;
                                                        menu.style.display = (menu.style.display === "none" || menu.style.display === "") ? "block" : "none";
                                                    });
                                                });
                                            });

                                        </script>

                                        </html>
