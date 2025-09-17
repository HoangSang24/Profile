<%--
    Document   : dashboard
    Created on : Jun 20, 2025, 5:55:00 PM
    Author     : HoangSang
--%>

<%@page import="com.google.gson.Gson"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map, java.util.List, shop.model.Product, java.text.NumberFormat, java.util.Locale" %>
<%-- Removed JSTL taglib: <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> --%>

<%
    // --- Data Retrieval from Servlet ---
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    Map<String, Object> summaryStats = (Map<String, Object>) request.getAttribute("summaryStats");
    double totalRevenue = (summaryStats != null) ? (double) summaryStats.get("totalRevenue") : 0;
    int productsSold = (summaryStats != null) ? (int) summaryStats.get("productsSold") : 0;
    Integer totalStock = (Integer) request.getAttribute("totalStock");

    // Retrieve new attributes for Period 2 summary (will be null in "period" mode)
    Double totalRevenuePeriod2 = (Double) request.getAttribute("totalRevenuePeriod2");
    Integer totalProductsSoldPeriod2 = (Integer) request.getAttribute("totalProductsSoldPeriod2");
    // Integer totalCustomersPeriod2 = (Integer) request.getAttribute("totalCustomersPeriod2"); // Not used in this layout anymore

    Integer totalCustomers = (Integer) request.getAttribute("totalCustomers"); // Overall Total Customers

    Double revenueChange = (Double) request.getAttribute("revenueChange");
    Double productsSoldChange = (Double) request.getAttribute("productsSoldChange");
    String comparisonLabel = (String) request.getAttribute("comparisonLabel");
    String currentPeriod = (String) request.getAttribute("currentPeriod");
    List<Product> topSellingProducts = (List<Product>) request.getAttribute("topSellingProducts");
    Map<String, Integer> stockByCategory = (Map<String, Integer>) request.getAttribute("stockByCategory");
    String trendLabelsJson = (String) request.getAttribute("trendLabelsJson");
    String currentTrendDataJson = (String) request.getAttribute("currentTrendDataJson");
    String prevTrendDataJson = (String) request.getAttribute("prevTrendDataJson");
    Integer selectedYear1 = (Integer) request.getAttribute("selectedYear1");
    Integer selectedYear2 = (Integer) request.getAttribute("selectedYear2");
    String comparisonType = (String) request.getParameter("comparisonType"); // Get comparisonType from request
    if (comparisonType == null || comparisonType.isEmpty()) {
        comparisonType = "period"; // Default to 'period' if not specified
    }
%>

<%@include file="/WEB-INF/include/dashboard-header.jsp" %>

<style>
    body {
        width: 84%;
        margin-left: 15%;
        background-color: #161922;
        margin-top: 6%;
    }
    .dashboard-card {
        background: #1f2334;
        border: 1px solid #2b3149;
        border-radius: 12px;
        padding: 25px;
        color: #fff;
        height: 100%;
    }
    .dashboard-card-title {
        font-size: 0.9rem;
        font-weight: 500;
        color: #828ac4;
        text-transform: uppercase;
        margin-bottom: 8px;
    }
    .dashboard-card-value {
        font-size: 2.2rem;
        font-weight: 700;
        color: #fff;
    }
    .dashboard-card-icon {
        font-size: 1.8rem;
        color: #434c7a;
    }
    .chart-wrapper {
        background: #1f2334;
        border: 1px solid #2b3149;
        border-radius: 12px;
        padding: 25px;
        position: relative;
    }
    .chart-title {
        color: #fff;
        font-weight: 600;
    }
    .table-dark-custom img {
        width: 150px;
        height: 200px;
        object-fit: cover;
        border-radius: 8px;
        margin-right: 5%;
    }
    .comparison-badge {
        font-size: 0.85rem;
        font-weight: 600;
        padding: 0.3em 0.6em;
    }
    .text-success {
        color: #28a745 !important;
    }
    .text-danger {
        color: #dc3545 !important;
    }

    .comparison-form {
        background: #1f2334;
        border: 1px solid #2b3149;
        border-radius: 12px;
        padding: 20px 25px;
        margin-bottom: 1.5rem;
    }

    /* CSS TÙY CHỈNH CHO BỘ LỌC */
    .comparison-form .form-label {
        font-size: 0.9rem;
        font-weight: 600;
        color: #aeb9e1;
    }
    .comparison-form .form-select {
        background-color: #2a2f45;
        color: #e0e0e0;
        border-radius: 8px;
        padding: 0.5rem 2.25rem 0.5rem 1rem;
        font-weight: 600;

    }

    .form-select-1{
        width: 90px;
    }
    .btn{
        background-color: #2a2f45;
        color: #e0e0e0;
        border-radius: 8px;
        padding: 5px;
    }

