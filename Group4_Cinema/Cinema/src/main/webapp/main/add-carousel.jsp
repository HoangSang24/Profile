<%-- 
    Document   : add-carousel
    Created on : Mar 25, 2025, 1:18:04 AM
    Author     : HoangSang
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Carousel Image</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            max-width: 500px;
            background: white;
            padding: 20px;
            margin-top: 50px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        .btn-primary {
            width: 100%;
        }
        .alert {
            margin-top: 15px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center">Add Image to Carousel</h2>
        <form action="UploadCarousel" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="add">
            <div class="mb-3">
                <label for="carouselImage" class="form-label">Choose Image</label>
                <input type="file" class="form-control" name="carouselImage" required>
            </div>
            <button type="submit" class="btn btn-primary">Upload</button>
        </form>

        <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-info mt-3"><%= request.getAttribute("message") %></div>
        <% } %>

        <div class="text-center mt-3">
            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
            <a href="UploadCarousel?action=delete" class="btn btn-outline-secondary">Manage Images</a>
        </div>
    </div>

    <!-- Bootstrap JS (Optional) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
