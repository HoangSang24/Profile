<%@ page import="java.util.List" %>
<%@ page import="model.Movie" %>
<%@ page import="dao.MovieDAO" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Movie List</title>
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
            font-size: 28px;
            font-weight: bold;
        }

        .navbar-brand span.g4 {
            color: red;
        }

        .navbar-brand span.cinema {
            color: #E1A95F; 
        }
        .box{
            margin-top: 100px;
        }
        .table{
            margin-top: 40px;
        }
        
    </style>
    <body>
 
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                </a>
                <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  

            </div>
        </nav>
        <div class="container mt-4 box">
            <h2>Movie List</h2>
            <table class="table table-bordered">
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Genre</th>
                    <th>Duration</th>
                    <th>Release Date</th>
                    <th>Description</th>
                    <th>Image</th>
                </tr>
                <%
                    MovieDAO movieDAO = new MovieDAO();
                    List<Movie> movies = movieDAO.getAllMovies();
                    for (Movie movie : movies) {
                %>
                <tr>
                    <td><%= movie.getMovieID()%></td>
                    <td><%= movie.getTitle()%></td>
                    <td><%= movie.getGenre()%></td>
                    <td><%= movie.getDuration()%> min</td>
                    <td><%= movie.getReleaseDate()%></td>
                    <td><%= movie.getDescription()%></td>

                    <td>
                        <img src="<%= request.getContextPath()%>/images/images-movie/<%= movie.getImagePath()%>" 
                             alt="Movie Image" width="100" height="150">
                    </td>
                </tr>


                <% }%>
            </table>
            <a class="btn btn-secondary" href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">Back</a>
        </div>
    </body>
</html>
