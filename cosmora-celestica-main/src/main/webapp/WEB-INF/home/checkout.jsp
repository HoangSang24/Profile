<%@page import="shop.model.Voucher"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.Checkout"%>
<%@page import="java.util.ArrayList"%>
<%@page import="shop.model.Product"%>
<%@include file="../include/home-header.jsp" %>

<!-- Page Title Section -->
<section id="top-background" class="section--first" data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section__wrap">
                    <h2 class="section__title">Checkout</h2>
                    <ul class="breadcrumb">
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/home">Home</a></li>
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/cart">Cart</a></li>
                        <li class="breadcrumb__item breadcrumb__item--active">Checkout</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</section>
                        <%
    String successMsg = (String) session.getAttribute("applyfail");
    if (successMsg != null) {
        session.removeAttribute("applyfail"); 
%>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        alert("<%= successMsg%>");
    });
</script>

<%
    }
%>

<%    double total = (double) session.getAttribute("totalAmount");
    ArrayList<Checkout> product = (ArrayList<Checkout>) session.getAttribute("checkout");
    Customer customer = (Customer) session.getAttribute("currentCustomer");
    int i = 0;
    for (Checkout p : product) {
        if (p.getProductCategory().equalsIgnoreCase("game")) {
            i = 1;
        }
    }
%>

