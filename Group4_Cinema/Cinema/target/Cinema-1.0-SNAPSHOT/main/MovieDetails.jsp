<%@page import="model.Movie"%>
<%@page import="dao.MovieDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%
    String movieIdParam = request.getParameter("movieName");
    MovieDAO movieDAO = new MovieDAO();
    Movie movie = movieDAO.getMovieByName(movieIdParam);
    if (movie != null) {
%>
<!DOCTYPE html>
<html>
    <head>
        <title><%= movie.getTitle()%> - Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
            }
            .movie-container {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
                margin-top: 30px;
            }
            .movie-title {
                font-size: 28px;
                font-weight: bold;
                color: #333;
            }
            .movie-img {
                width: 100%;
                max-width: 300px;
                border-radius: 8px;
                margin-bottom: 15px;
            }
            .movie-info {
                font-size: 16px;
                margin-bottom: 8px;
            }
            .btn-back {
                background-color: #dc3545;
                border: none;
                padding: 10px 15px;
                border-radius: 5px;
                color: #fff;
                font-size: 16px;
                text-decoration: none;
            }
            .btn-back:hover {
                background-color: #c82333;
            }


            .btn-warning {
                border: none;
                padding: 10px 15px;
                border-radius: 5px;
                color: #fff;
                font-size: 16px;
                text-decoration: none;
            }

            .btn-warning:hover {
                background-color: #28a745;
            }

        </style>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8 movie-container">
                    <div class="row">
                        <div class="col-md-4">
                            <img src="<%= request.getContextPath()%>/images/images-movie/<%= movie.getImagePath()%>" alt="<%= movie.getTitle()%>" class="img-fluid movie-img">

                        </div>
                        <div class="col-md-8">
                            <h1 class="movie-title"><%= movie.getTitle()%></h1>
                            <p class="movie-info"><strong>Genre:</strong> <%= movie.getGenre()%></p>
                            <p class="movie-info"><strong>Duration:</strong> <%= movie.getDuration()%> minutes</p>
                            <p class="movie-info"><strong>Release Date:</strong> <%= movie.getReleaseDate()%></p>
                            <p class="movie-info"><strong>Description:</strong> <%= movie.getDescription()%></p>
                            <%

                                String roleID = (String) session.getAttribute("roleID");
                                if ("admin".equalsIgnoreCase(roleID)) {
                            %>
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-back">Back to Movies</a>
                            <%
                                }
                                else if ("customer".equalsIgnoreCase(roleID)) {
                            %>
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-back">Back to Movies</a>
                            <%} else {
                            %>
                            <a href="<%= request.getContextPath()%>/G4" class="btn btn-back">Back to Movies</a>
                            <%
                                }
                            %>
                            <a href="<%= request.getContextPath()%>/Booking?action=showtime&movieID=<%= movie.getMovieID()%>" class="btn btn-warning ">Buy Tickets</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<%
} else {
%>
<div class="container text-center mt-5">
    <div class="alert alert-danger">
        <h4>Movie not found!</h4>
    </div>
    <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-back">Back to Movies</a>
</div>
<%
    }
%>
