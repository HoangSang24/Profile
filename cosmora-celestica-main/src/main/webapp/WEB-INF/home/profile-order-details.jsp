<%@page import="java.math.BigDecimal"%>
<%@page import="shop.model.Customer"%>
<%@page import="shop.model.OrderDetails"%>
<%@page import="shop.model.Order"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../include/home-header.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- page title -->
<section id="top-background" class="section--first " data-bg="${pageContext.servletContext.contextPath}/assets/img/bg3.png">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="section__wrap">
                    <!-- section title -->
                    <h2 class="section__title">Order Details</h2>
                    <!-- end section title -->

                    <!-- breadcrumb -->
                    <ul class="breadcrumb">
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/home">Home</a></li>
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/profile">Profile</a></li>
                        <li class="breadcrumb__item"><a href="${pageContext.servletContext.contextPath}/profile">Order History</a></li>
                        <li class="breadcrumb__item breadcrumb__item--active">Order Details</li>
                    </ul>
                    <!-- end breadcrumb -->
                </div>
            </div>
        </div>
    </div>
</section>


<script src="https://cdn.tailwindcss.com"></script>
<style>
    /* Custom scrollbar for timeline */
    .timeline::-webkit-scrollbar {
        width: 6px;
    }
    .timeline::-webkit-scrollbar-thumb {
        background-color: #a0aec0;
        border-radius: 10px;
    }
    /* Custom small line in progress steps */
    .step-line {
        height: 2px;
        background-color: #22c55e; /* green-500 */
        flex-grow: 1;
        margin: 0 6px;
    }
    /* Hide scroll bar for firefox */
    .timeline {
        scrollbar-width: thin;
        scrollbar-color: #a0aec0 transparent;
    }
    /* Dotted vertical timeline dot */
    .timeline-dot {
        width: 12px;
        height: 12px;
        border-radius: 9999px; /* full */
        background-color: #94a3b8; /* gray-400 */
        display: inline-block;
        margin-right: 12px;
        position: relative;
    }
    .timeline-dot.active {
        background-color: none;
    }
    /* Expandable text styles */
    .expandable-text {
        cursor: pointer;
        color: white; /* blue-600 */
        font-weight: 600;
    }
    /* Tooltip icon style for info */
    .tooltip-icon {
        display: inline-block;
        width: 16px;
        height: 16px;
        border-radius: 9999px;
        background-color: #cbd5e1; /* gray-300 */
        text-align: center;
        font-size: 14px;
        line-height: 15px;
        color: white; /* gray-600 */
        font-weight: 700;
        cursor: default;
        user-select: none;
    }
