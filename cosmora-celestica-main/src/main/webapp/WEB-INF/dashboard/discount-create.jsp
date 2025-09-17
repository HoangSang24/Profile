<%@page import="shop.model.Product"%>
<%@page import="java.util.List"%>
<%@page import="shop.model.Voucher"%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>


<main class="admin-main">
    <div class="table-header">
        <h2 class="table-title">Create Discount</h2>
    </div>
    <%
        String error = (String) request.getAttribute("message");
        if (error != null) {
    %>
    <div style="border: 1px solid green; background-color: yellow; color: black; padding: 10px; margin-bottom: 15px; border-radius: 5px;">
        <strong>Error:</strong> <%= error%>
    </div>
    <% }%>
    <form method="post" action="<%= request.getContextPath()%>/manage-discounts">
        <input type="hidden" name="action" value="create" >
         <input type="hidden" name="active" value="0">
        <div class="admin-manage-type voucher-details">
            <fieldset class="mb-4 admin-manage-fieldset">                  
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Select Product</label>
                        <select class="form-select admin-manage-input" name="product_id" required>
                            <option value="">-- Select a product --</option>
                            <%
                                List<Product> productList = (List<Product>) request.getAttribute("productList");
                                if (productList != null) {
                                    for (Product product : productList) {
                            %>
                            <option value="<%= product.getProductId()%>" data-price="<%= product.getPrice()%>">
                                <%= product.getName()%> (ID: <%= product.getProductId()%>)
                            </option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Price</label>
                        <input type="number" class="form-control admin-manage-input" id="price" name="price" readonly>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Sale Price</label>
                        <input type="number" class="form-control admin-manage-input"
                               name="saleprice" id="saleprice" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">Start Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="start_date" id="start_date"  required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label admin-manage-label">End Date</label>
                        <input type="date" class="form-control admin-manage-input"
                               name="end_date" id="end_date"  required>
                    </div>
                    <div class="col-12">
                        <label class="form-label admin-manage-label">Description</label>
                        <textarea class="form-control admin-manage-input"
                                  required name="description"></textarea>
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

</main>
<!-- JS x? lý auto-fill giá -->
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
   
    
    document.addEventListener('DOMContentLoaded', function () {
        const productSelect = document.querySelector('select[name="product_id"]');
        const priceInput = document.getElementById('price');

        productSelect.addEventListener('change', function () {
            const selectedOption = productSelect.options[productSelect.selectedIndex];
            const price = selectedOption.getAttribute('data-price');
            priceInput.value = price ? price : '';
        });
    });
</script>
<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>
