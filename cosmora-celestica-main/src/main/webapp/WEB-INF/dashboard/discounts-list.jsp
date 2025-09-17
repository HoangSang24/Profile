<%@page import="shop.model.Discount"%>
<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Product"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Manage Discounts</h2>
    </div>
    <style>
        .status-select-wrapper {
            position: relative;
            display: inline-block;
            width: 140px;
        }

        .status-select-wrapper select {
            width: 100%;
            padding: 4px 26px 4px 8px;
            font-size: 14px;
            border-radius: 6px;
            border: 1px solid #444;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            background-color: #343a40; /* n?n t?i */
            color: #fff;                /* ch? tr?ng */
        }

        .status-select-wrapper::after {
            content: '\25BC'; /* M?i t?n xu?ng d?ng unicode */
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            pointer-events: none;
            font-size: 12px;
            color: #ccc;
        }

        .status-select-wrapper select:focus {
            outline: none;
            border-color: #666;
        }
    </style>


    <div class="admin-header-top">

        <button class="btn-admin-add"
                onclick="location.href = '<%= request.getContextPath()%>/manage-discounts?view=create'">+ Add New Discount</button>
        <div class="admin-header-top">
            <form action="<%= request.getContextPath()%>/manage-discounts" method="get" style="margin-left: auto;">
                <input type="hidden" name="view" value="search" />
                <input type="text" name="keyword" class="search-input" placeholder="Enter discount name..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
                <button class="search-btn">Search</button>
                <a href="manage-discounts" class="clear-search-btn" style="background-color: #ef4444; color: #fff; padding: 8px; border-radius: 13px;">
                    <i class="fas fa-times"></i> Clear
                </a>
            </form>
        </div>
    </div>

    <%
        String success = (String) session.getAttribute("message");
        if (success != null) {
    %>
    <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <%= success%>
    </div>
    <%
            session.removeAttribute("message");
        }
    %>

    <section class="admin-table-wrapper">
        <div class="table-responsive shadow-sm rounded overflow-hidden">
            <table class="table table-dark table-bordered table-hover align-middle mb-0">
                <thead class="table-light text-dark">
                    <tr>
                        <th>ID</th>
                        <th>Product Name</th>
                        <th>Price</th>
                        <th>Sale Price</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Description</th>
                        <th>Active</th>

                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ArrayList<Discount> productList = (ArrayList) request.getAttribute("discountlist");
                        if (productList != null && !productList.isEmpty()) {
                            for (Discount product : productList) {
                                java.time.LocalDate today = java.time.LocalDate.now();
                                java.time.LocalDate startDate = product.getStarDate();
                                java.time.LocalDate endDate = product.getEndaDate();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                int currentStatus = product.getActive();
                                boolean isInvalidTime = startDate.isAfter(today) || endDate.isBefore(today);
                    %>
                    <tr>
                        <td><%= product.getDiscount_id()%></td>
                        <td><%= product.getProduct().getName()%></td>
                        <td><%= product.getProduct().getPrice()%></td>
                        <td><%= product.getSale_price()%></td>
                        <td><%= product.getStarDate().format(formatter)%></td>
                        <td><%= product.getEndaDate().format(formatter)%></td>
                        <td><%= product.getDescription()%></td>
                        <td>
                            <form action="<%= request.getContextPath()%>/manage-discounts" method="post">
                                <input type="hidden" name="voucherId" value="<%= product.getDiscount_id()%>">
                                <input type="hidden" name="productId" value="<%= product.getProduct().getProductId()%>">
                                <input type="hidden" name="action" value="edit-active">
                                <input type="hidden" name="start_date" value="<%= product.getStarDate()%>">
                                <input type="hidden" name="end_date" value="<%= product.getEndaDate()%>">

                                <div class="status-select-wrapper">
                                    <select name="active"
                                            class="form-select auto-submit"
                                            id="active-<%= product.getDiscount_id()%>"
                                            data-product-id="<%= product.getProduct().getProductId()%>"
                                            data-discount-id="<%= product.getDiscount_id()%>">
                                        <option value="1" <%= currentStatus == 1 ? "selected" : ""%>>Active</option>
                                        <option value="0" <%= currentStatus == 0 ? "selected" : ""%>>Inactive</option>
                                    </select>
                                </div>
                            </form>

                            <%-- N?u ngày sai và ?ang Active, t? ??ng chuy?n v? Inactive --%>
                            <% if (isInvalidTime && currentStatus == 1) {%>
                            <script>
                                document.addEventListener('DOMContentLoaded', function () {
                                    const select = document.getElementById('active-<%= product.getDiscount_id()%>');
                                    if (select) {
                                        select.value = "0";
                                        setTimeout(() => select.form.submit(), 200);
                                    }
                                });
                            </script>
                            <% }%>
                        </td>
                        <td>
                            <div class="table-actions-center">
                                <button class="btn-action btn-edit"
                                        onclick="location.href = '<%= request.getContextPath()%>/manage-discounts?view=edit&id=<%= product.getDiscount_id()%>'">
                                    Edit
                                </button>
                                <form action="manage-discounts" method="POST" onsubmit="return confirmDelete(event)">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="discount_id" value="<%= product.getDiscount_id()%>" />
                                    <button type="submit" class="btn-action btn-delete">
                                        Delete
                                    </button>
                                </form>

                                <script>
                                    function confirmDelete(event) {
                                        const confirmed = confirm("Are you sure you want to delete this discount?");
                                        if (!confirmed) {
                                            event.preventDefault(); // stop form submission if cancelled
                                            return false;
                                        }
                                        return true; // allow form submission
                                    }
                                </script>

                            </div>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="8" style="text-align:center; color: orange;">No vouchers found.</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </section>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const selects = document.querySelectorAll('.auto-submit');
        selects.forEach(function (select) {
            select.addEventListener('change', function () {
                this.form.submit();
            });
        });
    });
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
