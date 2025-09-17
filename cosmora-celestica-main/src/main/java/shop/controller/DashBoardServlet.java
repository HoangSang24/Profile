package shop.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import shop.dao.OrderDAO;
import shop.model.Order;
import shop.model.Product;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

@WebServlet(name="DashBoardServlet", urlPatterns={"/dashboard"})
public class DashBoardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DashBoardServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DashBoardServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }  

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderDAO dao = new OrderDAO();
        Gson gson = new Gson();

        List<Integer> availableYears = dao.getDistinctOrderYears();
        request.setAttribute("availableYears", availableYears);

        String comparisonType = request.getParameter("comparisonType");
        if (comparisonType == null || comparisonType.isEmpty()) {
            comparisonType = "period"; 
        }
        request.setAttribute("comparisonType", comparisonType); 

        // Initial set of fixed values (these will be updated for comparison scenarios)
        request.setAttribute("totalStock", dao.getTotalProductsInStock());
        request.setAttribute("totalCustomers", dao.getTotalCustomers());
        request.setAttribute("stockByCategory", dao.getStockByCategory());
        request.setAttribute("topSellingProducts", dao.getTopSellingProductsDetails(5));

        // Initialize Period 2 specific attributes to null
        request.setAttribute("totalRevenuePeriod2", null); 
        request.setAttribute("totalProductsSoldPeriod2", null);
        request.setAttribute("totalCustomersPeriod2", null);

        if (comparisonType.equals("period")) {
            String period = request.getParameter("period");
            if (period == null || period.trim().isEmpty()) {
                period = "year"; 
            }

            LocalDate today = LocalDate.now();
            LocalDate currentStartDate, currentEndDate; 
            String currentPeriodLabel; 

            // These are for Period 1. comparisonLabel etc. for comparison logic
            request.setAttribute("revenueChange", null);
            request.setAttribute("productsSoldChange", null);
            request.setAttribute("comparisonLabel", null);
            request.setAttribute("prevTrendDataJson", gson.toJson(new ArrayList<>())); 

            switch (period) {
                case "today":
                    currentStartDate = today; 
                    currentEndDate = today;
                    currentPeriodLabel = "Today"; 
                    request.setAttribute("currentTrendDataJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "today").get("data")));
                    request.setAttribute("trendLabelsJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "today").get("labels")));
                    break;
                case "week":
                    currentStartDate = today.with(DayOfWeek.MONDAY); 
                    currentEndDate = today.with(DayOfWeek.SUNDAY); 
                    currentPeriodLabel = "This Week"; 
                    request.setAttribute("currentTrendDataJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "week").get("data")));
                    request.setAttribute("trendLabelsJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "week").get("labels")));
                    break;
                case "month":
                    currentStartDate = today.withDayOfMonth(1); 
                    currentEndDate = today.with(TemporalAdjusters.lastDayOfMonth()); 
                    currentPeriodLabel = "This Month"; 
                    request.setAttribute("currentTrendDataJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "month").get("data")));
                    request.setAttribute("trendLabelsJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "month").get("labels")));
                    break;
                default: // "year"
                    currentStartDate = today.withDayOfYear(1); 
                    currentEndDate = today.with(TemporalAdjusters.lastDayOfYear()); 
                    currentPeriodLabel = "This Year"; 
                    request.setAttribute("currentTrendDataJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "year").get("data")));
                    request.setAttribute("trendLabelsJson", gson.toJson(dao.getRevenueTrend(currentStartDate, currentEndDate, "year").get("labels")));
                    break;
            }
            
            Map<String, Object> currentSummaryStats = dao.getSummaryStats(currentStartDate, currentEndDate);
            request.setAttribute("summaryStats", currentSummaryStats);
            request.setAttribute("currentPeriod", currentPeriodLabel);
            
        } else {
            // --- COMPARISON SCENARIO ---
            LocalDate startDate1, endDate1, startDate2, endDate2;
            String label1 = "", label2 = "";
            String trendPeriodForChart; 

            try {
                int year1 = Integer.parseInt(request.getParameter("year1"));
                int year2 = Integer.parseInt(request.getParameter("year2"));
                request.setAttribute("selectedYear1", year1);
                request.setAttribute("selectedYear2", year2);

                switch (comparisonType) {
                    case "year":
                        startDate1 = LocalDate.of(year1, 1, 1);
                        endDate1 = LocalDate.of(year1, 12, 31);
                        startDate2 = LocalDate.of(year2, 1, 1);
                        endDate2 = LocalDate.of(year2, 12, 31);
                        label1 = String.valueOf(year1);
                        label2 = String.valueOf(year2);
                        trendPeriodForChart = "year"; 
                        break;
                    case "month":
                        int month1 = Integer.parseInt(request.getParameter("month1"));
                        int month2 = Integer.parseInt(request.getParameter("month2"));
                        startDate1 = LocalDate.of(year1, month1, 1);
                        endDate1 = startDate1.with(TemporalAdjusters.lastDayOfMonth());
                        startDate2 = LocalDate.of(year2, month2, 1);
                        endDate2 = startDate2.with(TemporalAdjusters.lastDayOfMonth());
                        label1 = String.format("%d/%d", month1, year1);
                        label2 = String.format("%d/%d", month2, year2);
                        trendPeriodForChart = "month"; 
                        break;
                    case "week":
                        // ### CORRECTED LOGIC FOR WEEK CALCULATION ###
                        int w_year1 = Integer.parseInt(request.getParameter("year1"));
                        int w_month1 = Integer.parseInt(request.getParameter("month1"));
                        int w_week1 = Integer.parseInt(request.getParameter("week1"));
                        int w_year2 = Integer.parseInt(request.getParameter("year2"));
                        int w_month2 = Integer.parseInt(request.getParameter("month2"));
                        int w_week2 = Integer.parseInt(request.getParameter("week2"));

                        startDate1 = LocalDate.of(w_year1, w_month1, 1).plusWeeks(w_week1 - 1);
                        endDate1 = startDate1.plusDays(6);

                        startDate2 = LocalDate.of(w_year2, w_month2, 1).plusWeeks(w_week2 - 1);
                        endDate2 = startDate2.plusDays(6);

                        label1 = String.format("W%d-%d/%d", w_week1, w_month1, w_year1);
                        label2 = String.format("W%d-%d/%d", w_week2, w_month2, w_year2);
                        trendPeriodForChart = "week";
                        break;
                    default:
                          response.sendRedirect("dashboard?comparisonType=period");
                          return; 
                }
                
                // Get summary stats for both periods
                Map<String, Object> stats1 = dao.getSummaryStats(startDate1, endDate1);
                Map<String, Object> stats2 = dao.getSummaryStats(startDate2, endDate2); 

                double revenue1 = (double) stats1.get("totalRevenue");
                double revenue2 = (double) stats2.get("totalRevenue");
                // ### CORRECTED FORMULA TO PREVENT DIVISION BY ZERO ###
                double revenueChange = (revenue2 != 0) ? ((revenue1 - revenue2) / revenue2) * 100 : (revenue1 > 0 ? 100.0 : 0.0);

                int sold1 = (int) stats1.get("productsSold");
                int sold2 = (int) stats2.get("productsSold");
                // ### CORRECTED FORMULA TO PREVENT DIVISION BY ZERO ###
                double soldChange = (sold2 != 0) ? (((double) sold1 - sold2) / sold2) * 100 : (sold1 > 0 ? 100.0 : 0.0);
                
                int totalCustomersPeriod2 = dao.getTotalCustomers(startDate2, endDate2);
                
                Map<String, Object> trend1 = dao.getRevenueTrend(startDate1, endDate1, trendPeriodForChart);
                Map<String, Object> trend2 = dao.getRevenueTrend(startDate2, endDate2, trendPeriodForChart);

                // Set attributes for JSP
                request.setAttribute("summaryStats", stats1); 
                request.setAttribute("revenueChange", revenueChange);
                request.setAttribute("productsSoldChange", soldChange);
                request.setAttribute("comparisonLabel", "vs " + label2); 
                request.setAttribute("currentPeriod", label1); 

                request.setAttribute("totalRevenuePeriod2", revenue2);
                request.setAttribute("totalProductsSoldPeriod2", sold2);
                request.setAttribute("totalCustomersPeriod2", totalCustomersPeriod2);


                // Align chart data
                List<String> labels1 = (List<String>) trend1.get("labels");
                List<Double> data1 = (List<Double>) trend1.get("data");
                List<String> labels2 = (List<String>) trend2.get("labels");
                List<Double> data2 = (List<Double>) trend2.get("data");

                Map<String, Double> mapData1 = new HashMap<>();
                for (int i = 0; i < labels1.size(); i++) {
                    mapData1.put(labels1.get(i), data1.get(i));
                }
                Map<String, Double> mapData2 = new HashMap<>();
                for (int i = 0; i < labels2.size(); i++) {
                    mapData2.put(labels2.get(i), data2.get(i));
                }

                List<String> combinedLabels = new ArrayList<>(labels1);
                for (String label : labels2) {
                    if (!combinedLabels.contains(label)) {
                        combinedLabels.add(label);
                    }
                }
                
                // Sorting logic for labels (can be expanded if needed)
                if (trendPeriodForChart.equals("year")) { 
                    final List<String> MONTH_ORDER = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
                    Collections.sort(combinedLabels, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return MONTH_ORDER.indexOf(s1) - MONTH_ORDER.indexOf(s2);
                        }
                    });
                } else if (trendPeriodForChart.equals("week")) { 
                    final List<String> DAY_ORDER = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
                    Collections.sort(combinedLabels, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return DAY_ORDER.indexOf(s1) - DAY_ORDER.indexOf(s2);
                        }
                    });
                }
                
                List<Double> alignedData1 = new ArrayList<>();
                List<Double> alignedData2 = new ArrayList<>();

                for (String label : combinedLabels) {
                    alignedData1.add(mapData1.getOrDefault(label, 0.0));
                    alignedData2.add(mapData2.getOrDefault(label, 0.0));
                }

                request.setAttribute("trendLabelsJson", gson.toJson(combinedLabels));
                request.setAttribute("currentTrendDataJson", gson.toJson(alignedData1));
                request.setAttribute("prevTrendDataJson", gson.toJson(alignedData2));

            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("dashboard?comparisonType=period");
                return;
            }
        }
        
        request.getRequestDispatcher("/WEB-INF/dashboard/dashboard-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "DashBoard Servlet for managing statistics and trends.";
    }
}