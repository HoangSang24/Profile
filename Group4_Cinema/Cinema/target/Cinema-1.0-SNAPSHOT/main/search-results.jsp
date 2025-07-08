<%@page import="java.util.List"%>
<%@page import="model.Movie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Search Results</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
        <style>
            body {
                font-family: 'Poppins', sans-serif;
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
            .section-title {
                font-size: 32px;
                font-weight: 600;
                color: #222;
                text-align: center;
                position: relative;
                text-transform: uppercase;
                margin-bottom: 30px;
            }

            .section-title::after {
                content: '';
                width: 100px;
                height: 4px;
                background: #f39c12;
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
                border-radius: 2px;
            }

            .movie-box {
                position: relative;
                overflow: hidden;
                border-radius: 10px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
                transition: transform 0.3s ease-in-out, box-shadow 0.3s;
            }

            .movie-box:hover {
                transform: scale(1.05);
                box-shadow: 0 8px 15px rgba(0, 0, 0, 0.25);
            }

            .img-hover {
                position: relative;
                overflow: hidden;
                border-radius: 10px;
            }

            .img-hover img {
                width: 100%;
                height: auto;
                transition: transform 0.4s ease;
            }

            .img-hover:hover img {
                transform: scale(1.1);
            }

            .img-hover .overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.7);
                color: white;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                opacity: 0;
                transition: opacity 0.3s ease-in-out;
                padding: 20px;
                text-align: center;
            }

            .img-hover:hover .overlay {
                opacity: 1;
            }

            .overlay h3 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            .overlay p {
                font-size: 14px;
                font-style: italic;
            }

            .button-group {
                margin-top: 10px;
            }

            .button-group .btn {
                width: 100%;
                margin-top: 5px;
                font-size: 14px;
                font-weight: bold;
                transition: background 0.3s, color 0.3s;
            }

            .button-group .btn:hover {
                background: #f39c12;
                color: white;
            }

            .btn-secondary {
                background: #555;
                border: none;
            }

            .btn-secondary:hover {
                background: #f39c12;
            }
            .a{
                padding-top: 50px;
            }

        </style>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="<%= request.getContextPath()%>/main/main-cinema.jsp">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                </a>
                <form class="d-flex" action="<%= request.getContextPath()%>/SearchMovie" method="GET">
                    <input class="form-control me-2" type="search" placeholder="Search movie..." aria-label="Search" name="movieName">
                    <button class="btn btn-outline-light" type="submit">Search</button>
                </form>
                <div class="auth-buttons">
                    <a href="<%= request.getContextPath()%>/Logout" class="btn btn-outline-light btn-sm">Logout</a>   
                    <%
                        String roleID = (String) session.getAttribute("roleID");
                        if ("admin".equalsIgnoreCase(roleID)) {
                    %>
                    <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-outline-light btn-sm">Manager</a>
                    <%
                        }
                    %>
                </div>
            </div>
        </nav>

        <div class="container mt-5 a">
            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-secondary">⬅ Quay lại</a>
            <h2 class="section-title">SEARCH RESULTS</h2>
            <div class="row">
                <%
                    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
                    if (movies != null && !movies.isEmpty()) {
                        for (Movie movie : movies) {
                            String imagePath = request.getContextPath() + "/images/images-movie/" + movie.getImagePath();
                            String title = movie.getTitle();
                            int id = movie.getMovieID();
                            String description = movie.getDescription();
                %>
                <div class="col-6 col-md-3 mb-4">
                    <div class="movie-box">
                        <div class="img-hover">
                            <a href="<%= request.getContextPath()%>/main/MovieDetails.jsp?movieName=<%= title%>">
                                <img src="<%= imagePath%>" alt="<%= title%>" class="img-fluid">
                            </a>
                            <div class="overlay">
                                <h3><%= title%></h3>
                                <p><%= description%></p>
                                <div class="button-group">
                                    <a href="<%= request.getContextPath()%>/main/MovieDetails.jsp?movieName=<%= title%>" class="btn btn-light btn-sm">View Details</a>
                                    <a href="<%= request.getContextPath()%>/Booking?action=showtime&movieID=<%= id%>" class="btn btn-warning btn-sm">Buy Tickets</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <div class="col-12">
                    <p class="text-center">No movies found.</p>
                </div>
                <%
                    }
                %>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>