</style>

<div class="container-fluid mt-4">

    <div class="row">
        <div class="col-12">
            <div class="comparison-form">
                <form action="dashboard" method="GET" class="row g-3 align-items-center">

                    <div class="col-md-3">
                        <label for="comparisonType" class="form-label">Comparison Type</label>
                        <select name="comparisonType" id="comparisonType" class="form-select form-select-dark">
                                <%-- Các option của bạn --%>
                                <option value="period" <% if ("period".equals(comparisonType)) {
                                    out.print("selected");
                                } %>>Standard Period</option>
                                    <option value="year" <% if ("year".equals(comparisonType)) {
                                    out.print("selected");
                                } %>>Year vs Year</option>
                                    <option value="month" <% if ("month".equals(comparisonType)) {
                                    out.print("selected");
                                } %>>Month vs Month</option>
                                    <option value="week" <% if ("week".equals(comparisonType)) {
                                    out.print("selected");
                                } %>>Week vs Week</option>
                        </select>
                    </div>

                    <div class="col-md-7">
                        <div class="row g-3" id="comparison-inputs">
                            <div class="col-md-6">
                                <label class="form-label">Period 1</label>
                                <div class="input-group">
                                    <select name="year1" class="form-select form-select-1 form-select-dark year-select"></select>
                                    <select name="month1" class="form-select form-select-1 form-select-dark month-select" style="display:none;"></select>
                                    <select name="week1" class="form-select form-select-1 form-select-dark week-select" style="display:none;"></select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Period 2</label>
                                <div class="input-group">
                                    <select name="year2" class="form-select form-select-1 form-select-dark year-select"></select>
                                    <select name="month2" class="form-select form-select-1 form-select-dark month-select" style="display:none;"></select>
                                    <select name="week2" class="form-select form-select-1 form-select-dark week-select" style="display:none;"></select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-2">
                        <button id="compare-button" type="submit" class="btn btn-primary w-100" style="background-color: #6a5af9; border-color: #6a5af9; border-radius: 7px; padding: 8px;
                                font-weight: 600;">Compare</button>
                    </div>

                </form>
            </div>
        </div>
    </div>
    <div class="row g-4">
        <%-- Card 1: Revenue (Period 1) --%>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">
                            Revenue <% if (currentPeriod != null && !currentPeriod.isEmpty()) {
                                    out.print("(" + currentPeriod + ")");
                                }%>
                        </span>
                        <h3 class="dashboard-card-value"><%= currencyFormatter.format(totalRevenue)%></h3>
                        <% if (revenueChange != null) {%>
                        <span class="comparison-badge <% if (revenueChange >= 0) {
                                out.print("text-success");
                            } else {
                                out.print("text-danger");
                            } %>">
                            <i class="fas fa-arrow-<% if (revenueChange >= 0) {
                                    out.print("up");
                                } else {
                                    out.print("down");
                                }%>"></i>
                            <%= String.format("%.1f%%", Math.abs(revenueChange))%>
                        </span>
                        <small class="comparison-label ms-1"><%= comparisonLabel%></small>
                        <% }%>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-dollar-sign"></i></div>
                </div>
            </div>
        </div>

        <%-- Card 2: Products Sold (Period 1) --%>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">
                            Products Sold <% if (currentPeriod != null && !currentPeriod.isEmpty()) {
                                    out.print("(" + currentPeriod + ")");
                                }%>
                        </span>
                        <h3 class="dashboard-card-value"><%= productsSold%></h3>
                        <% if (productsSoldChange != null) {%>
                        <span class="comparison-badge <% if (productsSoldChange >= 0) {
                                out.print("text-success");
                            } else {
                                out.print("text-danger");
                            } %>">
                            <i class="fas fa-arrow-<% if (productsSoldChange >= 0) {
                                    out.print("up");
                                } else {
                                    out.print("down");
                                }%>"></i>
                            <%= String.format("%.1f%%", Math.abs(productsSoldChange))%>
                        </span>
                        <small class="comparison-label ms-1"><%= comparisonLabel%></small>
                        <% }%>
                    </div>
                    <div class="dashboard-card-icon"><i class="fas fa-shopping-cart"></i></div>
                </div>
            </div>
        </div>

        <%-- Card 3: Products In Stock (Standard Period) OR Revenue (Period 2 - Comparison) --%>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">
                            <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) { %>
                            Products In Stock
                            <% } else { %>
                            Revenue <% if (comparisonLabel != null && !comparisonLabel.isEmpty()) {
                                    out.print("(" + comparisonLabel.replace("vs ", "") + ")");
                                } %>
                            <% } %>
                        </span>
                        <h3 class="dashboard-card-value">
                            <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) {%>
                            <%= totalStock%>
                            <% } else {%>
                            <%= currencyFormatter.format(totalRevenuePeriod2 != null ? totalRevenuePeriod2 : 0)%>
                            <% } %>
                        </h3>
                    </div>
                    <div class="dashboard-card-icon">
                        <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) { %>
                        <i class="fas fa-boxes"></i>
                        <% } else { %>
                        <i class="fas fa-dollar-sign"></i>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <%-- Card 4: Total Customers (Standard Period) OR Products Sold (Period 2 - Comparison) --%>
        <div class="col-lg-3 col-md-6">
            <div class="dashboard-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <span class="dashboard-card-title">
                            <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) { %>
                            Total Customers
                            <% } else { %>
                            Products Sold <% if (comparisonLabel != null && !comparisonLabel.isEmpty()) {
                                    out.print("(" + comparisonLabel.replace("vs ", "") + ")");
                                } %>
                            <% } %>
                        </span>
                        <h3 class="dashboard-card-value">
                            <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) {%>
                            <%= totalCustomers%>
                            <% } else {%>
                            <%= totalProductsSoldPeriod2 != null ? totalProductsSoldPeriod2 : 0%>
                            <% } %>
                        </h3>
                    </div>
                    <div class="dashboard-card-icon">
                        <% if ("period".equals(comparisonType) || comparisonType == null || comparisonType.isEmpty()) { %>
                        <i class="fas fa-users"></i>
                        <% } else { %>
                        <i class="fas fa-shopping-cart"></i>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4 mt-3">
        <div class="col-lg-8">
            <div class="chart-wrapper h-100">
                <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap">
                    <h5 class="chart-title mb-2 mb-md-0">Revenue Trend Comparison</h5>
                    <% if ("period".equals(request.getParameter("comparisonType")) || request.getParameter("comparisonType") == null) { %>
                    <div class="btn-group filter-btn-group" role="group">
                        <a href="dashboard?period=today" class="btn btn-sm <% if ("Today".equals(currentPeriod)) {
                                out.print("active");
                            } %>">Today</a>
                        <a href="dashboard?period=week" class="btn btn-sm <% if ("This Week".equals(currentPeriod)) {
                                out.print("active");
                            } %>">This Week</a>
                        <a href="dashboard?period=month" class="btn btn-sm <% if ("This Month".equals(currentPeriod)) {
                                out.print("active");
                            } %>">This Month</a>
                        <a href="dashboard?period=year" class="btn btn-sm <% if ("This Year".equals(currentPeriod)) {
                                out.print("active");
                            } %>">This Year</a>
                    </div>
                    <% }%>
                </div>
                <div style="height: 350px;">
                    <canvas id="revenueChart"></canvas>
                        <%-- REMOVED: Custom display for Period 2 Revenue Total (yellow text overlay) --%>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="chart-wrapper h-100">

                <span class="dashboard-card-title">Products In Stock</span>
                <h3 class="dashboard-card-value"><%= totalStock%></h3>

                <div class="table-responsive">
                    <table class="table table-dark-custom">
                        <thead><tr><th>Category</th><th class="text-end">Quantity</th></tr></thead>
                        <tbody>
                            <% if (stockByCategory != null && !stockByCategory.isEmpty()) {
                                    for (Map.Entry<String, Integer> entry : stockByCategory.entrySet()) {
                                        int quantity = entry.getValue();%>
                            <tr>
                                <td><%= entry.getKey()%></td>
                                <td class="text-end fw-bold"><%= quantity%></td>
                            </tr>
                            <% }
                            } else { %>
                            <tr><td colspan="3" class="text-center text-muted">No stock data available.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4 mt-3">
        <div class="col-12">
            <div class="chart-wrapper">
                <h5 class="chart-title mb-3">Top 5 Selling Products (All Time)</h5>
                <div class="table-responsive">
                    <table class="table table-dark-custom">
                        <thead><tr><th>Product</th><th class="text-end">Sold</th></tr></thead>
                        <tbody>
                            <% if (topSellingProducts != null && !topSellingProducts.isEmpty()) {
                                    for (Product p : topSellingProducts) {%>
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <img src="<%= request.getContextPath()%>/assets/img/<%= (p.getImageUrls() != null && !p.getImageUrls().isEmpty() ? p.getImageUrls().get(0) : "default-product.png")%>" alt="<%= p.getName()%>" class="me-3">
                                        <span><%= p.getName()%></span>
                                    </div>
                                </td>
                                <td class="text-end fw-bold"><%= p.getQuantity()%></td>
                            </tr>
                            <% }
                            } else { %>
                            <tr><td colspan="2" class="text-center text-muted">No sales data available.</td></tr>
                            <% }%>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>
<script>
// JavaScript để điều khiển Super Filter
    document.addEventListener('DOMContentLoaded', function () {
        const comparisonTypeSelect = document.getElementById('comparisonType');
        const comparisonInputs = document.getElementById('comparison-inputs');

        const yearSelects = document.querySelectorAll('.year-select');
        const monthSelects = document.querySelectorAll('.month-select');
        const weekSelects = document.querySelectorAll('.week-select');

        // Dữ liệu từ JSP để điền vào dropdown
        const availableYears = JSON.parse('<%= new Gson().toJson(request.getAttribute("availableYears"))%>');

        // Use proper null/empty checks for numbers when parsing from String to Int
        const selectedYear1 = parseInt('<%= (request.getAttribute("selectedYear1") != null ? request.getAttribute("selectedYear1").toString() : "")%>') || (availableYears.length > 0 ? availableYears[0] : new Date().getFullYear());
        const selectedYear2 = parseInt('<%= (request.getAttribute("selectedYear2") != null ? request.getAttribute("selectedYear2").toString() : "")%>') || (availableYears.length > 1 ? availableYears[1] : new Date().getFullYear());
        const selectedMonth1 = parseInt('<%= (request.getParameter("month1") != null ? request.getParameter("month1") : "")%>') || (new Date().getMonth() + 1);
        const selectedMonth2 = parseInt('<%= (request.getParameter("month2") != null ? request.getParameter("month2") : "")%>') || (new Date().getMonth() || 12);
        const selectedWeek1 = parseInt('<%= (request.getParameter("week1") != null ? request.getParameter("week1") : "")%>') || 1;
        const selectedWeek2 = parseInt('<%= (request.getParameter("week2") != null ? request.getParameter("week2") : "")%>') || 1;


        function populateSelect(select, start, end, selectedValue) {
            select.innerHTML = '';
            for (let i = start; i <= end; i++) {
                const option = new Option(i, i);
                if (i === selectedValue)
                    option.selected = true;
                select.appendChild(option);
            }
        }

        function populateYearSelect(select, years, selectedValue) {
            select.innerHTML = '';
            if (years.length === 0)
                years.push(new Date().getFullYear());
            years.forEach(year => {
                const option = new Option(year, year);
                if (year === selectedValue)
                    option.selected = true;
                select.appendChild(option);
            });
        }

        populateYearSelect(yearSelects[0], availableYears, selectedYear1);
        populateYearSelect(yearSelects[1], availableYears, selectedYear2);
        populateSelect(monthSelects[0], 1, 12, selectedMonth1);
        populateSelect(monthSelects[1], 1, 12, selectedMonth2);
        populateSelect(weekSelects[0], 1, 4, selectedWeek1);
        populateSelect(weekSelects[1], 1, 4, selectedWeek2);

        function toggleInputs() {
            const type = comparisonTypeSelect.value;
            comparisonInputs.style.display = (type === 'period') ? 'none' : 'flex';
            yearSelects.forEach(s => s.style.display = (type !== 'period') ? 'block' : 'none');
            monthSelects.forEach(s => s.style.display = (type === 'month' || type === 'week') ? 'block' : 'none');
            weekSelects.forEach(s => s.style.display = (type === 'week') ? 'block' : 'none');
        }

        comparisonTypeSelect.addEventListener('change', toggleInputs);
        // Call on load to set initial state based on current param
        toggleInputs();
    });

// Main Chart.js script
    document.addEventListener("DOMContentLoaded", function () {
        Chart.defaults.color = '#8e95a5';
        Chart.defaults.font.family = 'Poppins, sans-serif';

        const trendLabels = JSON.parse('<%= trendLabelsJson != null ? trendLabelsJson : "[]"%>');
        const currentTrendData = JSON.parse('<%= currentTrendDataJson != null ? currentTrendDataJson : "[]"%>');
        const prevTrendData = JSON.parse('<%= prevTrendDataJson != null ? prevTrendDataJson : "[]"%>');

        const comparisonType = '<%= comparisonType%>';

        const datasets = [
            {
                label: '${not empty currentPeriod ? currentPeriod : "Period 1"}',
                data: currentTrendData,
                backgroundColor: 'rgba(75, 192, 192, 0.7)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                borderRadius: 4
            }
        ];

        // Only add the second dataset if comparisonType is not 'period'
        if (comparisonType !== 'period') {
            datasets.push({
                label: '${not empty comparisonLabel ? comparisonLabel.replace("vs ", "") : "Period 2"}',
                data: prevTrendData,
                backgroundColor: 'rgba(255, 159, 64, 0.7)',
                borderColor: 'rgba(255, 159, 64, 1)',
                borderWidth: 1,
                borderRadius: 4
            });
        }

        const revenueCtx = document.getElementById('revenueChart');
        if (revenueCtx && trendLabels.length > 0) {
            new Chart(revenueCtx.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: trendLabels,
                    datasets: datasets // Use the dynamically created datasets array
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    animation: false, // TẮT HIỆU ỨNG CHUYỂN ĐỘNG
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {color: '#2b3149'},
                            ticks: {color: '#828ac4', callback: function (value) {
                                    return '$' + value;
                                }}
                        },
                        x: {
                            grid: {display: false},
                            ticks: {color: '#828ac4'}
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            labels: {color: '#abb9e8'},
                            onClick: null // VÔ HIỆU HÓA CHỨC NĂNG CLICK
                        },
                        tooltip: {
                            enabled: true // Bật ô thông tin (tooltip)
                        },
                        datalabels: {// Cấu hình cho plugin datalabels
                            display: function (context) {
                                // Only display if value is greater than 0, otherwise hide the label
                                return context.dataset.data[context.dataIndex] > 0;
                            },
                            color: '#e0e0e0', // Text color for the labels
                            anchor: 'end', // Anchor at the end of the bar (top of the bar)
                            align: 'top', // Align the label to the top of the anchor (above the bar)
                            offset: 4, // Small positive offset to move it slightly above the bar
                            formatter: function (value, context) {
                                return '$' + value; // Format the value with a dollar sign
                            },
                            font: {
                                size: 10, // Font size for the labels
                                weight: 'bold' // Font weight for the labels
                            }
                        }
                    }
                }
            });
        }
    });
</script>

<%@include file="/WEB-INF/include/dashboard-footer.jsp" %>