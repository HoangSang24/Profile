
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Delete Room</title>
    </head>
    <body>
        <h1>Delete Room</h1>
        <form action="<%= request.getContextPath() %>/ManageRoomServlet" method="post">
            <input type="hidden" name="action" value="delete">
            <div>
                <label for="id">Room ID:</label>
                <input type="number" id="id" name="id" required>
            </div>
            <div>
                <button type="submit">Delete Room</button>
            </div>
        </form>
    </body>
</html>
