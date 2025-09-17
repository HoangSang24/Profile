/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import shop.db.DBContext;
import shop.model.Checkout;

/**
 *
 * @author ADMIN
 */
public class CheckoutDAO extends DBContext {

    public Checkout getInfoToCheckout(int productid) throws SQLException {
        Checkout temp = null;
        String query = "SELECT \n"
                + "    p.product_id,\n"
                + "    p.name AS product_name,\n"
                + "    p.price,\n"
                + "    c.name AS category_name,\n"
                + "    i.image_url,\n"
                + "    d.sale_price\n"
                + "FROM product p\n"
                + "JOIN category c ON p.category_id = c.category_id\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 image_url \n"
                + "    FROM image \n"
                + "    WHERE product_id = p.product_id \n"
                + "    ORDER BY image_id\n"
                + ") i\n"
                + "OUTER APPLY (\n"
                + "    SELECT TOP 1 sale_price \n"
                + "    FROM discount \n"
                + "    WHERE product_id = p.product_id AND active = 1 \n"
                + "    ORDER BY discount_id DESC\n"
                + ") d\n"
                + "WHERE p.product_id = ?;";
        Object[] params = {productid};
        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            temp = new Checkout(rs.getInt("product_id"), rs.getString("image_url"), rs.getString("product_name"), rs.getString("category_name"), rs.getDouble("price"), rs.getDouble("sale_price"));
        }
        return temp;
    }

    public double getSalePrice(int productid) throws SQLException {
        String query = "SELECT sale_price\n"
                + "FROM discount\n"
                + "WHERE product_id = ?\n"
                + "  AND active = 1\n"
                + "  AND GETDATE() BETWEEN start_date AND end_date;";
        Object[] params = {productid};
        ResultSet rs = execSelectQuery(query, params);

        if (rs.next()) {
            return rs.getDouble("sale_price");
        } else {
            // Không tìm thấy giảm giá → có thể trả giá mặc định là 0 hoặc -1 tùy logic
            return 0.0; // hoặc throw new SQLException("Không có sale_price cho product_id = " + productid);
        }
    }

    public String[] getKeysForProducts(String[] productIds) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT key_code FROM (")
                .append("SELECT p.product_id, gk.key_code, ")
                .append("ROW_NUMBER() OVER (PARTITION BY p.product_id ORDER BY gk.key_code) AS rn ")
                .append("FROM product p ")
                .append("JOIN category c ON p.category_id = c.category_id ")
                .append("JOIN game_details gd ON p.game_details_id = gd.game_details_id ")
                .append("JOIN game_key gk ON gd.game_details_id = gk.game_details_id ")
                .append("WHERE c.category_name = 'game' AND p.product_id IN (");

        Object[] params = new Object[productIds.length];
        for (int i = 0; i < productIds.length; i++) {
            query.append("?");
            if (i < productIds.length - 1) {
                query.append(", ");
            }
            params[i] = Integer.parseInt(productIds[i]);
        }

        query.append(")) AS T WHERE rn = 1");

        ResultSet rs = execSelectQuery(query.toString(), params);

        List<String> keyList = new ArrayList<>();
        while (rs.next()) {
            keyList.add(rs.getString("key_code"));
        }

        return keyList.toArray(new String[0]);

    }

    public int writeOrderIntoDb(int customerId, Integer voucherId, double total, String paymentMethod, String address) throws SQLException {
        int orderId = 0;
        String query = "INSERT INTO [order] (\n"
                + "    customer_id, voucher_id, total_amount, payment_method, \n"
                + "    shipping_address, order_date, status, staff_id\n"
                + ")\n"
                + "VALUES (\n"
                + "    ?, ?, ?, ?, ?, GETDATE(), 'Pending', NULL\n"
                + ");SELECT SCOPE_IDENTITY();";

        Object[] params = {customerId, voucherId, total, paymentMethod, address};

        ResultSet rs = execSelectQuery(query, params);
        while (rs.next()) {
            orderId = rs.getInt(1);
        }
        return orderId;

    }

    public int writeOrderDetails(int orderId, String[] productId, String[] quantity, String[] price) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO order_detail (order_id, product_id, quantity, price) VALUES ");
        Object[] params = new Object[productId.length * 4];
        int index = 0;
        for (int i = 0; i < productId.length; i++) {
            query.append("(?, ?, ?, ?)");
            if (i < productId.length - 1) {
                query.append(", ");
            }
            int proId = Integer.parseInt(productId[i]);
            int quan = Integer.parseInt(quantity[i]);
            double pri = Double.parseDouble(price[i]);
            params[index++] = orderId;
            params[index++] = proId;
            params[index++] = quan;
            params[index++] = pri;

        }

        int b = execQuery(query.toString(), params);
        int a = updateOrderDetailsWithGameKey(orderId, productId);
        return b;

    }

    public int updateOrderDetailsWithGameKey(int orderId, String[] productIds) throws SQLException {
        StringBuilder query = new StringBuilder();
        List<Object> paramList = new ArrayList<>();

        for (String pid : productIds) {
            int productId = Integer.parseInt(pid);

            String keyQuery = "SELECT TOP 1 gk.game_key_id, gk.key_code "
                    + "FROM game_key gk "
                    + "JOIN game_details gd ON gk.game_details_id = gd.game_details_id "
                    + "JOIN product p ON gd.game_details_id = p.game_details_id "
                    + "JOIN category c ON p.category_id = c.category_id "
                    + "WHERE p.product_id = ? AND c.name = 'Game' "
                    + "ORDER BY gk.game_key_id";

            ResultSet rs = execSelectQuery(keyQuery, new Object[]{productId});
            List<Map<String, Object>> keyResults = resultSetToList(rs);
            if (!keyResults.isEmpty()) {
                Map<String, Object> row = keyResults.get(0);
                int gameKeyId = ((Number) row.get("game_key_id")).intValue(); // Ép kiểu an toàn
                String keyCode = (String) row.get("key_code");

                // UPDATE order_detail
                query.append("UPDATE order_detail SET game_key = ? WHERE order_id = ? AND product_id = ?; ");
                paramList.add(keyCode);
                paramList.add(orderId);
                paramList.add(productId);

                // DELETE game_key đã dùng
                query.append("DELETE FROM game_key WHERE game_key_id = ?; ");
                paramList.add(gameKeyId);
            } else {
                System.out.println("⚠ Không tìm thấy key cho product_id: " + productId);
            }
        }

        if (query.length() == 0) {
            return 0;
        }
        System.out.println("Query: " + query.toString());
        System.out.println("Params: " + paramList);
        return execQuery(query.toString(), paramList.toArray());

    }

    public void decreaseQuantity(String[] productIds, String[] quantities) throws SQLException {
        StringBuilder query = new StringBuilder();
        Object[] params = new Object[productIds.length * 2];
        int index = 0;

        for (int i = 0; i < productIds.length; i++) {
            query.append("UPDATE Product SET quantity = quantity - ? WHERE product_id = ?; ");

            int qty = Integer.parseInt(quantities[i]);
            int pid = Integer.parseInt(productIds[i]);

            params[index++] = qty;  // For quantity
            params[index++] = pid;  // For product_id
        }

        execQuery(query.toString(), params);
    }

}
