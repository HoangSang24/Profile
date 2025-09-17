<%-- 
    Document   : home
    Created on : Jun 17, 2025, 11:45:00 PM
    Author     : SangNH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="shop.model.Product"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%
    shop.model.Customer currentCustomer = (shop.model.Customer) session.getAttribute("currentCustomer");
%>




<%@include file="/WEB-INF/include/home-header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/product-list.css">

<section class="section_banner section--bg section__ad--bg section--first" data-bg="<%= request.getContextPath()%>/assets/img/banner.png">
    <h1 style="
        text-align: center;
        color: white;
        font-size: 3rem;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 2px;
        text-shadow: 2px 2px 10px rgba(0, 0, 0, 0.6);
        margin-top: 100px;">
        Explore Top Games & Gaming Accessories
    </h1>
</section>

<%
    String successMsg = (String) session.getAttribute("buysucces");
    if (successMsg != null) {
        session.removeAttribute("buysucces"); // Xóa sau khi hiển thị
%>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        alert("<%= successMsg%>");
    });
</script>

<%
    }
%>
<%
    String successMs = (String) session.getAttribute("received");
    if (successMs != null) {
        session.removeAttribute("received");
%>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        alert("<%= successMs%>");
    });
</script>

<%
    }
%>

<style>

    /* CSS cho phần hiển thị sao - PHIÊN BẢN SỬA LỖI NẰM NGANG */
    .card__rating {
        display: flex;       /* BẬT FLEXBOX: Buộc các phần tử con (các ngôi sao) nằm ngang */
        flex-direction: row; /* Hướng từ trái sang phải (đây là mặc định nhưng ghi rõ cho chắc) */
        align-items: center; /* Căn giữa các sao theo chiều dọc (đề phòng chiều cao không đều) */
        gap: 2px;            /* Tạo khoảng cách 2px giữa các ngôi sao cho đẹp hơn */
        margin: 5px 0 10px;
        font-size: 18px;
    }

    .star-icon {
        /* display: inline-block; -- Thuộc tính này không còn cần thiết khi container đã dùng flexbox */
        line-height: 1; /* Đảm bảo các ngôi sao không có chiều cao dòng thừa */
    }

    .star-icon.full {
        color: #ffc107; /* Màu vàng cho sao đầy */
    }
    .red-price {
        color: red;
        font-weight: bold;
    }

    .star-icon.empty {
        color: #e0e0e0; /* Màu xám nhạt cho sao rỗng */
    }
