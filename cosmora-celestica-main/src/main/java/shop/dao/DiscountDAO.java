/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.db.DBContext;
import shop.model.Discount;
import shop.model.Product;
import shop.model.Voucher;

/**
 *
 * @author PC
 */
public class DiscountDAO extends DBContext {

    public ArrayList<Discount> searchDiscountByProductName(String keyword) {
        ArrayList<Discount> discounts = new ArrayList<>();

        String query = "SELECT d.*, p.product_id, p.name AS product_name, p.price AS product_price "
                + "FROM discount d "
                + "JOIN product p ON d.product_id = p.product_id "
                + "WHERE LOWER(p.name) LIKE LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Discount d = new Discount(rs.getInt("discount_id"), rs.getInt("active"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getBigDecimal("sale_price"), new Product(rs.getInt("product_id"), rs.getString("product_name"), rs.getBigDecimal("product_price")), rs.getString("description"));
                discounts.add(d);
            }
        } catch (SQLException e) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return discounts;
    }

    public int createDiscount(Discount discount) {
        try {
            String query = "INSERT INTO discount (product_id, active, start_date, end_date, sale_price, description) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            Object[] params = {
                discount.getProduct().getProductId(),
                discount.getActive(),
                discount.getStarDate(),
                discount.getEndaDate(),
                discount.getSale_price(),
                discount.getDescription()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public ArrayList<Discount> getListDicounts() {
        ArrayList<Discount> discounts = new ArrayList<>();
        try {
            String query = "SELECT "
                    + "d.discount_id, "
                    + "d.product_id, "
                    + "d.active, "
                    + "d.start_date, "
                    + "d.end_date, "
                    + "d.sale_price, "
                    + "d.description, "
                    + "p.name AS product_name, "
                    + "p.price AS product_price "
                    + "FROM discount d "
                    + "JOIN product p ON d.product_id = p.product_id";

            ResultSet rs = execSelectQuery(query);
            while (rs.next()) {
                Discount discount = new Discount(rs.getInt("discount_id"), rs.getInt("active"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getBigDecimal("sale_price"), new Product(rs.getInt("product_id"), rs.getString("product_name"), rs.getBigDecimal("product_price")), rs.getString("description"));
                discounts.add(discount);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return discounts;
    }

    public Discount getOne(int id) {
        try {
            String query = "SELECT "
                    + "d.discount_id, "
                    + "d.product_id, "
                    + "d.active, "
                    + "d.start_date, "
                    + "d.end_date, "
                    + "d.description,"
                    + "d.sale_price, "
                    + "p.name AS product_name, "
                    + "p.price AS product_price "
                    + "FROM discount d "
                    + "JOIN product p ON d.product_id = p.product_id "
                    + "WHERE d.discount_id = ?";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Discount(rs.getInt("discount_id"), rs.getInt("active"), rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(), rs.getBigDecimal("sale_price"), new Product(rs.getInt("product_id"), rs.getString("product_name"), rs.getBigDecimal("product_price")), rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int editDiscount(Discount discount) {
        try {
            String query = "UPDATE discount SET sale_price = ?, start_date= ? ,end_date = ?, description= ?  WHERE discount_id = ?;";
            Object[] params = {
                discount.getSale_price(),
                discount.getStarDate(),
                discount.getEndaDate(),
                discount.getDescription(),
                discount.getDiscount_id()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int editActiveForDiscount(Discount code) {
        try {
            String query
                    = "UPDATE discount\n"
                    + "SET active = ?"
                    + "WHERE discount_id = ?;";
            Object[] params = {code.getActive(), code.getDiscount_id()};
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int checkIfProductHasAnotherActiveDiscount(int productId, int discountId) {
        try {
            String sql = "SELECT COUNT(*) AS total FROM discount WHERE product_id = ? AND active = 1 AND discount_id != ?";
            Object[] params = {productId, discountId};
            ResultSet rs = execSelectQuery(sql, params);
            if (rs.next() && rs.getInt("total") > 0) {
                return 1; // Có discount khác đã bật
            }
        } catch (SQLException ex) {
            Logger.getLogger(DiscountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0; // Không có cái nào active
    }

    public int deleteById(int discount_id) throws SQLException {

        String query = "DELETE FROM discount\n"
                + "WHERE discount_id = ?;";
        Object [] params = {discount_id};
        return execQuery(query, params);
    
    }
}
