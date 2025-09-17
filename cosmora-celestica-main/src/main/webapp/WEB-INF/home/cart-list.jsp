<%@page import="shop.model.CartItem"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@include file="../include/home-header.jsp" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Cart</title>
    </head>
    <body>

        <section id="top-background" class="section--first" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
            <div class="container">
                <h2 class="section__title">Cart</h2>
                <ul class="breadcrumb" style="display: flex; justify-content: flex-end;">
                    <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/home">Home</a></li>
                    <li class="breadcrumb__item breadcrumb__item--active">Cart</li>
                </ul>
            </div>
        </section>

        <main id="main-background" data-bg="${pageContext.servletContext.contextPath}/assets/img/main-background.png">
            <div class="container pt-5">
                <a href="${pageContext.servletContext.contextPath}/home" class="btn btn-secondary mb-3">Back</a>

                <% List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems"); %>

                <% if (cartItems == null || cartItems.isEmpty()) { %>
                <h3 style="color: white; padding-bottom: 700px">Your cart is empty.</h3>
                <% } else { %>
                <div id="messageContainer"></div>
                <div class="table-responsive">
                    <table class="table table-dark">
                        <thead>
                            <tr>
                                <th>Check</th>
                                <th>Image</th>
                                <th>Title</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Total</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (CartItem item : cartItems) {
                                    double unitPrice = item.getSalePrice() != null ? item.getSalePrice() : item.getPrice();
                                    double totalPrice = unitPrice * item.getCartQuantity();
                            %>
                            <tr id="row-<%= item.getProductId()%>">
                                <td>

                                    <input type="checkbox"
                                           class="product-check"
                                           data-product-id="<%= item.getProductId()%>"
                                           data-quantity="<%= item.getCartQuantity()%>"
                                           data-price="<%= String.format(Locale.ENGLISH, "%.2f", totalPrice)%>"
                                           onchange="calculateSummary()"
                                           style="transform: scale(1.5); appearance: auto; margin-right: 10px;" />
                                </td>
                                <td><img src="${pageContext.servletContext.contextPath}/assets/img/<%= item.getImageUrl()%>" width="50"></td>
                                <td><%= item.getProductName()%></td>
                                <td><%= item.getCategoryName()%></td>
                                <td>$<%= String.format("%.2f", unitPrice)%></td>
                                <td>
                                    <div class="quantity-control">
                                        <button style="background-color: gray" onclick="handleDecrease(<%= item.getProductId()%>)">-</button>
                                        <span id="quantity-<%= item.getProductId()%>"><%= item.getCartQuantity()%></span>
                                        <button
                                            style="background-color: gray"
                                            class="btn-increase"
                                            id="btn-increase-<%= item.getProductId()%>"
                                            onclick="updateCart('increase', <%= item.getProductId()%>)"
                                            <%= item.getCartQuantity() >= item.getProductQuantity() ? "disabled" : ""%>>
                                            +
                                        </button>
                                    </div>
                                </td>
                                <td>$<span id="total-<%= item.getProductId()%>"><%= String.format("%.2f", totalPrice)%></span></td>
                                <td>
                                    <button class="btn-action btn-delete" onclick="confirmDelete(<%= item.getProductId()%>)">Delete</button>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

                <div class="mt-3" style="padding-bottom: 500px;">
                    <h3 style="color: white;">Summary: $<span id="summaryTotal">0.00</span></h3>

                    <form id="checkoutForm" action="checkout" method="post">
                        <input type="hidden" name="totalAmount" id="hiddenTotalAmount" value="">
                        <div class="checkout_btn">
                            <button type="button" class="form__btn" id="checkoutBtn" onclick="prepareAndSubmitForm()" disabled>
                                Proceed to checkout
                            </button>
                        </div>
                    </form>
                </div>
                <% }%>
            </div>
        </main>

        <script>
            function updateCart(action, productId) {
                fetch('<%= request.getContextPath()%>/api/cart', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({action: action, productId: productId, quantity: 1})
                })
                        .then(res => res.json())
                        .then(data => {
                            if (data.status === 'success') {


                                if (action === 'delete' && data.deleted) {
                                    document.getElementById('row-' + productId).remove();
                                } else {
                                    document.getElementById('quantity-' + productId).innerText = data.newQuantity;
                                    document.getElementById('total-' + productId).innerText = data.newTotal.toFixed(2);

                                    let checkbox = document.querySelector(`.product-check[data-product-id='${productId}']`);
                                    if (checkbox) {
                                        checkbox.dataset.price = data.newTotal.toFixed(2);
                                        checkbox.dataset.quantity = data.newQuantity;

                                    }

                                    const btnIncrease = document.getElementById('btn-increase-' + productId);
                                    if (btnIncrease) {
                                        btnIncrease.disabled = !data.canIncrease;
                                    }
                                }
                                calculateSummary();
                            } else {
                                showMessage(data.message, false);
                            }
                        })
                        .catch(err => console.error(err));
            }

            function handleDecrease(productId) {
                const quantity = parseInt(document.getElementById('quantity-' + productId).innerText);
                if (quantity <= 1) {
                    if (confirm("Are you sure you want to remove this product from your cart?")) {
                        updateCart('delete', productId);
                    }
                } else {
                    updateCart('decrease', productId);
                }
            }

            function confirmDelete(productId) {
                if (confirm("Are you sure you want to delete this product from your cart?")) {
                    updateCart('delete', productId);
                }
            }

            function calculateSummary() {
                let total = 0;
                document.querySelectorAll('.product-check:checked').forEach(cb => {
                    const productId = cb.dataset.productId;
                    const totalElement = document.getElementById('total-' + productId);
                    if (totalElement) {
                        const itemTotal = parseFloat(totalElement.innerText.replace('$', '').trim());
                        if (!isNaN(itemTotal)) {
                            total += itemTotal;
                        }
                    }
                });
                document.getElementById('summaryTotal').innerText = total.toFixed(2);
                document.getElementById('checkoutBtn').disabled = total === 0;
            }

            function prepareAndSubmitForm() {
                const form = document.getElementById("checkoutForm");
                document.querySelectorAll(".dynamic-input").forEach(e => e.remove());

                const checkboxes = document.querySelectorAll('.product-check:checked');
                let total = 0;

                checkboxes.forEach(cb => {
                    const productId = cb.getAttribute("data-product-id");
                    const quantityElement = document.getElementById("quantity-" + productId);
                    const quantity = quantityElement ? quantityElement.innerText.trim() : "1";
                    const price = parseFloat(cb.getAttribute("data-price"));
                    total += isNaN(price) ? 0 : price;

                    const idInput = document.createElement("input");
                    idInput.type = "hidden";
                    idInput.name = "productIds";
                    idInput.value = productId;
                    idInput.classList.add("dynamic-input");
                    form.appendChild(idInput);

                    const quantityInput = document.createElement("input");
                    quantityInput.type = "hidden";
                    quantityInput.name = "quantities";
                    quantityInput.value = quantity;
                    quantityInput.classList.add("dynamic-input");
                    form.appendChild(quantityInput);
                });

                const summaryText = document.getElementById("summaryTotal").innerText.trim();
                const summaryTotal = parseFloat(summaryText);
                document.getElementById("hiddenTotalAmount").value = isNaN(summaryTotal) ? 0 : summaryTotal.toFixed(2);

                const actionInput = document.createElement("input");
                actionInput.type = "hidden";
                actionInput.name = "action";
                actionInput.value = "fromcart";
                actionInput.classList.add("dynamic-input");
                form.appendChild(actionInput);

                form.submit();
            }

            document.addEventListener('DOMContentLoaded', calculateSummary);


        </script>

        <%@include file="/WEB-INF/include/home-footer.jsp" %>
    </body>
</html>