<%@page import="shop.model.Voucher"%>
<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Create Voucher</h2>
    </div>

    <%
        String error = (String) request.getAttribute("message");
        if (error != null) {
    %>
    <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 10px; margin-top: 15px; border-radius: 5px;">
        <strong>Error:</strong> <%= error%>
    </div>
    <% }%>

    <form method="post" action="<%= request.getContextPath()%>/manage-vouchers">
        <input type="hidden" name="action" value="create">
        <div class="admin-manage-type voucher-details">
            <fieldset class="mb-4 admin-manage-fieldset">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Code</label>
                        <input type="text" class="form-control admin-manage-input" name="code" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Value</label>
                        <input type="number" class="form-control admin-manage-input" name="value" min="0" step="0.01" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Usage Limit</label>
                        <input type="number" class="form-control admin-manage-input" name="usage_limit" min="0" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Minimum Order Value</label>
                        <input type="number" class="form-control admin-manage-input" name="min_order_value" min="0" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Start Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="start_date" id="start_date"  required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">End Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="end_date" id="end_date" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input" name="description" required></textarea>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Active</label>
                        <select class="form-control admin-manage-input" name="active" id="active-select" required>
                            <option value="1">Active</option>
                            <option value="0">Inactive</option>
                        </select>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="<%= request.getContextPath()%>/manage-vouchers" class="admin-manage-back">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>
            <div>
                <button type="submit" class="btn admin-manage-button">
                    <i class="fas fa-plus mr-1"></i> Create
                </button>
            </div>
        </div>
    </form>

    <script>
        function validateDateRange(changedField) {
            const startInput = document.getElementById("start_date");
            const endInput = document.getElementById("end_date");
            const select = document.querySelector('select[name="active"]');

            const today = new Date();
            today.setHours(0, 0, 0, 0);

            const startDate = startInput.value ? new Date(startInput.value + "T00:00:00") : null;
            const endDate = endInput.value ? new Date(endInput.value + "T00:00:00") : null;

            // N?u ngày b?t ??u l?n h?n ho?c b?ng ngày k?t thúc
            if (startDate && endDate && startDate >= endDate) {
                alert("Start date must be earlier than end date.");
                if (changedField === "start") {
                    startInput.value = "";
                    startInput.focus();
                } else {
                    endInput.value = "";
                    endInput.focus();
                }

                // Set tr?ng thái Inactive
                if (select) {
                    select.value = "0";
                }
            }

            // N?u ngày b?t ??u l?n h?n hôm nay => Inactive
            if (startDate && startDate > today) {
                if (select) {
                    select.value = "0";
                }
            }

            // N?u ngày k?t thúc nh? h?n hôm nay => Inactive
            if (endDate && endDate < today) {
                if (select) {
                    select.value = "0";
                }
            }
        }

        // Ch?y khi trang load
        window.addEventListener("DOMContentLoaded", () => {
            document.getElementById("start_date").addEventListener("change", function () {
                validateDateRange("start");
            });
            document.getElementById("end_date").addEventListener("change", function () {
                validateDateRange("end");
            });

            validateDateRange("start");
        });

        // Ki?m tra khi submit
        document.querySelector("form").addEventListener("submit", function (event) {
            const value = parseFloat(document.querySelector('input[name="value"]').value);
            const minOrderValue = parseFloat(document.querySelector('input[name="min_order_value"]').value);
            const startDate = new Date(document.getElementById("start_date").value + "T00:00:00");
            const endDate = new Date(document.getElementById("end_date").value + "T00:00:00");

            if (startDate >= endDate) {
                alert("Start date must be earlier than end date.");
                event.preventDefault();
                return;
            }

            if (value >= minOrderValue / 2) {
                alert("Voucher value must be less than half of the minimum order value.");
                event.preventDefault();
            }
        });
    </script>
</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