</style>
<main id="main-background" data-bg="<%= request.getContextPath()%>/assets/img/main-background.png">
    <section class="custom-section">
        <div class="container">
            <div class="row">
                <div class="col-12"> 
                    <form action="<%= request.getContextPath()%>/home" method="get">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="keyword" class="search-input" placeholder="Enter product name..." value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">
                        <button class="search-btn">Search</button>
                        <a href="home" class="clear-search-btn" 
                           style="background-color: #ef4444;
                           color: #fff;
                           padding: 8px;
                           border-radius: 13px;">
                            <i class="fas fa-times"></i> Clear
                        </a>
                    </form>
                </div>
                <div class="col-12 mt-3 text-center">
                    <h2>Category:</h2>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword='">All</button>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword=game'">Game</button>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword=headset'">Headset</button>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword=keyboard'">KeyBoard</button>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword=mouse'">Mouse</button>
                    <button class="btn custom-category-btn" onclick="location.href = '<%= request.getContextPath()%>/home?action=filter&keyword=controller'">Controller</button>
                </div>
            </div>

            <div class="row mt-5"><div class="col-12"><h2 class="section__title">Featured Accessories</h2></div></div>
            <div class="slider-wrapper">
                <div class="slider-viewport" id="accessory-viewport">
                    <div class="slider-track" id="accessory-track">
                        <%
                            List<Product> accessoryList = (List<Product>) request.getAttribute("accessoryList");
                            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                            if (accessoryList != null && !accessoryList.isEmpty()) {
                                for (Product product : accessoryList) {
                        %>
                        <div class="slider-item">
                            <div class="card card--catalog card--uniform">
                                <a href="<%= request.getContextPath()%>/home?action=details&productId=<%= product.getProductId()%>" class="card__cover">
                                    <img src="<%= request.getContextPath()%>/assets/img/<%= (product.getImageUrls() != null && !product.getImageUrls().isEmpty() ? product.getImageUrls().get(0) : "default-product.png")%>" alt="<%= product.getName()%>" />
                                </a>
                                <div class="card__title">
                                    <h3 class="card__brand"><%= (product.getBrandName() != null && !product.getBrandName().isEmpty() ? product.getBrandName() : "N/A")%></h3>
                                    <h3><a href="<%= request.getContextPath()%>/home?action=details&productId=<%= product.getProductId()%>"><%= product.getName()%></a></h3>
                                        <%-- BẮT ĐẦU: Hiển thị đánh giá sao --%>
                                    <div class="card__rating">
                                        <%
                                            long roundedStarsGame = Math.round(product.getAverageStars());
                                            for (int i = 1; i <= 5; i++) {
                                                if (i <= roundedStarsGame) {
                                        %>
                                        <span class="star-icon full">&#9733;</span>
                                        <%
                                        } else {
                                        %>
                                        <span class="star-icon empty">&#9733;</span>
                                        <%
                                                }
                                            }
                                        %>
                                    </div>
                                    <%-- KẾT THÚC: Hiển thị đánh giá sao --%>
                                    <span>
                                        <% if ( product.getDiscount().getSale_price() != null && product.getDiscount().getSale_price().compareTo(BigDecimal.ZERO) > 0 && product.getActive() == 1 ) {%>
                                        <span style="color: red;"><%= currencyFormatter.format(product.getDiscount().getSale_price())%></span>
                                        <s><%= currencyFormatter.format(product.getPrice())%></s>
                                            <% } else {%>
                                            <%= currencyFormatter.format(product.getPrice())%>
                                            <% }%>
                                    </span>
                                </div>
                                <div class="card__actions">
                                    <button type="button" class="btn btn-buy" onclick="location.href = '<%= request.getContextPath()%>/checkout?view=single&id=<%= product.getProductId()%>&quantity=<%= 1%>'" >Buy Now</button>

                                    <form action="<%= request.getContextPath()%>/cart" method="POST">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="page" value="home">
                                        <input type="hidden" name="username" value="<%= currentCustomer != null ? currentCustomer.getUsername() : ""%>">
                                        <input type="hidden" name="productId" value="<%= product.getProductId()%>">
                                        <input type="hidden" name="quantity" value="1">

                                        <button class="card__addtocart" type="submit">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="512" height="512" viewBox="0 0 512 512">
                                            <circle cx="176" cy="416" r="16" style="fill:none;
                                                    stroke-linecap:round;
                                                    stroke-linejoin:round;
                                                    stroke-width:32px"/>
                                            <circle cx="400" cy="416" r="16" style="fill:none;
                                                    stroke-linecap:round;
                                                    stroke-linejoin:round;
                                                    stroke-width:32px"/>
                                            <polyline points="48 80 112 80 160 352 416 352" style="fill:none;
                                                      stroke-linecap:round;
                                                      stroke-linejoin:round;
                                                      stroke-width:32px"/>
                                            <path d="M160,288H409.44a8,8,0,0,0,7.85-6.43l28.8-144a8,8,0,0,0-7.85-9.57H128" style="fill:none;
                                                  stroke-linecap:round;
                                                  stroke-linejoin:round;
                                                  stroke-width:32px"/>
                                            </svg>
                                        </button>
                                    </form>

                                </div>
                            </div>
                        </div>
                        <% }
                                    } %>
                    </div>
                </div>
                <button id="prevAccessory" class="slider-nav prev-nav">&#x2039;</button>
                <button id="nextAccessory" class="slider-nav next-nav">&#x203A;</button>
            </div>

            <div class="row mt-5"><div class="col-12"><h2 class="section__title section__title--margin">Featured Games</h2></div></div>
            <div class="slider-wrapper">
                <div class="slider-viewport" id="game-viewport">
                    <div class="slider-track" id="game-track">
                        <%
                            List<Product> gameList = (List<Product>) request.getAttribute("gameList");
                            if (gameList != null && !gameList.isEmpty()) {
                                for (Product game : gameList) {

                        %>
                        <div class="slider-item">
                            <div class="card card--catalog card--uniform">
                                <a href="<%= request.getContextPath()%>/home?action=details&productId=<%= game.getProductId()%>" class="card__cover">
                                    <img src="<%= request.getContextPath()%>/assets/img/<%= (game.getImageUrls() != null && !game.getImageUrls().isEmpty() ? game.getImageUrls().get(0) : "default-product.png")%>" alt="<%= game.getName()%>" />
                                </a>
                                <div class="card__title">
                                    <h3 class="card__brand"><%= (game.getBrandName() != null && !game.getBrandName().isEmpty() ? game.getBrandName() : "N/A")%></h3>
                                    <%-- BẮT ĐẦU: Hiển thị đánh giá sao --%>
                                    <div class="card__rating">
                                        <%
                                            long roundedStarsGame = Math.round(game.getAverageStars());
                                            for (int i = 1; i <= 5; i++) {
                                                if (i <= roundedStarsGame) {
                                        %>
                                        <span class="star-icon full">&#9733;</span>
                                        <%
                                        } else {
                                        %>
                                        <span class="star-icon empty">&#9733;</span>
                                        <%
                                                }
                                            }
                                        %>
                                    </div>
                                    <%-- KẾT THÚC: Hiển thị đánh giá sao --%>
                                    <h3><a href="<%= request.getContextPath()%>/home?action=details&productId=<%= game.getProductId()%>"><%= game.getName()%></a></h3>
                                    <span>
                                        <% if (game.getDiscount().getSale_price() != null && game.getDiscount().getSale_price().compareTo(BigDecimal.ZERO) > 0 && game.getActive() == 1) {%>
                                        <span style="color: red;"><%= currencyFormatter.format(game.getDiscount().getSale_price())%></span>
                                        <s><%= currencyFormatter.format(game.getPrice())%></s>
                                            <% } else {%>
                                            <%= currencyFormatter.format(game.getPrice())%>
                                            <% }%>
                                    </span>
                                </div>
                                <div class="card__actions">
                                    <button type="button" class="btn btn-buy" onclick="location.href = '<%= request.getContextPath()%>/checkout?view=single&id=<%= game.getProductId()%>&quantity=<%= 1%>'" >Buy Now</button>
                                    <form action="<%= request.getContextPath()%>/cart" method="POST">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="page" value="home">
                                        <input type="hidden" name="username" value="<%= currentCustomer != null ? currentCustomer.getUsername() : ""%>">
                                        <input type="hidden" name="productId" value="<%= game.getProductId()%>">
                                        <input type="hidden" name="quantity" value="1">

                                        <button class="card__addtocart" type="submit">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="512" height="512" viewBox="0 0 512 512">
                                            <circle cx="176" cy="416" r="16" style="fill:none;
                                                    stroke-linecap:round;
                                                    stroke-linejoin:round;
                                                    stroke-width:32px"/>
                                            <circle cx="400" cy="416" r="16" style="fill:none;
                                                    stroke-linecap:round;
                                                    stroke-linejoin:round;
                                                    stroke-width:32px"/>
                                            <polyline points="48 80 112 80 160 352 416 352" style="fill:none;
                                                      stroke-linecap:round;
                                                      stroke-linejoin:round;
                                                      stroke-width:32px"/>
                                            <path d="M160,288H409.44a8,8,0,0,0,7.85-6.43l28.8-144a8,8,0,0,0-7.85-9.57H128" style="fill:none;
                                                  stroke-linecap:round;
                                                  stroke-linejoin:round;
                                                  stroke-width:32px"/>
                                            </svg>
                                        </button>
                                    </form>


                                </div>
                            </div>
                        </div>
                        <% }

                            }%>
                    </div>
                </div>
                <button id="prevGame" class="slider-nav prev-nav">&#x2039;</button>
                <button id="nextGame" class="slider-nav next-nav">&#x203A;</button>
            </div>
        </div>
    </section>
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

<script>
    function initializeFreeSlider(viewportId, prevButtonId, nextButtonId) {
        const viewport = document.getElementById(viewportId);
        if (!viewport)
            return;

        const prevButton = document.getElementById(prevButtonId);
        const nextButton = document.getElementById(nextButtonId);

        const scrollAmount = viewport.clientWidth * 0.8;

        nextButton.addEventListener('click', function () {
            viewport.scrollBy({left: scrollAmount, behavior: 'smooth'});
        });

        prevButton.addEventListener('click', function () {
            viewport.scrollBy({left: -scrollAmount, behavior: 'smooth'});
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        initializeFreeSlider('accessory-viewport', 'prevAccessory', 'nextAccessory');
        initializeFreeSlider('game-viewport', 'prevGame', 'nextGame');
    });
</script>

<%@include file="/WEB-INF/include/home-footer.jsp" %>


