<%@page import="shop.model.Discount"%>
<%@page import="shop.model.Product"%>
<%@page import="shop.model.Voucher"%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<%
    Discount product = (Discount) request.getAttribute("product");
  
%>

<main class="admin-main">
    <div class="table-header">

        <h2 class="table-title">Edit Discount</h2>
        <%
            if (product == null) {
        %>
        <div style="border: 1px solid red; background-color: #ffe6e6; color: red; padding: 15px; border-radius: 5px;">
            <strong>Error:</strong> Discount not found.
        </div>
        <a href="<%= request.getContextPath()%>/manage-vouchers" class="btn btn-secondary mt-3">
            ? Back to Voucher List
        </a>
        <%
        } else {
        %>
    </div>
    <%
        String error = (String) request.getAttribute("message");
        if (error != null) {
    %>
    <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <strong>Error:</strong> <%= error%>
    </div>

    <%
        }
    %>
    <form method="post" action="<%= request.getContextPath()%>/manage-discounts">
        <input type="hidden" name="action" value="edit" >
        <input type="hidden" name="id" value="<%= product.getDiscount_id()%>" >
        <div class="admin-manage-type voucher-details">
            <fieldset class="mb-4 admin-manage-fieldset">                  
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Name</label>
                        <input type="text" class="form-control admin-manage-input"
                               name="name" value="<%= product.getProduct().getName()%>"  readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Price</label>
                        <input type="number" class="form-control admin-manage-input"
                               id="price"   name="price"  value="<%=product.getProduct().getPrice()%>" readonly >
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Sale Price</label>
                        <input type="number" class="form-control admin-manage-input"
                               step="0.01"  id="saleprice" name="saleprice" value="<%= product.getSale_price()%>" min="0" step="0.01" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Start Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="start_date" id="start_date" value="<%= product.getStarDate()%>" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">End Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="end_date" id="end_date" value="<%= product.getEndaDate()%>" required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input"
                                  required name="description"><%= product.getDescription() != null ? product.getDescription().replaceAll("<", "&lt;").replaceAll(">", "&gt;") : ""%></textarea>

                    </div>
                </div>
            </fieldset>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-4">
            <a href="<%= request.getContextPath()%>/manage-discounts" class="admin-manage-back">
                <i class="fas fa-arrow-left mr-1"></i> Back
            </a>

            <div>
                <button type="submit" class="btn admin-manage-button">
                    <i class="fas fa-pen-to-square mr-1"></i> Update
                </button>
            </div>
        </div>

    </form>
    <script>
        function validateDateRange(changedField) {
            const startInput = document.getElementById("start_date");
            const endInput = document.getElementById("end_date");
            const select = document.querySelector('select[name="active"]'); // n?u kh?ng c? select n?y th? b?

            const today = new Date();
            today.setHours(0, 0, 0, 0);

            // Chuy?n ??i gi? tr? input sang Date
            const startDate = startInput.value ? new Date(startInput.value + "T00:00:00") : null;
            const endDate = endInput.value ? new Date(endInput.value + "T00:00:00") : null;

            // ? Ki?m tra start >= end
            if (startDate && endDate && startDate >= endDate) {
                alert("Start date must be earlier than end date.");
                if (changedField === "start") {
                    startInput.value = "";
                    startInput.focus();
                } else {
                    endInput.value = "";
                    endInput.focus();
                }
                return;
            }

            // ? N?u b?n c? dropdown tr?ng th?i th? c?p nh?t n? (n?u kh?ng c? th? b? ph?n n?y ?i)
            if (select) {
                if (startDate && startDate > today) {
                    select.innerHTML = `<option value="2" selected>Not yet started</option>`;
                } else if (endDate && endDate < today) {
                    select.innerHTML = `<option value="0" selected>Inactive</option>`;
                } else {
                    select.innerHTML = `
                <option value="1">Active</option>
                <option value="0">Inactive</option>
            `;
                }
            }
        }
        window.addEventListener("DOMContentLoaded", () => {
            document.getElementById("start_date").addEventListener("change", function () {
                validateDateRange("start");
            });
            document.getElementById("end_date").addEventListener("change", function () {
                validateDateRange("end");
            });

            // G?i l?n ??u khi trang v?a load (n?u ?? c? ng?y start ???c prefill)
            validateDateRange("start");
        });
        document.querySelector("form").addEventListener("submit", function (event) {
            const price = parseFloat(document.querySelector('input[name="price"]').value);
            const salePrice = parseFloat(document.querySelector('input[name="saleprice"]').value);

            if (salePrice >= price / 2 || salePrice <= 0) {
                alert("Sale price must be greater than 0 and less than half of the original price.");
                event.preventDefault(); // ? ch?n submit
            }
        });
    </script>

    <%
        } // end else
    %>




</main>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
