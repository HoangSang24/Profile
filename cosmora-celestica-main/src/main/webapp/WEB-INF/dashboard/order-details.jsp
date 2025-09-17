<%@page import="java.math.BigDecimal"%>
<%@page import="shop.model.Product"%>
<%@page import="shop.model.OrderDetails"%>
<%@page import="shop.dao.OrderDAO"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Customer and Order information</h2>
    </div>

    <%
        Order order = (Order) request.getAttribute("order");
    %>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>Order ID</th>
                        <th>Customer Name</th>
                        <th>Email</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Address</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><%= request.getParameter("order_id")%></td>
                        <td><%= order.getCustomerName()%></td>
                        <td><%= order.getCustomerEmail()%></td>
                        <td><%= order.getOrderDate()%></td>
                        <td><span 
                                class="relative group cursor-pointer" 
                                title="Original: <%= order.getTotalAmount()%>$ | Discount: <%= order.getVoucherValue()%>$">
                                <%= order.getTotalAmount().subtract(order.getVoucherValue())%>$
                            </span></td>
                        <td><span class="badge-status"><%= order.getStatus()%></span></td>
                        <td><span ><%= order.getShippingAddress()%></span></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </section>


    <div class="table-header">
        <h2 class="table-title">Product in order</h2>
    </div>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>Product Image</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>

                    </tr>
                </thead>
                <tbody>
                    <%
                        ArrayList<OrderDetails> orderDetails = (ArrayList) request.getAttribute("orderdetails");
                        for (OrderDetails orderdetails : orderDetails) {

                    %>
                    <tr>
                        <td><div class="cart__img">
                                <img src="<%= request.getContextPath()%>/assets/img/<%= orderdetails.getImageURL()%>" alt="">
                            </div></td>
                        <td><%= orderdetails.getProductName()%></td>
                        <td><%= orderdetails.getQuantity()%></td>
                        <td><%= orderdetails.getPrice()%>$</td>
                        <td><%= orderdetails.getPrice().multiply(BigDecimal.valueOf(orderdetails.getQuantity()))%>$</td>
                    </tr>
                    <%  }%>
                </tbody>
            </table>
        </div>
    </section>
    <a href="javascript:history.back()" class="admin-manage-back">
        <i class="fas fa-arrow-left mr-1"></i> Back
    </a>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>