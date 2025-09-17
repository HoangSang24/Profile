<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="shop.model.Product"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- Bổ sung JSTL Core Tag Library --%>

<%@include file="/WEB-INF/include/home-header.jsp" %>
<%    shop.model.Customer currentCustomer = (shop.model.Customer) session.getAttribute("currentCustomer");

%>
<link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/product-list.css">
<%-- Đã xóa thẻ <link> lặp lại bên dưới --%>

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
    alert("<%= successMsg%>");
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
                <div class="col-12 mt-3 text-center">
                    <%
                        List<String> osList = (List<String>) request.getAttribute("osList");
                        String keyword = (String) request.getAttribute("messageFilter");
                    %>

                    <% if (osList != null) {%>
                    <h2>Attribute:</h2>
                    <%      for (String elem : osList) {
                    %>

                    <form action="<%= request.getContextPath()%>/home" method="post" style="display:inline-block; margin: 5px;">
                        <input type="hidden" name="action" value="filterofattribute">
                        <input type="hidden" name="attribute" value="<%= elem%>">

                        <input type="hidden" name="keyword" value="<%= keyword%>">

                        <button type="submit" class="btn custom-category-btn"><%= elem%></button>
                    </form>
                    <%   }
                        } %>
                </div>

            </div>
            <div id="message" style="color: yellow;
                 margin: 5px;">
                <p id="messageText">
                    <c:choose>
                        <c:when test="${not empty messageFilter}">
                            Currently searching for: ${messageFilter}
                        </c:when>
                        <c:otherwise>
                            Currently searching for: All
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="row">
                <%
                    // Lấy productList từ request
                    List<Product> productList = (List<Product>) request.getAttribute("productList");
                    // Khởi tạo NumberFormat
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

                    // Kiểm tra và lặp qua danh sách sản phẩm
                    if (productList != null && !productList.isEmpty()) {
                        for (Product product : productList) {
                %>
                <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                    <div class="card card--catalog card--uniform h-100">
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
                                <%-- Logic hiển thị giá giảm giá hoặc giá gốc --%>
                                <%-- product.getDiscount() bây giờ luôn trả về không null --%>
                                <% if (product.getSalePrice() != null && product.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {%>
                                <span style="color: red;"><%= currencyFormatter.format(product.getSalePrice())%></span>
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
                } else { %>
                <div class="row mt-5">
                    <div class="col-12 text-center" style="padding-bottom: 180px;">
                        <p>No products available.</p>
                    </div>
                </div>
                <% }%>
            </div>
        </div>
    </section>
</main>

<%@include file="/WEB-INF/include/home-footer.jsp" %>