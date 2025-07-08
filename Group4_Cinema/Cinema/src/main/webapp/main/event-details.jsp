<%@page import="model.Event"%>
<%@page import="dao.EventDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String eventIdParam = request.getParameter("id");
    int eventID = 0;

    if (eventIdParam != null) {
        try {
            eventID = Integer.parseInt(eventIdParam);
        } catch (NumberFormatException e) {
            eventID = -1;
        }
    }

    EventDAO eventDAO = new EventDAO();
    Event event = eventDAO.getEventById(eventID);
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Event Details</title>

        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
                font-family: Arial, sans-serif;
            }
            .event-container {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.15);
                margin-top: 50px;
            }
            .event-title {
                font-size: 30px;
                font-weight: bold;
                color: #333;
            }
            .event-info {
                font-size: 16px;
                margin-bottom: 10px;
            }
            .event-image {
                width: 100%;
                max-height: 400px;
                object-fit: cover;
                border-radius: 10px;
            }
            .btn-back {
                background-color: #ff4757;
                border: none;
                padding: 12px 20px;
                border-radius: 5px;
                color: #fff;
                font-size: 16px;
                text-decoration: none;
                display: inline-block;
                transition: 0.3s;
            }
            .btn-back:hover {
                background-color: #e84118;
                transform: scale(1.05);
            }
            .navbar-brand {
                font-weight: bold;
                font-size: 22px;
            }
            .navbar .btn-logout {
                background-color: #ff4757;
                border: none;
                padding: 8px 12px;
                color: white;
                border-radius: 5px;
                transition: 0.3s;
            }
            .navbar .btn-logout:hover {
                background-color: #e84118;
                transform: scale(1.05);
            }
        </style>
    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
                <a class="navbar-brand" href="MainFuncion?movie=main-admin">
                    <span class="g4">G4</span> <span class="cinema">CINEMA</span>
                </a>
                <a href="Logout" class="btn btn-outline-light btn-sm">Logout</a>  
            </div>
        </nav>

        <!-- Event Details Section -->
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-10">
                    <div class="event-container">
                        <% if (event != null) { %>
                        <div class="row">
                          
                            <div class="col-md-5 text-center">
                                <% if (event.getImagePath() != null && !event.getImagePath().isEmpty()) {%>
                                <img src="images/images-event/<%= event.getImagePath()%>" class="event-image img-fluid" alt="Event Image">
                                <% } else { %>
                                <img src="https://via.placeholder.com/400x400" class="event-image img-fluid" alt="No Image Available">
                                <% }%>
                            </div>


                            <div class="col-md-7">
                                <h1 class="event-title"><%= event.getName()%></h1>
                                <p class="event-info"><strong>Discount:</strong> <%= event.getDiscountPercentage()%>%</p>
                                <p class="event-info"><strong>Event Date:</strong> <%= event.getEventDate()%></p>
                                <p class="event-info"><strong>Description:</strong> <%= event.getDescription()%></p>

                                <%
                                    String roleID = (String) session.getAttribute("roleID");
                                    if ("admin".equalsIgnoreCase(roleID)) {
                                %>
                                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-back">Back to Movies</a>
                                <%
                                } else if ("customer".equalsIgnoreCase(roleID)) {
                                %>
                                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-back">Back to Movies</a>
                                <%} else {
                                %>
                                <a href="<%= request.getContextPath()%>/G4" class="btn btn-back">Back to Movies</a>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <% } else {%>
                        <h2 class="text-danger text-center">Event not found!</h2>
                        <div class="text-center">
                            <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-cinema" class="btn btn-back mt-3">Back to Events</a>
                        </div>
                        <% }%>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
