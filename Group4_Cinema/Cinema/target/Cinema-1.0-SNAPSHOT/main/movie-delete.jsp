<%@page import="dao.MovieDAO"%>
<%@page import="model.Movie"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    MovieDAO movieDAO = new MovieDAO();
    Movie movie = movieDAO.getMovieById(id);

    if (movie == null) {
        response.sendRedirect(request.getContextPath() + "/MainFuncion?movie=view");
        return;
    }

    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Delete Movie</title>

    </head>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin: 50px;
        }
        .container {
            max-width: 500px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            background-color: #f9f9f9;
        }
        h2 {
            color: #d9534f;
        }
        .btn {
            padding: 10px 20px;
            margin: 10px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            font-size: 16px;
        }
        .btn-danger {
            background-color: #d9534f;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .error-message {
            color: red;
            font-weight: bold;
        }
        .a{
            text-decoration: none;
        }
        
    </style>
    <body>
        
        <div class="container box">

            <h2>Do You Want to Delete Movie? "<%= movie.getTitle()%>"?</h2>

            <% if (errorMessage != null) {%>
            <p class="error-message"><%= errorMessage%></p>
            <% }%>

            <form action="MainFuncion" method="post">
                <input type="hidden" name="movie" value="delete">
                <input type="hidden" name="id" value="<%= movie.getMovieID()%>">

                <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc chắn muốn xóa phim này?')">Xóa Phim</button>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=edit-list" class="btn btn-secondary a">Back</a>
            </form>
        </div>

        <script>
            function goBack() {
                window.history.back();
            }
        </script>
    </body>
</html>
