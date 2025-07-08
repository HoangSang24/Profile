<%@ page import="dao.EventDAO" %>
<%@ page import="model.Event" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));
    EventDAO eventDAO = new EventDAO();
    Event event = eventDAO.getEventById(id);

    if (event == null) {
        response.sendRedirect(request.getContextPath() + "/MainFuncion?event=edit-list");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Event</title>
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
    .box {
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

    <div class="container mt-4">
        <h2 class="box">Edit Event</h2>
        <form action="<%= request.getContextPath()%>/Event?action=event-edit&id=<%= event.getEventID()%>" 
              method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<%= event.getEventID()%>"> 

            <div class="mb-3">
                <label class="form-label">Title</label>
                <input type="text" name="title" class="form-control" value="<%= event.getName()%>" required>
            </div>
            
            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control" required><%= event.getDescription() %></textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Event Date</label>
                <input type="date" name="eventDate" class="form-control" value="<%= event.getEventDate() %>" required>
            </div>

            <div class="mb-3">
                <label class="form-label fw-bold">Event Image</label>

                <!-- Ô nhập tên file -->
                <div class="input-group mb-3">
                    <input type="text" name="imageName" class="form-control" id="fileName_<%= event.getEventID()%>" 
                           value="<%= event.getImagePath()%>" readonly>
                    <button class="btn btn-outline-secondary" type="button" 
                            onclick="document.getElementById('fileInput_<%= event.getEventID()%>').click()">
                        Choose File
                    </button>
                </div>

             
                <input type="file" name="imageFile" class="form-control visually-hidden" id="fileInput_<%= event.getEventID()%>" 
                       onchange="previewImage(event, <%= event.getEventID()%>)">

            
                <div class="mt-3">
                    <img id="preview_<%= event.getEventID()%>" 
                         src="<%= request.getContextPath()%>/images/images-event/<%= event.getImagePath()%>" 
                         alt="Event Image" 
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

            <button type="submit" class="btn btn-primary">Update Event</button>
            <a href="<%= request.getContextPath()%>/Event?action=edit-list" class="btn btn-secondary">Back</a>
        </form>
    </div>
</body>
</html>
