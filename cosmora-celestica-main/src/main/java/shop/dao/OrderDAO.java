/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import shop.db.DBContext;
import shop.model.Customer;
import shop.model.Order;
import shop.model.OrderDetails;
import shop.model.Product;

/**
 *
 * @author ADMIN
 */
public class OrderDAO extends DBContext {

    public ArrayList<Order> getallOrder() throws SQLException {
        ArrayList<Order> order = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name\n"
                + "FROM \n"
                + "    [order] o\n"
                + "JOIN \n"
                + "    customer c ON o.customer_id = c.customer_id \n"
                + "ORDER BY o.order_date DESC;";
        ResultSet rs = execSelectQuery(query);
        while (rs.next()) {
            order.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return order;

    }

    public ArrayList<OrderDetails> getOrderDetail(int orderId) throws SQLException {
        ArrayList<OrderDetails> temp = new ArrayList<>();
        String query = "SELECT \n"
                + "    od.*, \n"
                + "    p.name AS product_name\n"
                + "FROM \n"
                + "    Order_Detail od\n"
                + "JOIN \n"
                + "    Product p ON od.product_id = p.product_id\n"
                + "WHERE \n"
                + "    od.order_id = ?;";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getBigDecimal(5), rs.getString(6)));
        }
        return temp;

    }

    public ArrayList<OrderDetails> getOrderDetailForCus(int orderId) throws SQLException {
        ArrayList<OrderDetails> temp = new ArrayList<>();
        String query = "SELECT \n"
                + "    od.product_id,\n"
                + "    od.quantity,\n"
                + "    od.price,\n"
                + "    p.name AS product_name,\n"
                + "    img.image_URL,\n"
                + "    c.name AS category_name,\n"
                + "    od.game_key -- Lấy trực tiếp key từ order_detail\n"
                + "FROM order_detail od\n"
                + "JOIN product p ON od.product_id = p.product_id\n"
                + "\n"
                + "-- Lấy 1 ảnh đại diện cho sản phẩm\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 image_URL\n"
                + "    FROM image\n"
                + "    WHERE image.product_id = p.product_id\n"
                + "    ORDER BY image_id\n"
                + ") AS img\n"
                + "\n"
                + "-- Lấy tên danh mục sản phẩm\n"
                + "LEFT JOIN category c ON p.category_id = c.category_id\n"
                + "\n"
                + "WHERE od.order_id = ?";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            if (rs.getString(6).equalsIgnoreCase("game")) {
                temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
            } else {
                temp.add(new OrderDetails(rs.getInt(1), rs.getInt(2), rs.getBigDecimal(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        }
        return temp;

    }

    public Order getOneOrder(int orderId) throws SQLException {
        Order temp = new Order();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name, \n"
                + "    c.email,\n"
                + "    ISNULL(v.value, 0.00) AS voucher_value\n"
                + "FROM \n"
                + "    [Order] o\n"
                + "JOIN \n"
                + "    Customer c ON o.customer_id = c.customer_id\n"
                + "LEFT JOIN \n"
                + "    Voucher v ON o.voucher_id = v.voucher_id\n"
                + "WHERE \n"
                + "    o.order_id = ?;";
        Object[] params = {orderId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.setOrderId(rs.getInt(1));
            temp.setTotalAmount(rs.getBigDecimal(4));
            temp.setPaymentMethod(rs.getString(5));
            temp.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
            temp.setStatus(rs.getString(8));
            temp.setShippingAddress(rs.getString(6));
            temp.setCustomerName(rs.getString(10));
            temp.setCustomerEmail(rs.getString(11));
            temp.setVoucherValue(rs.getBigDecimal(12));
        }
        return temp;
    }

    public int updateOrderStatus(String status, int orderId) throws SQLException {
        String query = "UPDATE [order]\n"
                + "SET status = ?\n"
                + "WHERE order_id = ?;";
        Object[] params = {status, orderId};
        return execQuery(query, params);
    }

    public ArrayList<Order> searchOrders(String customer_name) throws SQLException {
        ArrayList<Order> temp = new ArrayList<>();
        String query = "SELECT o.*, c.full_name\n"
                + "FROM [Order] o\n"
                + "JOIN Customer c ON o.customer_id = c.customer_id\n"
                + "WHERE c.full_name LIKE ? \n"
                + "ORDER BY o.order_date DESC;";
        Object[] params = {"%" + customer_name + "%"};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return temp;

    }

    public ArrayList<Order> getOrderById(int customerId) throws SQLException {
        ArrayList<Order> order = new ArrayList<>();
        String query = "SELECT \n"
                + "    o.*, \n"
                + "    c.full_name\n"
                + "FROM \n"
                + "    [order] o\n"
                + "JOIN \n"
                + "    customer c ON o.customer_id = c.customer_id \n" // thêm khoảng trắng hoặc xuống dòng
                + "WHERE o.customer_id = ? \n"
                + "ORDER BY o.order_date DESC;";

        Object[] params = {customerId};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            order.add(new Order(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBigDecimal(4), rs.getString(5), rs.getString(6), rs.getObject("order_date", LocalDateTime.class),
                    rs.getString(8), rs.getInt(9), rs.getString(10)));
        }
        return order;

    }

    public int[] getProIdByOrderId(int orderid) throws SQLException {
        List<Integer> tempList = new ArrayList<>();

        String query = "SELECT product_id FROM order_detail WHERE order_id = ?";
        Object[] params = {orderid};
        ResultSet rs = execSelectQuery(query, params);

        while (rs.next()) {
            tempList.add(rs.getInt("product_id"));
        }

        int[] proId = new int[tempList.size()];
        for (int i = 0; i < tempList.size(); i++) {
            proId[i] = tempList.get(i);
        }

        return proId;
    }


//   --- SangNH----
    // sang // sang // sang // 
    private String getDateWhereClause(String dateColumn, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return "";
        }
        return String.format(" WHERE CAST(%s AS DATE) BETWEEN '%s' AND '%s' ",
                dateColumn,
                startDate.toString(),
                endDate.toString());
    }

    // --- DASHBOARD METHODS (DYNAMIC & COMPARATIVE) ---
    public Map<String, Object> getSummaryStats(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", 0.0);
        stats.put("productsSold", 0);

        String whereClause = getDateWhereClause("o.order_date", startDate, endDate);
        if (whereClause.isEmpty()) {
            return stats;
        }

        String revenueSql = "SELECT ISNULL(SUM(total_amount), 0) FROM [order] o " + whereClause;
        String soldSql = "SELECT ISNULL(SUM(od.quantity), 0) FROM order_detail od JOIN [order] o ON od.order_id = o.order_id " + whereClause;

        try {
            try ( PreparedStatement ps = conn.prepareStatement(revenueSql);  ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalRevenue", rs.getDouble(1));
                }
            }
            try ( PreparedStatement ps = conn.prepareStatement(soldSql);  ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("productsSold", rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public Map<String, Object> getRevenueTrend(LocalDate startDate, LocalDate endDate, String period) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        result.put("labels", labels);
        result.put("data", data);

        String whereClause = getDateWhereClause("order_date", startDate, endDate);
        if (whereClause.isEmpty()) {
            return result; // Trả về kết quả rỗng nếu không có ngày tháng
        }

        String sql;

        try {
            switch (period) {
                case "today": // Trend by hour
                    sql = "SELECT DATEPART(hour, order_date) AS trend_key, SUM(total_amount) AS revenue FROM [order]"
                            + whereClause + "GROUP BY DATEPART(hour, order_date) ORDER BY trend_key";
                    Map<Integer, Double> hourlyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            hourlyRevenue.put(rs.getInt("trend_key"), rs.getDouble("revenue"));
                        }
                    }
                    for (int i = 0; i < 24; i++) {
                        labels.add(i + "h");
                        data.add(hourlyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                case "week":          
                    sql = "SELECT (DATEPART(weekday, CAST(order_date AS DATE)) + @@DATEFIRST - 2) % 7 + 1 AS trend_key, "
                            + "SUM(total_amount) AS revenue FROM [order] "
                            + whereClause
                            + "GROUP BY (DATEPART(weekday, CAST(order_date AS DATE)) + @@DATEFIRST - 2) % 7 + 1 "
                            + "ORDER BY trend_key";

                    Map<Integer, Double> dailyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            dailyRevenue.put(rs.getInt("trend_key"), rs.getDouble("revenue"));
                        }
                    }

                    // Mảng tên này vẫn khớp với logic mới (Thứ Hai bắt đầu)
                    String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                    for (int i = 1; i <= 7; i++) {
                        labels.add(dayNames[i - 1]);
                        data.add(dailyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                case "month": // Trend by week of month
                    sql = "SELECT (DATEPART(day, order_date) - 1) / 7 + 1 AS trend_key, SUM(total_amount) AS revenue FROM [order] "
                            + whereClause + "GROUP BY (DATEPART(day, order_date) - 1) / 7 + 1 ORDER BY trend_key";

                    Map<Integer, Double> weeklyRevenue = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            weeklyRevenue.put(rs.getInt("trend_key"), rs.getDouble("revenue"));
                        }
                    }
                    for (int i = 1; i <= 5; i++) { // Một tháng có thể có đến 5 tuần
                        labels.add("Week " + i);
                        data.add(weeklyRevenue.getOrDefault(i, 0.0));
                    }
                    break;

                default: // "year"
                    sql = "SELECT MONTH(order_date) AS trend_key, SUM(total_amount) AS revenue FROM [order]"
                            + whereClause + "GROUP BY MONTH(order_date) ORDER BY trend_key";
                    Map<Integer, Double> monthlyRevenueForYear = new HashMap<>();
                    try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            monthlyRevenueForYear.put(rs.getInt("trend_key"), rs.getDouble("revenue"));
                        }
                    }
                    String[] monthNamesArrForYear = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                    for (int i = 1; i <= 12; i++) {
                        labels.add(monthNamesArrForYear[i - 1]);
                        data.add(monthlyRevenueForYear.getOrDefault(i, 0.0));
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // --- DASHBOARD METHODS (FIXED / NON-COMPARATIVE) ---
    public int getTotalProductsInStock() {
        String sql = "SELECT SUM(quantity) AS total_stock FROM product";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalCustomers() {
        String sql = "SELECT COUNT(customer_id) AS total_customers FROM customer";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_customers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalCustomers(LocalDate startDate, LocalDate endDate) {
        String whereClause = getDateWhereClause("order_date", startDate, endDate);
        String sql = "SELECT COUNT(DISTINCT customer_id) AS total_customers FROM [order] " + whereClause;
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total_customers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if no customers or error
    }

    public List<Product> getTopSellingProductsDetails(int limit) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT TOP (?) p.product_id, p.name, p.price, SUM(od.quantity) AS total_sold, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM order_detail od "
                + "JOIN product p ON od.product_id = p.product_id "
                + "GROUP BY p.product_id, p.name, p.price "
                + "ORDER BY total_sold DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setQuantity(rs.getInt("total_sold"));

                    List<String> imageUrls = new ArrayList<>();
                    String imageUrl = rs.getString("image_url");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        imageUrls.add(imageUrl);
                    }
                    p.setImageUrls(imageUrls);

                    productList.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Map<String, Integer> getStockByCategory() {
        Map<String, Integer> stockMap = new LinkedHashMap<>();
        String sql = "SELECT c.name, SUM(p.quantity) AS total_stock "
                + "FROM product p JOIN category c ON p.category_id = c.category_id "
                + "GROUP BY c.name HAVING SUM(p.quantity) > 0 ORDER BY total_stock DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String categoryName = rs.getString("name");
                int totalStock = rs.getInt("total_stock");
                stockMap.put(categoryName, totalStock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stockMap;
    }

    public List<Integer> getDistinctOrderYears() {
        List<Integer> years = new ArrayList<>();
        String sql = "SELECT DISTINCT YEAR(order_date) AS order_year FROM [order] ORDER BY order_year DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                years.add(rs.getInt("order_year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }
}
