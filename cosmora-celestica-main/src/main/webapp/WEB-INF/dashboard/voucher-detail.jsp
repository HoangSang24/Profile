<%@page import="shop.model.Voucher"%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>


<%
    Voucher voucher = (Voucher) request.getAttribute("voucher");

%>

<main class="admin-main">
    <div class="table-header">

        <h2 class="table-title">Detail Voucher</h2>
        <%            if (voucher == null) {
        %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px;">
            <strong>Error:</strong> Voucher not found.
        </div>
        <a href="<%= request.getContextPath()%>/manage-vouchers" class="admin-manage-back">
            <i class="fas fa-arrow-left mr-2" style="font-size: 1.1rem; color: #333;"></i> Back
        </a>
        <%
        } else {
        %>
    </div>
    <%
        String error = (String) request.getAttribute("message");
        if (error != null) {
    %>
    <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <strong>Error:</strong> <%= error%>
    </div>

    <%
        }

    %>

    <div class="admin-manage-type voucher-details">
        <fieldset class="mb-4 admin-manage-fieldset">                  
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Code</label>
                    <input type="text" class="form-control admin-manage-input"
                           name="code" value="<%= voucher.getCode()%>"  readonly>
                </div>
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Value</label>
                    <input type="number" class="form-control admin-manage-input"
                           name="value" value="<%= voucher.getValue()%>" readonly>
                </div>
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Usage</label>
                    <input type="number" class="form-control admin-manage-input"
                           name="usage_limit" value="<%= voucher.getUsageLimit()%>"  readonly>
                </div>
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Minimum Order Value</label>
                    <input type="number" class="form-control admin-manage-input"
                           name="min_order_value" value="<%= voucher.getMinOrderValue()%>"  min="0" step="0.01"  readonly>
                </div>
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Start Date</label>
                    <input type="date" class="form-control admin-manage-input"
                           name="start_date" id="start_date" value="<%= voucher.getStartDate()%>"  readonly>
                </div>

                <div class="col-md-6">
                    <label class="form-label admin-manage-label">End Date</label>
                    <input type="date" class="form-control admin-manage-input"
                           name="end_date" id="end_date" value="<%= voucher.getEndDate()%>"  readonly>
                </div>
                <div class="col-md-6">
                    <label class="form-label admin-manage-label">Status</label>
                    <%
                        String statusText;
                        int status = voucher.getActive();
                        if (status == 1) {
                            statusText = "Active";
                        } else if (status == 0) {
                            statusText = "Inactive";
                        } else {
                            statusText = "Not yet started";
                        }
                    %>
                    <input type="text" class="form-control admin-manage-input"
                           value="<%= statusText%>" readonly>
                </div>
                <div class="col-12">
                    <label class="form-label admin-manage-label">Description</label>
                    <textarea class="form-control admin-manage-input"
                              disabled name="description"><%= voucher.getDescription() != null ? voucher.getDescription().replaceAll("<", "&lt;").replaceAll(">", "&gt;") : ""%></textarea>

                </div>
            </div>
        </fieldset>
    </div>

  <div class="d-flex justify-content-between align-items-center mt-4">
    <a href="<%= request.getContextPath()%>/manage-vouchers" class="admin-manage-back">
        <i class="fas fa-arrow-left mr-1"></i> Back
    </a>

    <a href="<%= request.getContextPath()%>/manage-vouchers?view=edit&id=<%= voucher.getVoucherId()%>"
       style="background-color: #EAB308; color: #000; border: none;"    class="btn admin-manage-button"  >
        <i class="fas fa-pen-to-square mr-1"></i> Edit
    </a>
</div>



    <%
        } // end else
    %>
</main>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
