<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Voucher"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>

<main class="admin-main">

    <div class="table-header">
        <h2 class="table-title">Manage Vouchers</h2>
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
                onclick="location.href = '<%= request.getContextPath()%>/manage-vouchers?view=create'">+ Add New Voucher</button>
        <div class="search-filter-wrapper">
            <form action="<%= request.getContextPath()%>/manage-vouchers" method="get">
                <input type="hidden" name="view" value="search" />
                <input type="text" name="keyword" class="search-input" placeholder="Enter voucher name..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
                <button class="search-btn">Search</button>
                <a href="manage-vouchers" class="clear-search-btn" 
                   style="background-color: #ef4444;
                   color: #fff;
                   padding: 8px;
                   border-radius: 13px;">
                    <i class="fas fa-times "></i> Clear
                </a>                    
            </form>

        </div> 
    </div>  
    <%
        String success = (String) session.getAttribute("message");
        if (success != null) {
    %>
    <div style="border: 1px solid green; background-color: #e6ffe6; color: green; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
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
                        <th>Code</th>
                        <th>Usage</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th style="text-align: center;">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        ArrayList<Voucher> voucherlist = (ArrayList) request.getAttribute("voucherslist");
                        if (voucherlist != null && !voucherlist.isEmpty()) {
                            for (Voucher voucher : voucherlist) {

                    %>
                    <tr>

                        <td> <%= voucher.getVoucherId()%></td>
                        <td> <%= voucher.getCode()%></td>                          
                        <td><%= voucher.getUsageLimit()%></td>
                        <td><%= voucher.getFormattedStartDate()%></td>
                        <td><%= voucher.getFormattedEndDate()%></td>
                        <td>
                            <form action="<%= request.getContextPath()%>/manage-vouchers" method="post">


                                <%
                                    java.time.LocalDate today = java.time.LocalDate.now();
                                    java.time.LocalDate startDate = voucher.getStartDate();
                                    java.time.LocalDate endDate = voucher.getEndDate();
                                    int currentStatus = voucher.getActive();

                                %>
                                <input type="hidden" name="voucherId" value="<%= voucher.getVoucherId()%>">
                                <input type="hidden" name="action" value="edit-active">
                                <div class="status-select-wrapper">
                                    <select name="active" class="form-select auto-submit">

                                        <% if (endDate.isBefore(today) || voucher.getUsageLimit() <= 0 || startDate.isAfter(today)) { %>
                                        <option value="0" selected>Inactive</option>
                                        <% } else {%>
                                        <option value="1" <%= currentStatus == 1 ? "selected" : ""%>>Active</option>
                                        <option value="0" <%= currentStatus == 0 ? "selected" : ""%>>Inactive</option>
                                        <% }%>
                                    </select>
                                </div>
                            </form>
                        </td>
              
           
                <td> <div class="table-actions-center">                                      
                        <button class="btn-action btn-edit"
                                onclick="location.href = '<%= request.getContextPath()%>/manage-vouchers?view=edit&id=<%= voucher.getVoucherId()%>'">
                            Edit
                        </button>
                        <button class="btn-action btn-details"
                                onclick="location.href = '<%= request.getContextPath()%>/manage-vouchers?view=detail&id=<%= voucher.getVoucherId()%>'">
                            Detail
                        </button>

                    </div> </td> <% }
                        } else {%>
                <td style="color: orange; margin-bottom: 10px;">No vouchers found.</td>
                <%}%>


                </tr>
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