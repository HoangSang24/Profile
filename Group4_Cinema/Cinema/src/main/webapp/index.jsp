<%@page import="dao.EventDAO"%>
<%@page import="model.Event"%>
<%@page import="model.Event"%>
<%@page import="dao.MovieDAO"%>
<%@page import="model.Movie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
    String contextPath = request.getContextPath();
    String movieImagePath = application.getRealPath("images/images-movie");
    String eventImagePath = application.getRealPath("images/images-event");
    String carouselImagePath = application.getRealPath("/images/images-carousel");
    
    File movieImageDir = new File(movieImagePath);
    File carouselImageDir = new File(carouselImagePath);
    File eventImageDir = new File(eventImagePath);
    
    List<String> movieImages = new ArrayList<>();
    List<String> eventImages = new ArrayList<>();
    List<String> carouselImages = new ArrayList<>();
    
    
    
    
     
    if (carouselImageDir.exists() && carouselImageDir.isDirectory()) {
        File[] carouselFiles = carouselImageDir.listFiles();
        if (carouselFiles != null) {
            for (File file : carouselFiles) {
                if (file.isFile() && file.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    carouselImages.add(contextPath + "/images/images-carousel/" + file.getName());
                }
            }
        }
    }
    
    
    if (movieImageDir.exists() && movieImageDir.isDirectory()) {
        File[] movieFiles = movieImageDir.listFiles();
        if (movieFiles != null) {
            for (File file : movieFiles) {
                if (file.isFile() && file.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    movieImages.add("images/images-movie/" + file.getName());
                }
            }
        }
    }

    if (eventImageDir.exists() && eventImageDir.isDirectory()) {
        File[] eventFiles = eventImageDir.listFiles();
        if (eventFiles != null) {
            for (File file : eventFiles) {
                if (file.isFile() && file.getName().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    eventImages.add("images/images-event/" + file.getName());
                }
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>G4 Cinema | Home</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">

        <style>
            body {
                font-family: 'Poppins', sans-serif;
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

            .auth-buttons .btn {
                margin-left: 10px;
                font-weight: 500;
                transition: 0.3s;
            }
            .auth-buttons .btn:hover {
                transform: translateY(-2px);
            }

            .carousel {
                max-width: 1300px;
                margin: 40px auto;
                border-radius: 15px;
                overflow: hidden;
                box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            }
            .carousel img {
                width: 100%;
                height: 500px;
                object-fit: cover;
            }
            .carousel-control-prev-icon,
            .carousel-control-next-icon {
                background-color: rgba(0, 0, 0, 0.5);
                border-radius: 50%;
                padding: 15px;
            }

            .section-title {
                font-size: 32px;
                font-weight: 600;
                color: #222;
                margin-bottom: 30px;
                text-align: center;
                position: relative;
            }
            .section-title::after {
                content: '';
                width: 100px;
                height: 3px;
                background: #f39c12;
                position: absolute;
                bottom: -10px;
                left: 50%;
                transform: translateX(-50%);
            }
            .img-hover {
                position: relative;
                overflow: hidden;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                transition: 0.3s;
            }
            .img-hover img {
                width: 100%;
                height: auto;
                transition: 0.3s;
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
                transition: 0.3s;
            }
            .img-hover:hover .overlay {
                opacity: 1;
            }
            .overlay h3 {
                font-size: 24px;
                font-weight: 600;
                margin-bottom: 10px;
            }
            .overlay p {
                font-size: 16px;
                text-align: center;
            }
            .button-group {
                display: flex;
                flex-direction: column;
                margin-top: 10px;
            }
            .button-group .btn {
                margin: 5px 0;
                transition: background-color 0.3s;
            }
            .button-group .btn:hover {
                background-color: #f39c12; 
            }

            .event-container .img-hover {
                height: 300px; 
            }

            .event-container .img-hover img {
                height: 100%;
                object-fit: cover;
            }

            .event-container .col-md-4 {
                flex: 0 0 25%; 
                max-width: 25%;
            }
            .c {
                margin-top: 100px;
            }

        </style>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="G4">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                </a>             
            </div>

            <div class="auth-buttons">
                <a href="Login" class="btn btn-outline-light btn-sm">Login</a>    
                <a href="Function?action=create" class="btn btn-warning btn-sm">Register</a>
            </div>
        </nav>

        <div class="c">

            <div id="movieCarousel" class="carousel slide" data-bs-ride="carousel">
                <div class="carousel-inner">
                    <%
                        for (int i = 0; i < carouselImages.size(); i++) {
                            String img = carouselImages.get(i);
                    %>
                    <div class="carousel-item <%= (i == 0) ? "active" : ""%>">
                        <a href="Login">
                            <img src="<%= img%>" alt="Movie Image">
                        </a>
                    </div>
                    <% } %>
                </div>

                <!-- Navigation buttons -->
                <button class="carousel-control-prev" type="button" data-bs-target="#movieCarousel" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>

                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#movieCarousel" data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span 
                </button>
            </div>

            <div class="container mt-5">
                <h2 class="section-title">MOVIE SELECTION</h2>
                <div class="row" id="movieSelection">
                    <%
                        MovieDAO movieDAO = new MovieDAO();
                        List<Movie> movies = movieDAO.getAllMovies();
                        for (Movie movie : movies) {
                            String imagePath = request.getContextPath() + "/images/images-movie/" + movie.getImagePath();
                            String title = movie.getTitle();
                    %>
                    <div class="col-md-3 mb-4">
                        <div class="img-hover">
                            <img src="<%= imagePath%>" class="img-fluid" alt="Movie">
                            <div class="overlay">
                                <h3><%= title%></h3> 
                                <p>Description or details about the movie.</p>
                                <div class="button-group">
                                    <a href="main/MovieDetails.jsp?movieName=<%= title%>" class="btn btn-light btn-sm">View Details</a>  
                                    <a href="Login" class="btn btn-warning btn-sm">Buy Tickets</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
                <div class="d-flex justify-content-between mt-3">
                    <button id="prevMovie" class="btn btn-warning">Previous</button>
                    <button id="nextMovie" class="btn btn-warning">Next</button>
                </div>
            </div>

            <div class="container mt-5 event-container">
                <h2 class="section-title">EVENT</h2>
                <div class="row " id="eventSelection">
                    <%
                        EventDAO eventDAO = new EventDAO();
                        List<Event> events = eventDAO.getAllEvents();
                        for (Event event : events) {
                            String imagePath = request.getContextPath() + "/images/images-event/" + event.getImagePath();
                            int eventID = event.getEventID();
                    %>
                    <div class="col-md-4 mb-4">
                        <div class="img-hover">
                            <a href="<%= request.getContextPath()%>/Event?action=event-details&id=<%= eventID%>">
                                <img src="<%= imagePath%>" class="img-fluid" alt="Event">
                            </a>
                        </div>
                    </div>
                    <% }%>
                </div>
                <div class="d-flex justify-content-between mt-3">
                    <button id="prevEvent" class="btn btn-warning">Previous</button>
                    <button id="nextEvent" class="btn btn-warning">Next</button>
                </div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            let currentMovieIndex = 0;
            const movieItems = document.querySelectorAll('#movieSelection .col-md-3');
            const totalMovies = movieItems.length;

            document.getElementById('nextMovie').addEventListener('click', function () {
                if (currentMovieIndex < totalMovies - 4) {
                    currentMovieIndex++;
                    updateMovieDisplay();
                }
            });

            document.getElementById('prevMovie').addEventListener('click', function () {
                if (currentMovieIndex > 0) {
                    currentMovieIndex--;
                    updateMovieDisplay();
                }
            });

            function updateMovieDisplay() {
                movieItems.forEach((item, index) => {
                    item.style.display = (index >= currentMovieIndex && index < currentMovieIndex + 4) ? 'block' : 'none';
                });
            }

            updateMovieDisplay();
        </script>
    </body>
</html>