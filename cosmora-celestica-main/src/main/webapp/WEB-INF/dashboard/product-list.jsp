<%--
    Document   : product-list
    Created on : Jun 10, 2025, 10:55:00 PM
    Author     : HoangSang
--%>

<%@page import="java.util.List"%>
<%@page import="shop.model.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<style>
    .admin-filter-select:hover {
        transform: scale(1.1); /* Slightly increase size on hover */
    }

    .admin-filter-select:focus {
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }
</style>
<%
    String contextPath = request.getContextPath();
    int currentPage = (Integer) request.getAttribute("currentPage");
    int totalPages = (Integer) request.getAttribute("totalPages");
    int rowNumber = (Integer) request.getAttribute("startRowNumber");
    List<Product> productList = (List<Product>) request.getAttribute("productList");

    String pageUrl = (String) request.getAttribute("pageUrl");
    String previousPageUrl = (String) request.getAttribute("previousPageUrl");
    String nextPageUrl = (String) request.getAttribute("nextPageUrl");

    // <<< GET THE MESSAGES FROM THE SERVLET >>>
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
%>

<style>
    .table-product-img {
        width: 80px;
        height: 60px;
        object-fit: cover;
        border-radius: 4px;
    }
    .clear-search-btn{
        background-color: #ef4444;
        color: #fff;
        padding: 8px;
        border-radius: 13px;
    }
</style>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Products</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                <a class="btn-admin-add" href="manage-products?action=add">+ Add New Product</a>
            </c:if>
            <div class="search-filter-wrapper" style="margin-left: auto">
                <form action="manage-products" method="get" class="search-form">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="query" class="search-input" placeholder="Enter product name..." value="${param.query != null ? param.query : ''}">
                    <button type="submit" class="search-btn">Search</button>
                </form>
                <a href="manage-products?action=list" class="clear-search-btn">
                    <i class="fas fa-times "></i> Clear
                </a>
            </div>
        </div>      
    </section>

    <% if (successMessage != null && !successMessage.isEmpty()) {%>
    <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <%= successMessage%>
    </div>
    <% } %>
    <% if (errorMessage != null && !errorMessage.isEmpty()) {%>
    <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <%= errorMessage%>
    </div>
    <% } %>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Product Image</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Sale Price</th>
                        <th>Category</th>
                        <th style="text-align: center;">Status</th>
                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (productList != null && !productList.isEmpty()) {
                            for (Product p : productList) {
                            
                    %>
                    <tr>
                        <td><%= p.getProductId()%></td> 
                        <td>
                            <% if (p.getImageUrls() != null && !p.getImageUrls().isEmpty()) {%>
                            <img src="<%= contextPath%>/assets/img/<%= p.getImageUrls().get(0)%>" alt="<%= p.getName()%>" class="table-product-img">
                            <% } else { %>
                            <span>No Image</span>
                            <% }%>
                        </td>
                        <td><%= p.getName()%></td>
                        <td>$<%= p.getPrice()%></td>
                        <td>
                            <% if (p.getSalePrice() != null && p.getActive() == 1) {%>
                            $<%= p.getSalePrice()%>
                            <% } else { %>
                            N/A
                            <% }%>
                        </td>
                        <td><%= p.getCategoryName() != null ? p.getCategoryName() : "N/A"%></td>
                        <td style="text-align: center;">
                            <form action="manage-products" method="POST" style="margin: 0;">
                                <input type="hidden" name="action" value="updateVisibility">
                                <input type="hidden" name="id" value="<%= p.getProductId()%>">
                                <input type="hidden" name="page" value="<%= currentPage%>">


                                <c:choose>
                                    <c:when test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                                        <select name="newStatus" class="admin-filter-select" 
                                                style="border: 1px solid <%= (p.getActiveProduct() == 0) ? "#F44336" : "#4CAF50"%>; border-radius: 4px; padding: 2px; cursor: pointer; transition: transform 0.3s ease, background-color 0.3s ease;"
                                                onchange="if (confirm('Are you sure you want to change this status?')) {
                                                            this.form.submit();
                                                        }">
                                            <option value="1" <%= (p.getActiveProduct() == 1) ? "selected" : ""%>>
                                                Enabled
                                            </option>
                                            <option value="0" <%= (p.getActiveProduct() == 0) ? "selected" : ""%>>
                                                Disabled
                                            </option>
                                        </select>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-status <%= (p.getActiveProduct() == 1) ? "badge-active" : "badge-suspend"%>">
                                            ${customer.isDeactivated ? 'Suspended' : 'Active'}
                                        </span>
                                    </c:otherwise>
                                </c:choose>

                            </form>
                        </td>
                        <td>
                            <div class="table-actions-center">
                                <a class="btn-action btn-details" href="manage-products?action=details&id=<%= p.getProductId()%>">Details</a>
                                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                                    <a class="btn-action btn-edit" href="manage-products?action=update&id=<%= p.getProductId()%>">Edit</a>
                                </c:if>
                                <c:if test="${sessionScope.currentEmployee != null && sessionScope.currentEmployee.role == 'admin'}">
                                    <a class="btn-action btn-delete" href="manage-products?action=delete&id=<%= p.getProductId()%>">Delete</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                    <%      }
                    } else {
                    %>
                    <tr>
                        <td colspan="8" class="text-center">No products found.</td>
                    </tr>
                    <%  } %>
                </tbody>
            </table>
        </div>
    </section>

    <% if (totalPages > 1) {%>
    <nav class="admin-pagination">
        <ul class="pagination">
            <li class="page-item <%= (currentPage <= 1) ? "disabled" : ""%>">
                <a class="page-link" href="<%= previousPageUrl%>" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>

            <% for (int i = 1; i <= totalPages; i++) {%>
            <li class="page-item <%= (i == currentPage) ? "active" : ""%>">
                <a class="page-link" href="<%= pageUrl + i%>"><%= i%></a>
            </li>
            <% }%>

            <li class="page-item <%= (currentPage >= totalPages) ? "disabled" : ""%>">
                <a class="page-link" href="<%= nextPageUrl%>" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
    <% }%>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>