<main id="main-background" data-bg="<%= request.getContextPath()%>/assets/img/main-background.png" style="padding-bottom: 80px;">
    <div class="section">
        <div class="container">

            <!-- Back Button -->
         <div class="mb-4">
                <a href="javascript:void(0)" onclick="location.replace(document.referrer)" class="admin-manage-back">
                    <i class="fas fa-arrow-left mr-1"></i> Back
                </a>

            </div>   

            <div class="row">

                <!-- Left Column: Cart Info -->
                <div class="col-12 col-lg-8">
                    <div class="cart">
                        <div class="table-responsive">
                            <table class="cart__table">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th class="active">Title</th>
                                        <th>Category</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (Checkout pro : product) {%>
                                    <tr>
                                        <td>
                                            <div class="cart__img">
                                                <img src="<%= request.getContextPath()%>/assets/img/<%= pro.getImageURL()%>" alt="">
                                            </div>
                                        </td>
                                        <td><a href="#"><%= pro.getProductName()%></a></td>
                                        <td><%= pro.getProductCategory()%></td>
                                        <td><span class="cart__price">$<%= (pro.getSale_price() == 0.0) ? pro.getPrice() : pro.getSale_price()%></span></td>
                                        <td><span class="cart__price"><%= pro.getQuantity()%></span></td>
                                    </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>

                        <!-- Voucher Handling -->
                        <%
                            double vouchervalue = 0.0;
                            Boolean voucherApplied = (Boolean) request.getAttribute("voucherApplied");
                            if (voucherApplied == null || !voucherApplied) {
                        %>
                        <div class="cart__info">
                            <div class="cart__total">
                                <p>Total:</p>
                                <span>$<%= total%></span>
                            </div>
                            <div class="cart__systems">
                                <i class="pf pf-visa"></i>
                                <i class="pf pf-mastercard"></i>
                                <i class="pf pf-paypal"></i>
                            </div>
                        </div>
                        <% } else {
                            vouchervalue = (double) session.getAttribute("voucherValue");
                        %>
                        <div class="cart__info">
                            <div class="cart__total">
                                <p>Total:</p>
                                <span>$<%= total%></span>
                                <span> - $<%= vouchervalue%></span>
                                <span>= $<%= total - vouchervalue%></span>
                            </div>
                            <div class="cart__systems">
                                <i class="pf pf-visa"></i>
                                <i class="pf pf-mastercard"></i>
                                <i class="pf pf-paypal"></i>
                            </div>
                        </div>
                        <% } %>
                    </div>

                    <!-- Help Box -->
                    <%
                        ArrayList<Voucher> voucherlist = (ArrayList<Voucher>) request.getAttribute("voucherslist");
                        if (voucherlist != null && !voucherlist.isEmpty()) {
                    %>
                    <div class="section" style="background-color: rgba(255,255,255,0.05); padding: 20px; margin-top: 40px; border-radius: 10px; color: white;">
                        <h4 style="margin-bottom: 15px; font-weight: 600;">Available Voucher Codes</h4>

                        <!-- Grid style -->
                        <div style="display: flex; flex-wrap: wrap; gap: 15px;">
                            <% for (Voucher voucher : voucherlist) {
                                    if (voucher.getActive() != 0) {


                            %>

                            <div style="
                                 flex: 1 1 calc(33.333% - 10px);
                                 background-color: rgba(255,255,255,0.03);
                                 padding: 15px;
                                 border-radius: 8px;
                                 border: 1px dashed #4fc3f7;
                                 min-width: 220px;
                                 box-sizing: border-box;
                                 ">
                                <div style="font-size: 16px; font-weight: bold; color: #4fc3f7; margin-bottom: 5px;">
                                    <i class="fas fa-tag"></i> <%= voucher.getCode()%>
                                </div>
                                <div style="color: #ccc; font-size: 14px;">
                                    <div><%= voucher.getDescription()%></div>
                                    <div>Min Order: $<%= voucher.getMinOrderValue()%></div>
                                    <div>Discount: $<%= voucher.getValue()%></div>
                                </div>
                            </div>
                            <% }
                                } %>
                        </div>

                        <p style="margin-top: 15px; font-size: 13px; color: #aaa;">
                            Copy one of the codes above and enter it manually in the coupon field on the right to apply.
                        </p>
                    </div>
                    <% } %>
                </div>

                <!-- Right Column: Payment & Info -->
                <div class="col-12 col-lg-4">

                    <!-- Voucher Form -->
                    <% if (voucherApplied == null || !voucherApplied) { %>
                    <form action="ApplyVoucherServlet" method="post" class="form form--first form--coupon">
                        <input type="text" name="voucher" class="form__input" placeholder="Coupon code">
                        <button type="submit" class="form__btn">Apply</button>
                    </form>
                    <% } else { %>
                    <p style="color: lightgreen;">Voucher applied successfully!</p>
                    <% }%>

                    <!-- Checkout Form -->
                    <form action="checkout" method="post" class="form">
                        <input type="text" name="customerName" value="<%= customer.getFullName()%>" readonly class="form__input" style="color: white; background-color: transparent; border: none;" />
                        <input type="text" name="customerAddress" value="<%= customer.getAddress()%>" required class="form__input" style="color: white; background-color: transparent; border: none;" />
                        <input type="text" name="customerEmail" value="<%= customer.getEmail()%>" readonly class="form__input" style="color: white; background-color: transparent; border: none;" />
                        <input type="text" name="customerPhone" value="<%= customer.getPhone()%>" readonly class="form__input" style="color: white; background-color: transparent; border: none;" />

                        <% for (Checkout pro : product) {%>
                        <input type="hidden" name="productId" value="<%= pro.getProductId()%>" />
                        <input type="hidden" name="quantity" value="<%= pro.getQuantity()%>" />
                        <input type="hidden" name="price" value="<%= (pro.getSale_price() == 0.0) ? pro.getPrice() : pro.getSale_price()%>" />
                        <% }%>

                        <input type="hidden" name="total" value="<%= total - vouchervalue%>" />
                        <input type="hidden" name="customerId" value="<%= customer.getCustomerId()%>" />
                        <input type="hidden" name="vouchercode" value="<%= request.getAttribute("voucherCode")%>" />
                        <input type="hidden" name="action" value="order" />

                        <select name="paymentMethod" class="form__select">
                            <option value="visa">Visa</option>
                            <option value="mastercard">Mastercard</option>
                            <option value="paypal">Paypal</option>
                            <% if (i != 1) { %>
                            <option value="Cash On Delivery">Cash On Delivery</option>
                            <% }%>
                        </select>

                        <span class="form__text">There are many variations of passages of Lorem Ipsum...</span>
                        <button type="submit" class="form__btn">Proceed to checkout</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Floating Facebook Button -->
<a href="https://www.facebook.com/YourPage" 
   style="position: fixed; bottom: 20px; right: 20px; background-color: #4267B2; color: white; padding: 15px 20px; border-radius: 50%; font-size: 24px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); transition: background-color 0.3s, transform 0.3s; text-decoration: none; cursor: pointer; z-index: 100;">
    <i class="fab fa-facebook-f"></i>
</a>

<!-- Floating Phone Button -->
<a href="tel:+1234567890" id="phoneButton"
   style="position: fixed; bottom: 80px; right: 20px; background-color: #34b7f1; color: white; padding: 15px; border-radius: 50%; font-size: 24px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); transition: background-color 0.3s, transform 0.3s; text-decoration: none; cursor: pointer; z-index: 100;">
    <i class="fas fa-phone-alt"></i>
    <span id="phoneText" style="display: none; position: absolute; top: -30px; left: 0%; transform: translateX(-50%); background-color: #34b7f1; color: white; padding: 5px 10px; border-radius: 5px; font-size: 14px;">+1234567890</span>
</a>

<script>
    document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseover', function () {
        document.getElementById('phoneText').style.display = 'block';
    });
    document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseout', function () {
        document.getElementById('phoneText').style.display = 'none';
    });
    document.getElementById('phoneButton').addEventListener('click', function (e) {
        e.preventDefault();
        const tempInput = document.createElement('input');
        tempInput.value = "+1234567890";
        document.body.appendChild(tempInput);
        tempInput.select();
        document.execCommand('copy');
        document.body.removeChild(tempInput);
        alert('Phone number copied to clipboard!');
    });
</script>

<%@include file="../include/home-footer.jsp" %>
