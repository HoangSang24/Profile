<%-- 
    Document   : staff-list
    Created on : Jun 11, 2025, 11:52:31 PM
    Author     : VICTUS
--%>

<%@page import="java.util.List"%>
<%@page import="shop.model.Staff"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Staffs</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <a class="btn-admin-add" href="${pageContext.servletContext.contextPath}/manage-staffs?view=create">+ Add New Staff</a>

            <!-- Form search -->
            <form action="${pageContext.servletContext.contextPath}/manage-staffs" method="POST" class="search-filter-wrapper">
                <input type="text" class="search-input" name="keyWord" placeholder="Enter staff name...">
                <input type="hidden" class="search-input" name="action" value="search" placeholder="Enter staff name...">

                <button class="search-btn" type="submit">Search</button>
                <a href="manage-staffs" class="clear-search-btn" 
                   style="background-color: #ef4444;
                   color: #fff;
                   padding: 8px;
                   border-radius: 13px;">
                    <i class="fas fa-times "></i> Clear
                </a>
            </form>
        </div>
    </section>

    <section class="admin-table-wrapper">
        <% String message = (String) session.getAttribute("sMessage");
            if (message != null) {%>
        <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
            <%= message%>
        </div>
        <%

            }
            session.removeAttribute("sMessage");
        %>

        <% String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {%>
        <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
            <%= errorMessage%>
        </div>
        <%

            }
            session.removeAttribute("errorMessage");
        %>

        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Avatar</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Staff> staffs = (List<Staff>) request.getAttribute("s");
                        if (staffs == null || staffs.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="8" class="text-center text-danger">No Staff Found</td>
                    </tr>
                    <%
                    } else {
                        for (Staff s : staffs) {
                    %>
                    <tr>
                        <td><%= s.getId()%></td>

                        <td>
                            <img src="<%= s.getAvatarUrl()%>" 
                                 alt="Avatar" 
                                 style="width: 80px; height: 60px; object-fit: cover; border-radius: 4px;">
                        </td>
                        <td><%= s.getFullName()%></td>
                        <td><%= s.getEmail()%></td>
                        <td>
                            <span style="
                                  display: inline-block;
                                  padding: 4px 10px;
                                  border-radius: 12px;
                                  font-size: 14px;
                                  font-weight: bold;
                                  color: white;
                                  color: <%= "admin".equalsIgnoreCase(s.getRole()) ? "#4CAF50" : "#2196F3"%>;">
                                <%= s.getRole()%>
                            </span>
                        </td>
                        <td>
                            <div class="table-actions-center">
                                <%
                                    if (!"admin".equalsIgnoreCase(s.getRole())) {
                                %>
                                <a class="btn-action btn-details" href="${pageContext.servletContext.contextPath}/manage-staffs?view=details&id=<%= s.getId()%>">Details</a>
                                <a class="btn-action btn-edit" href="${pageContext.servletContext.contextPath}/manage-staffs?view=edit&id=<%= s.getId()%>">Edit</a>
                                <a class="btn-action btn-delete" href="${pageContext.servletContext.contextPath}/manage-staffs?view=delete&id=<%= s.getId()%>">Delete</a>
                                <%
                                    }
                                %>
                            </div>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>

            </table>
        </div>

    </section>

    <!-- Pagination -->
    <%
        int currentPage = 1;
        if (request.getParameter("page") != null) {
            currentPage = Integer.parseInt(request.getParameter("page"));
        }

        Object numAttr = request.getAttribute("numOfPage");
        int totalPage = (numAttr != null) ? Integer.parseInt(numAttr.toString()) : 1;
        String baseUrl = request.getContextPath() + "/manage-staffs?view=list&page=";
    %>

    <nav class="admin-pagination">
        <ul class="pagination">
            <li class="page-item <%= (currentPage == 1) ? "disabled" : ""%>">
                <a class="page-link" href="<%= baseUrl + (currentPage - 1)%>">«</a>
            </li>

            <% for (int i = 1; i <= totalPage; i++) {%>
            <li class="page-item <%= (i == currentPage) ? "active" : ""%>">
                <a class="page-link" href="<%= baseUrl + i%>"><%= i%></a>
            </li>
            <% }%>

            <li class="page-item <%= (currentPage == totalPage) ? "disabled" : ""%>">
                <a class="page-link" href="<%= baseUrl + (currentPage + 1)%>">»</a>
            </li>
        </ul>
    </nav>
</main>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>