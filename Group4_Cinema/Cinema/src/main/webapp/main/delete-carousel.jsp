<%-- 
    Document   : delete-carousel
    Created on : Mar 25, 2025, 1:18:28 AM
    Author     : HoangSang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<html>
<head>
    <title>Delete Carousel Image</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 700px;
            background: white;
            padding: 20px;
            margin-top: 50px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        .image-box {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 10px;
            background: #fff;
        }
        .image-box img {
            width: 100px;
            height: auto;
            border-radius: 5px;
        }
        .btn-danger {
            width: 80px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center">Manage Carousel Images</h2>
        <% 
            String imagePath = application.getRealPath("/images/images-carousel");
            File folder = new File(imagePath);
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) { 
        %>
        <div class="list-group">
            <% for (File file : files) { %>
            <div class="image-box">
                <img src="<%= request.getContextPath() %>/images/images-carousel/<%= file.getName() %>" alt="Carousel Image">
                <form action="UploadCarousel" method="post">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="imageName" value="<%= file.getName() %>">
                    <button type="submit" class="btn btn-danger">Delete</button>
                </form>
            </div>
            <% } %>
        </div>
        <% } else { %>
            <p class="text-center text-muted">No images available.</p>
        <% } %>

        <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-info mt-3"><%= request.getAttribute("message") %></div>
        <% } %>

        <div class="text-center mt-3">
            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
            <a href="UploadCarousel?action=add" class="btn btn-outline-primary">Add New Image</a>
        </div>
    </div>

    <!-- Bootstrap JS (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
