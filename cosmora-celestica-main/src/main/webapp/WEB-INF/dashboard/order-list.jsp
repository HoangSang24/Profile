<%-- 
    Document   : dashboar-order-list
    Created on : Jun 11, 2025, 1:27:27 PM
    Author     : ADMIN
--%>

<%@page import="java.util.List"%>
<%@page import="shop.dao.OrderDAO"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<c:set var="isSearch" value="${not empty param.customer_name}" />
<style>
    .admin-filter-select:hover {
        transform: scale(1.1); /* Slightly increase size on hover */
    }

    .admin-filter-select:focus {
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }
</style>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Orders</h2>
    </div>

    <section class="admin-header">
        <div class="admin-header-top">
            <form action="<%= request.getContextPath()%>/manage-orders" method="POST" style="display: flex; margin-left: auto;" class="search-filter-wrapper">
                <input type="hidden" name="action" value="search" />
                <input type="text" name="customer_name" class="search-input" placeholder="Enter customer full name...">
                <button type="submit" class="search-btn">Search</button>
                <a href="manage-orders" class="clear-search-btn" 
                   style="background-color: #ef4444;
                   color: #fff;
                   padding: 8px;
                   border-radius: 13px;">
                    <i class="fas fa-times "></i> Clear
                </a>
            </form>
        </div>

    </section>

    <!-- Message Container -->
    <c:if test="${not empty message}">
        <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
            <p id="messageText">
                ${message}
            </p>
        </div>
    </c:if>
    <%
        request.getSession().removeAttribute("message");
    %>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Order> orderlist = (List) request.getAttribute("orderlist");
                        for (Order order : orderlist) {


                    %>
                    <tr>
                        <td><%= order.getOrderId()%></td>
                        <td><%= order.getCustomerName()%></td>
                        <td><%= order.getOrderDate()%></td>
                        <td><%= order.getTotalAmount()%></td>
                        <td><%
                            String status = order.getStatus();
                            int statusLevel = 0;
                            if (status.equals("Pending"))
                                statusLevel = 1;
                            else if (status.equals("Confirmed"))
                                statusLevel = 2;
                            else if (status.equals("Shipping"))
                                statusLevel = 3;
                            else if (status.equals("Shipped"))
                                statusLevel = 4;
                            else if (status.equals("Order Completed"))
                                statusLevel = 5;
                            else if (status.equals("Cancel"))
                                statusLevel = 6;
                            %>

                            <%
                                String borderColor;
                                switch (status) {
                                    case "Pending":
                                        borderColor = "#FFC107"; // amber
                                        break;
                                    case "Confirmed":
                                        borderColor = "#2196F3"; // blue
                                        break;
                                    case "Shipping":
                                        borderColor = "#3F51B5"; // indigo
                                        break;
                                    case "Shipped":
                                        borderColor = "#4CAF50"; // green
                                        break;
                                    case "Order Completed":
                                        borderColor = "#9C27B0"; // purple
                                        break;
                                    case "Cancel":
                                        borderColor = "#F44336"; // red
                                        break;
                                    default:
                                        borderColor = "#9E9E9E"; // grey
                                }
                            %>
                            <form action="manage-orders" method="post">
                                <input type="hidden" name="action" value="update" />
                                <input type="hidden" name="orderId" value="<%= order.getOrderId()%>" />
                                <select name="status" class="admin-filter-select" 
                                        style="border: 2px solid <%= borderColor%>; border-radius: 4px; padding: 2px; cursor: pointer; transition: transform 0.3s ease, background-color 0.3s ease;"
                                        onchange="confirmAndSubmit(this)">
                                    <option value="Pending"
                                            <%= status.equals("Pending") ? "selected" : ""%>
                                            <%= statusLevel > 1 ? "disabled" : ""%>>Pending</option>

                                    <option value="Confirmed"
                                            <%= status.equals("Confirmed") ? "selected" : ""%>
                                            <%= statusLevel > 2 ? "disabled" : ""%>>Confirmed</option>

                                    <option value="Shipping"
                                            <%= status.equals("Shipping") ? "selected" : ""%>
                                            <%= statusLevel > 3 ? "disabled" : ""%>>Shipping</option>

                                    <option value="Shipped"
                                            <%= status.equals("Shipped") ? "selected" : ""%>
                                            <%= statusLevel > 4 ? "disabled" : ""%>>Shipped</option>

                                    <option value="Order Completed"
                                            <%= status.equals("Order Completed") ? "selected" : ""%>
                                            <%= statusLevel > 5 ? "disabled" : ""%>>Order Completed</option>

                                    <option value="Cancel"
                                            <%= status.equals("Cancel") ? "selected" : ""%>
                                            <%= statusLevel > 6 ? "disabled" : ""%>>Cancel</option>
                                </select>
                            </form>
                        </td>
                <script>
                    function confirmAndSubmit(selectElement) {
                        const confirmed = confirm("Are you sure you want to change the order status?");
                        if (confirmed) {
                            selectElement.form.submit();
                        }
                    }
                </script>


                <td>
                    <div class="table-actions-center">
                        <button class="btn-action btn-details"
                                onclick="location.href = '<%= request.getContextPath()%>/manage-orders?view=details&customer_id=<%= order.getCustomerId()%>&order_id=<%= order.getOrderId()%>'">
                            Details
                        </button>
                        <button class="btn-action btn-history" onclick="location.href = '<%= request.getContextPath()%>/manage-customers?view=details&id=<%= order.getCustomerId()%>'">
                            Customer Details</button>
                    </div>
                </td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
    </section>

    <!-- FORM PHÂN TRANG -->
    <form id="paginationForm" method="POST" action="<%= request.getContextPath()%>/manage-orders">
        <input type="hidden" name="action" value="search" />
        <input type="hidden" name="customer_name" value="${param.customer_name}" />
        <input type="hidden" name="page" id="pageInput" value="${currentPage}" />
    </form>

    <nav class="admin-pagination">
        <ul class="pagination">
            <!-- Nút Previous -->
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a href="#" class="page-link" onclick="submitPage(${currentPage - 1})">«</a>
            </li>

            <!-- Các nút số trang -->
            <c:forEach var="i" begin="1" end="${totalPages}">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a href="#" class="page-link" onclick="submitPage(${i})">${i}</a>
                </li>
            </c:forEach>

            <!-- Nút Next -->
            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                <a href="#" class="page-link" onclick="submitPage(${currentPage + 1})">»</a>
            </li>
        </ul>
    </nav>

    <script>
        function submitPage(pageNumber) {
            document.getElementById('pageInput').value = pageNumber;
            document.getElementById('paginationForm').submit();
        }
    </script>



</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>