</style>
<main id="main-background" data-bg="<%= request.getContextPath()%>/assets/img/main-background.png">
</head>
<body class="bg-[#1B222E] font-sans text-gray-800">
    <% Order order = (Order) request.getAttribute("order");
        ArrayList<OrderDetails> orderDetails = (ArrayList) request.getAttribute("orderdetails");
        Customer customer = (Customer) session.getAttribute("currentCustomer");

        String status = order.getStatus();
        int currentStep = 0;
        boolean isCanceled = false;

        switch (status) {
            case "Pending":
                currentStep = 1;
                break;
            case "Confirmed":
                currentStep = 2;
                break;
            case "Shipping":
                currentStep = 3;
                break;
            case "Shipped":
                currentStep = 4;
                break;
            case "Order Completed":
                currentStep = 5;
                break;
            case "Canceled":
                currentStep = -1;
                isCanceled = true;
                break;
        }
    %>



    <div class="max-w-5xl mx-auto p-6" style="padding-bottom: 200px;">
        <!-- Header Top Bar -->
        <div class="flex justify-between border-b border-gray-200 pb-3 text-sm text-white">
            <button 
                aria-label="Trở lại" 
                class="flex items-center space-x-1 hover:text-blue-600 font-medium"
                onclick="history.back()"
                >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 stroke-current" fill="none" viewBox="0 0 24 24" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
                </svg>
                <span>Back</span>
            </button>

            <div>
                <% if (currentStep == 4) {
                %>
                <form action="profile-order-history" method="post">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" name="orderId" value="<%= order.getOrderId()%>" />
                    <input type="hidden" name="status" value="Order Completed" />
                    <button aria-label="Trở lại" 
                            class="flex items-center gap-2 px-4 py-2 bg-white/10 text-white hover:bg-orange-600 hover:text-white font-semibold rounded-lg transition duration-300" >
                        <span>I have received the whole product.</span>
                    </button>
                </form>

                <%}%>
                <span class="font-semibold">ORDER ID. <span class="font-bold"><%= order.getOrderId()%></span></span> 
                <span class="text-red-600 ml-4 font-semibold"><%= order.getStatus()%></span>
            </div>
        </div>

        <!-- Progress Tracker -->


        <div class="mt-6 bg-gray-50 rounded p-6">
            <div class="flex justify-between items-center relative">
                <!-- Step 1 -->
                <div class="flex flex-col items-center flex-shrink-0 min-w-[110px]">
                    <div class="rounded-full border-2
                         <%= isCanceled ? "border-red-500 text-red-500" : (currentStep >= 1 ? "border-green-500 text-green-500" : "border-gray-400 text-gray-400")%> p-3">
                        🛒
                    </div>
                    <p class="mt-2 text-sm font-semibold
                       <%= isCanceled ? "text-red-500" : (currentStep >= 1 ? "text-green-600" : "text-gray-400")%> text-center whitespace-nowrap">
                        Pending
                    </p>
                </div>

                <!-- Line 1 -->
                <div class="flex-1 h-1 mx-2" style="background-color: <%= isCanceled ? "#f87171" : (currentStep >= 2 ? "#22c55e" : "#94a3b8")%>;"></div>

                <!-- Step 2 -->
                <div class="flex flex-col items-center flex-shrink-0 min-w-[130px]">
                    <div class="rounded-full border-2
                         <%= isCanceled ? "border-red-500 text-red-500" : (currentStep >= 2 ? "border-green-500 text-green-500" : "border-gray-400 text-gray-400")%> p-3">
                        💳
                    </div>
                    <p class="mt-2 text-sm font-semibold
                       <%= isCanceled ? "text-red-500" : (currentStep >= 2 ? "text-green-600" : "text-gray-400")%> text-center whitespace-nowrap">
                        Confirmed
                    </p>
                </div>

                <!-- Line 2 -->
                <div class="flex-1 h-1 mx-2" style="background-color: <%= isCanceled ? "#f87171" : (currentStep >= 3 ? "#22c55e" : "#94a3b8")%>;"></div>

                <!-- Step 3 -->
                <div class="flex flex-col items-center flex-shrink-0 min-w-[110px]">
                    <div class="rounded-full border-2
                         <%= isCanceled ? "border-red-500 text-red-500" : (currentStep >= 3 ? "border-green-500 text-green-500" : "border-gray-400 text-gray-400")%> p-3">
                        📦
                    </div>
                    <p class="mt-2 text-sm font-semibold
                       <%= isCanceled ? "text-red-500" : (currentStep >= 3 ? "text-green-600" : "text-gray-400")%> text-center whitespace-nowrap">
                        Shipping
                    </p>
                </div>

                <!-- Line 3 -->
                <div class="flex-1 h-1 mx-2" style="background-color: <%= isCanceled ? "#f87171" : (currentStep >= 4 ? "#22c55e" : "#94a3b8")%>;"></div>

                <!-- Step 4 -->
                <div class="flex flex-col items-center flex-shrink-0 min-w-[110px]">
                    <div class="rounded-full border-2
                         <%= isCanceled ? "border-red-500 text-red-500" : (currentStep >= 4 ? "border-green-500 text-green-500" : "border-gray-400 text-gray-400")%> p-3">
                        🚚
                    </div>
                    <p class="mt-2 text-sm font-semibold
                       <%= isCanceled ? "text-red-500" : (currentStep >= 4 ? "text-green-600" : "text-gray-400")%> text-center whitespace-nowrap">
                        Shipped
                    </p>
                </div>

                <!-- Line 4 -->
                <div class="flex-1 h-1 mx-2" style="background-color: <%= isCanceled ? "#f87171" : (currentStep >= 5 ? "#22c55e" : "#94a3b8")%>;"></div>

                <!-- Step 5 -->
                <div class="flex flex-col items-center flex-shrink-0 min-w-[110px]">
                    <div class="rounded-full border-2
                         <%= isCanceled ? "border-red-500 text-red-500" : (currentStep >= 5 ? "border-green-500 text-green-500" : "border-gray-400 text-gray-400")%> p-3">
                        ✅
                    </div>
                    <p class="mt-2 text-sm font-semibold
                       <%= isCanceled ? "text-red-500" : (currentStep >= 5 ? "text-green-600" : "text-gray-400")%> text-center whitespace-nowrap">
                        Order Completed
                    </p>
                </div>
            </div>
        </div>






        <!-- Địa Chỉ Nhận Hàng &  -->
        <div class="md:flex md:space-x-8">
            <!-- Address left -->
            <div class="md:w-1/3 mb-10 md:mb-0">
                <h2 class="text-white font-semibold mb-4">Customer Information</h2>
                <div class="text-white font-semibold mb-1"><span>Name: </span><%= customer.getFullName()%></div>
                <div class="text-sm text-white mb-1"><span>Phone: </span><%= customer.getPhone()%></div>
                <div class="text-sm text-white leading-relaxed"><span>Address: </span>
                    <%= customer.getAddress()%>
                </div>
                <div class="text-sm text-white leading-relaxed "><span>Payment Method: </span>
                    <%= order.getPaymentMethod()%>
                </div>
                <div class="max-w-md ml-auto text-right space-y-1 text-white">
                    <div class="flex justify-between border-t border-gray-200 pt-2"><span>Total Amount: </span> <span><%= order.getTotalAmount()%></span></div>
                    <div class="flex justify-between"><span>Amount Reduced: </span> <span class="text-green-600 font-semibold"><%= order.getVoucherValue()%></span></div>
                    <div class="flex justify-between font-extrabold text-orange-600 text-xl border-t border-gray-200 pt-2">Final Amount: <span><%= order.getTotalAmount().subtract(order.getVoucherValue())%></span></div>
                </div>

            </div>


            <!-- Products right -->
            <div class="md:w-2/3 flex flex-col gap-6">
                <%

                    for (OrderDetails orderdetails : orderDetails) {
                %>
                <div class="flex flex-row gap-5 p-4 bg-gray-800 rounded">
                    <!-- Product image -->
                    <div class="w-28 h-28 bg-gray-100 rounded border border-gray-200 flex items-center justify-center shrink-0 relative overflow-hidden">
                        <img src="<%= request.getContextPath()%>/assets/img/<%= orderdetails.getImageURL()%>" alt="">
                    </div>
                    <div class="flex-1">
                        <h3 class="text-white font-semibold text-base leading-snug mb-1">
                            <%= orderdetails.getProductName()%>
                        </h3>
                        <p class="text-sm text-white mb-1"><%= orderdetails.getCategoryName()%></p>
                        <p class="text-sm text-white">Quantity: <%= orderdetails.getQuantity()%></p>
                        <% if (orderdetails.getGameKey() != null && !orderdetails.getGameKey().isEmpty() && orderdetails.getGameKey() != "" && !order.getStatus().equalsIgnoreCase("pending")) {
                        %>                            
                        <p class="text-sm text-white"> Game Key: <%= orderdetails.getGameKey()%> </p>

                        <%}
                        %>
                        <% if (order.getStatus().equalsIgnoreCase("Shipped") || order.getStatus().equalsIgnoreCase("Order Completed")) {%>

                        <p>
                            <%
                                Customer currentCustomer = (Customer) session.getAttribute("currentCustomer");

                                if (orderdetails.isIsProductReview()) {
                            %>
                            <p style="color: lightgreen;">This product has been previously reviewed.</p>
                            <%
                            } else {
                            %>
                            <form action="profile-order-history" method="post">
                                <input type="hidden" name="action" value="reviewsingle">
                                    <input type="hidden" name="orderId" value="<%=order.getOrderId()%>">
                                        <input type="hidden" name="productId" value="<%= orderdetails.getProductId()%>">
                                            <style>
                                                .star-rating input[type="radio"] {
                                                    display: none;
                                                }

                                                .star-rating label {
                                                    font-size: 20px;
                                                    color: #ccc;
                                                    cursor: pointer;
                                                }

                                                .star-rating input[type="radio"]:checked ~ label {
                                                    color: gold;
                                                }

                                                .star-rating label:hover,
                                                .star-rating label:hover ~ label {
                                                    color: gold;
                                                }
                                            </style>

                                            <div class="star-rating" style="direction: rtl;">
                                                <% for (int i = 5; i >= 1; i--) {%>
                                                <input type="radio" id="star<%= orderdetails.getOrderId()%>_<%= i%>" name="rating" value="<%= i%>">
                                                    <label for="star<%= orderdetails.getOrderId()%>_<%= i%>">&#9733;</label>
                                                    <% } %>
                                            </div>

                                            <button style="color: white" type="submit">Submit</button>
                                            </form>
                                            <%
                                                }
                                            %></p>
                                            <% } else if (order.getStatus().equalsIgnoreCase("Cancel")) {%>
                                            <P style="color:white">Order has been cancelled</P>
                                                <% } else {%>
                                            <p style="color:white">Order is still in transit</p><%}%>


                                            </div>
                                            <!-- Pricing summary -->
                                            <div class="max-w-md ml-auto text-right space-y-1 text-white">
                                                <div class="flex justify-between"><span>Price:</span> <span><%= orderdetails.getPrice()%></span></div>
                                                <div class="flex justify-between font-extrabold text-blue-600 text-xl border-t border-gray-200 pt-2"> <span>Total:   <%= orderdetails.getPrice().multiply(BigDecimal.valueOf(orderdetails.getQuantity()))%></span></div>
                                            </div>
                                            </div>
                                            <% }%>
                                            </div>
                                            </div>




                                            </div>

                                            </body>
                                            </html>






                                            </main>

                                            <!-- Facebook Button -->
                                            <a href="https://www.facebook.com/YourPage" 
                                               style="position: fixed;
                                               bottom: 20px;
                                               right: 20px;
                                               background-color: #4267B2;
                                               color: white;
                                               padding: 15px 20px;
                                               border-radius: 50%;
                                               font-size: 24px;
                                               display: flex;
                                               align-items: center;
                                               justify-content: center;
                                               box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
                                               transition: background-color 0.3s, transform 0.3s;
                                               text-decoration: none;
                                               cursor: pointer;
                                               z-index: 100;">
                                                <i class="fab fa-facebook-f" style="font-size: 24px;"></i>
                                            </a>

                                            <!-- Phone Button -->
                                            <a href="tel:+1234567890" 
                                               id="phoneButton"
                                               style="position: fixed;
                                               bottom: 80px;
                                               right: 20px;
                                               background-color: #34b7f1;
                                               color: white;
                                               padding: 15px;
                                               border-radius: 50%;
                                               font-size: 24px;
                                               display: flex;
                                               align-items: center;
                                               justify-content: center;
                                               box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
                                               transition: background-color 0.3s, transform 0.3s;
                                               text-decoration: none;
                                               cursor: pointer;
                                               z-index: 100;">
                                                <i class="fas fa-phone-alt"></i>
                                                <span id="phoneText" style="display: none;
                                                      position: absolute;
                                                      top: -30px;
                                                      left: 0%;
                                                      transform: translateX(-50%);
                                                      background-color: #34b7f1;
                                                      color: white;
                                                      padding: 5px 10px;
                                                      border-radius: 5px;
                                                      font-size: 14px;">+1234567890</span>
                                            </a>

                                            <script>
                                                // Hover effect to show phone number
                                                document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseover', function () {
                                                    document.getElementById('phoneText').style.display = 'block';
                                                });

                                                document.querySelector('a[href="tel:+1234567890"]').addEventListener('mouseout', function () {
                                                    document.getElementById('phoneText').style.display = 'none';
                                                });

                                                // Click-to-copy phone number functionality
                                                document.getElementById('phoneButton').addEventListener('click', function (e) {
                                                    e.preventDefault(); // Prevent the default action (making a call)

                                                    // Create a temporary input element to copy the phone number
                                                    const tempInput = document.createElement('input');
                                                    tempInput.value = "+1234567890"; // Phone number to copy
                                                    document.body.appendChild(tempInput);
                                                    tempInput.select();
                                                    tempInput.setSelectionRange(0, 99999); // For mobile devices

                                                    // Copy the text to the clipboard
                                                    document.execCommand('copy');

                                                    // Remove the temporary input element
                                                    document.body.removeChild(tempInput);

                                                    // Display a message or change button style to indicate success
                                                    alert('Phone number copied to clipboard!');
                                                });
                                            </script>
                                            <%@include file="../include/home-footer.jsp" %>