<%@page import="model.Movie"%>
<%@page import="dao.MovieDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int id = Integer.parseInt(request.getParameter("id")); 
    MovieDAO movieDAO = new MovieDAO();
    Movie movie = movieDAO.getMovieById(id); 

    if (movie == null) {
        response.sendRedirect(request.getContextPath() + "/MainFuncion?movie=edit-list");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Movie</title>
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
        <div class="container mt-4 ">
            <h2 class="box">Edit Movie</h2>
            <form method="post" action="<%= request.getContextPath()%>/Upload" enctype="multipart/form-data">
                <input type="hidden" name="id" value="<%= movie.getMovieID()%>">
                <div class="mb-3">
                    <label class="form-label">Title</label>
                    <input type="text" name="title" class="form-control" value="<%= movie.getTitle()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Genre</label>
                    <input type="text" name="genre" class="form-control" value="<%= movie.getGenre()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Duration</label>
                    <input type="number" name="duration" class="form-control" value="<%= movie.getDuration()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Release Date</label>
                    <input type="date" name="releaseDate" class="form-control" value="<%= movie.getReleaseDate()%>" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Description</label>
                    <textarea name="description" class="form-control" required><%= movie.getDescription()%></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Movie Image</label>

             
                    <div class="input-group mb-3">
                        <input type="text" name="imageName" class="form-control" id="fileName_<%= movie.getMovieID()%>" 
                               value="<%= movie.getImagePath()%>" readonly>
                        <button class="btn btn-outline-secondary" type="button" 
                                onclick="document.getElementById('fileInput_<%= movie.getMovieID()%>').click()">
                            Choose File
                        </button>
                    </div>

                    <input type="file" name="imageFile" class="form-control visually-hidden" id="fileInput_<%= movie.getMovieID()%>" 
                           onchange="previewImage(event, <%= movie.getMovieID()%>)">

      
                    <div class="mt-3">
                        <img id="preview_<%= movie.getMovieID()%>" 
                             src="images/images-movie<%= movie.getImagePath()%>" 
                             alt="Movie Image" 
                             class="img-thumbnail" 
                             style="max-width: 200px; height: auto; display: block;">
                    </div>
                </div>

                <script>
                    function previewImage(event, id) {
                        let input = event.target;
                        let reader = new FileReader();
                        let fileNameElement = document.getElementById("fileName_" + id);
                        let imgElement = document.getElementById("preview_" + id);

                        if (input.files.length > 0) {
                            fileNameElement.value = input.files[0].name; // Cập nhật tên file mới

                            reader.onload = function () {
                                imgElement.src = reader.result; // Hiển thị hình ảnh mới
                            };

                            reader.readAsDataURL(input.files[0]);
                        }
                    }
                </script>
                <button type="submit" class="btn btn-primary">Update Movie</button>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=edit-list" class="btn btn-secondary">Back</a>
            </form>
        </div>
    </body>
</html>