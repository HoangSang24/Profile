<%-- 
    Document   : login
    Created on : Feb 25, 2025, 6:49:57 PM
    Author     : HoangSang
--%>

<%@page import="java.util.List"%>
<%@page import="model.Account"%>
<%@page import="dao.AccountDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .login-container {
                max-width: 400px;
                margin: auto;
                padding: 20px;
                margin-top: 50px;
                border-radius: 10px;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
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
            .box{
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
        </nav>

        <div class="container box">
            <div class="login-container bg-light p-4">
                <div class="text-center">
                    <h1>Login</h1>
                </div>
                <form method="post" action="<%= request.getContextPath()%>/Login">
                    <div class="mb-3">
                        <label class="form-label">Username</label>
                        <input type="text" class="form-control" name="username" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Password</label>
                        <input type="password" class="form-control" name="password" required>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </div>

                    <%
                        if (session.getAttribute("err") != null) {
                            String err = (String) session.getAttribute("err");
                            out.print("<p>" + err + "</p>");
                        }
                    %>
                </form>
                <div class="text-center mt-3">
                    <a href="<%= request.getContextPath()%>/Function?action=create" class="btn btn-secondary w-100">Create Account</a>
                </div>
                <div class="text-center mt-2">
                    <a href="<%= request.getContextPath()%>/Function?action=forgot"  class="btn btn-link">Forgot Password?</a>
                </div>



            </div>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
