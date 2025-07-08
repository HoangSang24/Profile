
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Cinema</title>
    </head>
    <body>
        <h1>Delete Cinema</h1>
        <form action="<%= request.getContextPath() %>/ManageCinemaServlet" method="post">
            <input type="hidden" name="action" value="delete">
            <div>
                <label for="id">Cinema ID:</label>
                <input type="number" id="id" name="id" required>
            </div>
            <div>
                <a href="<%= request.getContextPath()%>/MainFuncion?movie=main-admin" class="btn btn-secondary">Back</a>
                <button type="submit">Delete Cinema</button>
            </div>
        </form>
    </body>
</html>
