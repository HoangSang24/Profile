<%-- 
    Document   : movie-edit-list
    Created on : Feb 26, 2025, 11:34:48 PM
    Author     : HoangSang
--%>

<%@ page import="java.util.List" %>
<%@ page import="model.Movie" %>
<%@ page import="dao.MovieDAO" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Movie edit List</title>
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

        .a{
            width: 150px;
        }
        .b{
            width: 75px;
        }
        .c{
            text-align: center;
        }
        th{
            text-align: center;
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
            <h2>Movie Edit List</h2>
            <form action="MainFuncion" method="post"></form>

            <table class="table table-bordered">
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Genre</th>
                    <th>Duration</th>
                    <th>Release Date</th>
                    <th>Description</th>
                    <th>Image</th>
                    <th>Actions</th>
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

                    <td class="c">
                        <div>
                            <a href="Upload?action=add" class="btn btn-primary mb-3 a">Add Movie</a>
                        </div>
                        <div>
                            <a href="<%= request.getContextPath()%>/Upload?action=edit&id=<%=movie.getMovieID()%>" class="btn btn-warning b">Edit</a>
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=delete&id=<%=movie.getMovieID()%>" class="btn btn-danger">Delete</a>
                        </div>
                    </td>
                </tr>
                <% }%>
            </table>
            <a class="btn btn-secondary" href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin">Back</a>
        </div>
    </body>
</html>
