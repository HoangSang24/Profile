/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.db.DBContext;
import shop.model.Discount;
import shop.model.GameDetails;
import shop.model.Product;
import shop.model.ProductAttribute;

/**
 *
 * @author HoangSang
 */
public class ProductDAO extends DBContext {

    public ArrayList<Product> getProductsByCategoryAndBrand(String categoryName, String brandName) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, "
                + "p.name, "
                + "p.price, "
                + "p.quantity, "
                + "b.brand_name, "
                + "(SELECT TOP 1 i.image_url FROM image i WHERE i.product_id = p.product_id) AS image_url "
                + "FROM product p "
                + "JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE LOWER(c.name) = LOWER(?) AND LOWER(b.brand_name) = LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, categoryName.toLowerCase());
            stmt.setString(2, brandName.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrandName(rs.getString("brand_name"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);

                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public ArrayList<Product> getProductsByStoreOS(String storeOSName) {
        ArrayList<Product> productList = new ArrayList<>();

        String sql = "SELECT "
                + "p.product_id, "
                + "p.name, "
                + "p.price, "
                + "d.sale_price, "
                + "p.quantity, "
                + "p.active_product, "
                + "c.name AS category_name, "
                + "b.brand_name, "
                + "( "
                + "    SELECT TOP 1 i.image_URL "
                + "    FROM image i "
                + "    WHERE i.product_id = p.product_id "
                + "    ORDER BY i.image_id "
                + ") AS image_url "
                + "FROM product p "
                + "JOIN store_platform sp ON p.game_details_id = sp.game_details_id "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE sp.store_OS_name = ? "
                + "ORDER BY p.product_id DESC";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, storeOSName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryName(rs.getString("category_name"));
                product.setBrandName(rs.getString("brand_name"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setActiveProduct(rs.getInt("active_product"));

                String imageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    imageUrls.add(imageUrl);
                }
                product.setImageUrls(imageUrls);

                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log lỗi
        }

        return productList;
    }

    public List<String> getDistinctStoreOSNames() {
        List<String> osNames = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT store_OS_name FROM store_platform;";
            Object[] params = {}; // Không có tham số
            ResultSet rs = execSelectQuery(query, params);
            while (rs.next()) {
                osNames.add(rs.getString("store_OS_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StorePlatformDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return osNames;
    }

    public List<String> getDistinctBrandNames() {
        List<String> brandNames = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT brand_name FROM brand;";
            Object[] params = {}; // Không có tham số truyền vào
            ResultSet rs = execSelectQuery(query, params);
            while (rs.next()) {
                brandNames.add(rs.getString("brand_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BrandDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return brandNames;
    }

    public ArrayList<Product> getProductsByCategory(String categoryName) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, "
                + "p.name, "
                + "p.price, "
                + "p.quantity, "
                + "d.sale_price, d.active AS discount_active, "
                + "b.brand_name, "
                + "(SELECT TOP 1 i.image_url FROM image i WHERE i.product_id = p.product_id) AS image_url "
                + "FROM product p "
                + "JOIN category c ON p.category_id = c.category_id " + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "WHERE LOWER(c.name) = LOWER(?)";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, categoryName.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrandName(rs.getString("brand_name"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);

                products.add(product);

            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public ArrayList<Product> searchProductByName(String keyword) {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT p.product_id, "
                + "p.name, "
                + "p.price, "
                + "p.quantity, "
                + "b.brand_name, "
                + "d.sale_price, d.active AS discount_active, "
                + "(SELECT TOP 1 i.image_url FROM image i WHERE i.product_id = p.product_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "WHERE LOWER(p.name) LIKE LOWER(?) AND p.active_product = 1";

        try ( PreparedStatement stmt = this.getConnection().prepareStatement(query)) {
            stmt.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setBrandName(rs.getString("brand_name"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);
                products.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(VouchersDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return products;
    }

    public int editSalePriceAndActive(Product product) {
        try {
            String query = "UPDATE product SET sale_price = ?, active = ?, updated_at = GETDATE() WHERE product_id = ?;";
            Object[] params = {
                product.getSalePrice(),
                product.getActive(),
                product.getProductId()
            };
            return execQuery(query, params);

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Product getOneDiscount(int id) {
        try {
            String query = "select * from product where product_id =?;";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);

            if (rs.next()) {
                return new Product(rs.getString("name"), rs.getBigDecimal("price"), rs.getBigDecimal("sale_price"), rs.getInt("active"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Product> getListDicounts() {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String query = "SELECT "
                    + "p.product_id, "
                    + "p.name, "
                    + "p.price, "
                    + "d.sale_price, "
                    + "p.quantity, "
                    + "p.active_product, "
                    + "c.name AS category_name, "
                    + "b.brand_name, "
                    + "( "
                    + "    SELECT TOP 1 i.image_URL "
                    + "    FROM image i "
                    + "    WHERE i.product_id = p.product_id "
                    + "    ORDER BY i.image_id "
                    + ") AS image_url "
                    + "FROM product p "
                    + "LEFT JOIN discount d ON p.product_id = d.product_id "
                    + // không lọc theo d.active
                    "LEFT JOIN category c ON p.category_id = c.category_id "
                    + "LEFT JOIN brand b ON p.brand_id = b.brand_id ";

            ResultSet rs = execSelectQuery(query);
            while (rs.next()) {
                Product product = new Product(rs.getInt("product_id"), rs.getString("name"), rs.getBigDecimal("price"), new Discount(rs.getInt("active"), rs.getBigDecimal("sale_price")));
                products.add(product);

            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    public void addFullGameProduct(Product product, GameDetails gameDetails, List<String> imageUrls, String[] platformIds, String[] osIds, String[] newKeys, List<ProductAttribute> attributes) throws SQLException {
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);
            int gameDetailsId;
            String sqlGameDetails = "INSERT INTO game_details (developer, genre, release_date) VALUES (?, ?, ?)";
            try ( PreparedStatement ps = conn.prepareStatement(sqlGameDetails, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameDetails.getDeveloper());
                ps.setString(2, gameDetails.getGenre());
                ps.setDate(3, gameDetails.getReleaseDate());
                ps.executeUpdate();
                try ( ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        gameDetailsId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Tạo chi tiết game thất bại, không nhận được ID.");
                    }
                }
            }
            product.setGameDetailsId(gameDetailsId);

            addProductAndImages(conn, product, imageUrls);

            if (platformIds != null && platformIds.length > 0) {
                StorePlatformDAO platformDao = new StorePlatformDAO();
                String sqlPlatform = "INSERT INTO store_platform (game_details_id, store_OS_name) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlPlatform)) {
                    for (String pIdStr : platformIds) {
                        int pId = Integer.parseInt(pIdStr);
                        String platformName = platformDao.getStorePlatformNameById(pId);
                        ps.setInt(1, gameDetailsId);
                        ps.setString(2, platformName);
                        ps.addBatch();

                    }
                    ps.executeBatch();
                }
            }

            if (osIds != null && osIds.length > 0) {
                OperatingSystemDAO osDao = new OperatingSystemDAO();
                String sqlOs = "INSERT INTO operating_system (game_details_id, os_name) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlOs)) {
                    for (String oIdStr : osIds) {
                        int oId = Integer.parseInt(oIdStr);
                        String osName = osDao.getOsNameById(oId);

                        ps.setInt(1, gameDetailsId);
                        ps.setString(2, osName);
                        ps.addBatch();

                    }
                    ps.executeBatch();
                }
            }

            if (newKeys != null && newKeys.length > 0) {
                String sqlKey = "INSERT INTO game_key (game_details_id, key_code) VALUES (?, ?)";
                try ( PreparedStatement ps = conn.prepareStatement(sqlKey)) {
                    for (String key : newKeys) {
                        if (key != null && !key.trim().isEmpty()) {
                            ps.setInt(1, gameDetailsId);
                            ps.setString(2, key.trim());
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                }
            }

            insertOrUpdateProductAttributes(conn, product.getProductId(), attributes);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public boolean isProductSold(int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_detail WHERE product_id = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void deleteProduct(int productId) throws SQLException {
        Integer gameDetailsIdToDelete = null;
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            String getGameDetailsIdSql = "SELECT game_details_id FROM product WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(getGameDetailsIdSql)) {
                ps.setInt(1, productId);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        gameDetailsIdToDelete = (Integer) rs.getObject("game_details_id");
                    }
                }
            }

            // Xóa các thuộc tính của sản phẩm
            String deleteAttributesSQL = "DELETE FROM product_attribute WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteAttributesSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // Xóa hình ảnh của sản phẩm
            String deleteImagesSQL = "DELETE FROM image WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteImagesSQL)) {
                ps.setInt(1, productId);
                ps.executeUpdate();
            }

            // Xóa sản phẩm chính
            String deleteProductSQL = "DELETE FROM product WHERE product_id = ?";
            try ( PreparedStatement ps = conn.prepareStatement(deleteProductSQL)) {
                ps.setInt(1, productId);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Delete defective product, no rows affected for product_id: " + productId);
                }
            }

            // Nếu sản phẩm là game và không còn sản phẩm nào khác dùng chung game_details_id này
            if (gameDetailsIdToDelete != null) {
                String checkRefsSql = "SELECT COUNT(*) FROM product WHERE game_details_id = ?";
                int refCount = 0;
                try ( PreparedStatement ps = conn.prepareStatement(checkRefsSql)) {
                    ps.setInt(1, gameDetailsIdToDelete);
                    try ( ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            refCount = rs.getInt(1);
                        }
                    }
                }

                if (refCount == 0) { // Nếu không còn sản phẩm nào tham chiếu đến game_details này
                    // Xóa các khóa game
                    String deleteKeysSql = "DELETE FROM game_key WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteKeysSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    // Xóa các nền tảng
                    String deletePlatformSql = "DELETE FROM store_platform WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deletePlatformSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    // Xóa các hệ điều hành
                    String deleteOsSql = "DELETE FROM operating_system WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteOsSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                    // Cuối cùng, xóa chi tiết game
                    String deleteGameDetailsSql = "DELETE FROM game_details WHERE game_details_id = ?";
                    try ( PreparedStatement ps = conn.prepareStatement(deleteGameDetailsSql)) {
                        ps.setInt(1, gameDetailsIdToDelete);
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback nếu có lỗi
            }
            throw e; // Ném lại ngoại lệ
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Đặt lại auto-commit
                conn.close();
            }
        }
    }

    public List<Product> getAllProductsOfDiscount() {
        List<Product> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM product";
            ResultSet rs = execSelectQuery(sql);
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getBigDecimal("price"));
                // ... fill các trường khác nếu cần
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        String sql = "SELECT \n"
                + "    p.product_id,\n"
                + "    p.name,\n"
                + "    p.price,\n"
                + "    d.sale_price,\n"
                + "    d.active,\n"
                + "    p.quantity,\n"
                + "    p.active_product,\n"
                + "    c.name AS category_name,\n"
                + "    (\n"
                + "        SELECT TOP 1 i.image_URL\n"
                + "        FROM image i\n"
                + "        WHERE i.product_id = p.product_id\n"
                + "        ORDER BY i.image_id\n"
                + "    ) AS image_url\n"
                + "FROM product p\n"
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "LEFT JOIN category c ON p.category_id = c.category_id\n"
                + "-- WHERE p.active_product = 1 -- (tuỳ chọn, bật nếu chỉ muốn sản phẩm đang hoạt động)\n"
                + "ORDER BY p.product_id DESC;";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryName(rs.getString("category_name"));
                product.setSalePrice(rs.getBigDecimal("sale_price"));
                product.setActiveProduct(rs.getInt("active_product"));
                product.setActive(rs.getInt("active"));

                String singleImageUrl = rs.getString("image_url");
                List<String> imageUrls = new ArrayList<>();
                if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                    imageUrls.add(singleImageUrl);
                }
                product.setImageUrls(imageUrls);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    private void addProductAndImages(Connection conn, Product product, List<String> imageUrls) throws SQLException {

        String sqlProduct = "INSERT INTO product (name, description, price, quantity, category_id, brand_id, game_details_id, active_product, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";

        try ( PreparedStatement psProduct = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
            psProduct.setString(1, product.getName());
            psProduct.setString(2, product.getDescription());
            psProduct.setBigDecimal(3, product.getPrice());
            psProduct.setInt(4, product.getQuantity());
            psProduct.setInt(5, product.getCategoryId());

            if (product.getBrandId() != null) {
                psProduct.setInt(6, product.getBrandId());
            } else {
                psProduct.setNull(6, Types.INTEGER);
            }
            if (product.getGameDetailsId() != null) {
                psProduct.setInt(7, product.getGameDetailsId());
            } else {
                psProduct.setNull(7, Types.INTEGER);
            }
            psProduct.setInt(8, product.getActiveProduct());

            psProduct.executeUpdate();

            try ( ResultSet rs = psProduct.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                } else {
                    throw new SQLException("Product creation failed, ID not received.");
                }
            }
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            String sqlImage = "INSERT INTO image (product_id, image_URL) VALUES (?, ?)";
            try ( PreparedStatement psImage = conn.prepareStatement(sqlImage)) {
                for (String imageUrl : imageUrls) {
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        psImage.setInt(1, product.getProductId());
                        psImage.setString(2, imageUrl);
                        psImage.addBatch();
                    }
                }
                psImage.executeBatch();
            }
        }
    }

    public void addAccessoryProduct(Product product, List<ProductAttribute> attributes, List<String> imageUrls) throws SQLException {
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            addProductAndImages(conn, product, imageUrls);

            insertOrUpdateProductAttributes(conn, product.getProductId(), attributes);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void updateProduct(Product product, GameDetails gameDetails, List<ProductAttribute> attributes,
            List<String> newImageUrls, String[] platformIds, String[] osIds, String[] newKeys) throws SQLException {
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            String sqlUpdateProduct = "UPDATE product SET name=?, description=?, price=?, quantity=?, category_id=?, brand_id=?, game_details_id=?, active_product=?, updated_at=GETDATE() WHERE product_id=?";
            try ( PreparedStatement ps = conn.prepareStatement(sqlUpdateProduct)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setInt(4, product.getQuantity());
                ps.setInt(5, product.getCategoryId());
                if (product.getBrandId() != null) {
                    ps.setInt(6, product.getBrandId());
                } else {
                    ps.setNull(6, Types.INTEGER);
                }
                if (product.getGameDetailsId() != null) {
                    ps.setInt(7, product.getGameDetailsId());
                } else {
                    ps.setNull(7, Types.INTEGER);
                }
                ps.setInt(8, product.getActiveProduct());
                ps.setInt(9, product.getProductId());
                ps.executeUpdate();
            }

            // Xử lý GameDetails nếu có
            if (gameDetails != null && gameDetails.getGameDetailsId() > 0) {
                String sqlUpdateGame = "UPDATE game_details SET developer=?, genre=?, release_date=? WHERE game_details_id=?";
                try ( PreparedStatement ps = conn.prepareStatement(sqlUpdateGame)) {
                    ps.setString(1, gameDetails.getDeveloper());
                    ps.setString(2, gameDetails.getGenre());
                    ps.setDate(3, gameDetails.getReleaseDate());
                    ps.setInt(4, gameDetails.getGameDetailsId());
                    ps.executeUpdate();
                }

                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM store_platform WHERE game_details_id = ?")) {
                    ps.setInt(1, gameDetails.getGameDetailsId());
                    ps.executeUpdate();
                }
                if (platformIds != null && platformIds.length > 0) {
                    String sqlPlatform = "INSERT INTO store_platform (game_details_id, store_OS_name) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlPlatform)) {
                        StorePlatformDAO platformDao = new StorePlatformDAO();
                        for (String pIdStr : platformIds) {
                            int pId = Integer.parseInt(pIdStr);
                            String platformName = platformDao.getStorePlatformNameById(pId);
                            if (platformName != null) {
                                ps.setInt(1, gameDetails.getGameDetailsId());
                                ps.setString(2, platformName);
                                ps.addBatch();
                            } else {

                            }
                        }
                        ps.executeBatch();
                    }
                }

                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM operating_system WHERE game_details_id = ?")) {
                    ps.setInt(1, gameDetails.getGameDetailsId());
                    ps.executeUpdate();
                }
                if (osIds != null && osIds.length > 0) {
                    String sqlOs = "INSERT INTO operating_system (game_details_id, os_name) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlOs)) {
                        OperatingSystemDAO osDao = new OperatingSystemDAO();
                        for (String oIdStr : osIds) {
                            int oId = Integer.parseInt(oIdStr);
                            String osName = osDao.getOsNameById(oId);
                            if (osName != null) {
                                ps.setInt(1, gameDetails.getGameDetailsId());
                                ps.setString(2, osName);
                                ps.addBatch();
                            } else {

                            }
                        }
                        ps.executeBatch();
                    }
                }

                if (newKeys != null && newKeys.length > 0) {
                    String sqlKey = "INSERT INTO game_key (game_details_id, key_code) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlKey)) {
                        for (String key : newKeys) {
                            if (key != null && !key.trim().isEmpty()) {
                                ps.setInt(1, gameDetails.getGameDetailsId());
                                ps.setString(2, key.trim());
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }
            }

            // Xóa tất cả các thuộc tính cũ và chèn lại các thuộc tính mới
            try ( PreparedStatement psDeleteAttr = conn.prepareStatement("DELETE FROM product_attribute WHERE product_id = ?")) {
                psDeleteAttr.setInt(1, product.getProductId());
                psDeleteAttr.executeUpdate();
            }
            // Chỉ gọi hàm insert nếu danh sách attributes không rỗng
            if (attributes != null && !attributes.isEmpty()) {
                insertOrUpdateProductAttributes(conn, product.getProductId(), attributes);
            }

            if (newImageUrls != null) {
                try ( PreparedStatement ps = conn.prepareStatement("DELETE FROM image WHERE product_id = ?")) {
                    ps.setInt(1, product.getProductId());
                    ps.executeUpdate();
                }
                if (!newImageUrls.isEmpty()) {
                    String sqlInsertImage = "INSERT INTO image (product_id, image_URL) VALUES (?, ?)";
                    try ( PreparedStatement ps = conn.prepareStatement(sqlInsertImage)) {
                        for (String url : newImageUrls) {
                            if (url != null && !url.trim().isEmpty()) {
                                ps.setInt(1, product.getProductId());
                                ps.setString(2, url);
                                ps.addBatch();
                            }
                        }
                        ps.executeBatch();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error while updating product.", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public Product getProductById(int productId) {
        Product product = null;
        String productSql = "SELECT p.*, "
                + "d.sale_price, d.active AS discount_active, "
                + "c.name AS category_name, "
                + "b.brand_name, "
                + "gd.developer, gd.genre, gd.release_date, gd.game_details_id "
                + "FROM product p "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "LEFT JOIN game_details gd ON p.game_details_id = gd.game_details_id "
                + "WHERE p.product_id = ?";

        String imagesSql = "SELECT image_URL FROM image WHERE product_id = ? ORDER BY image_id";
        String attributeSql = "SELECT a.name AS attribute_name, pa.value "
                + "FROM product_attribute pa "
                + "JOIN attribute a ON pa.attribute_id = a.attribute_id "
                + "WHERE pa.product_id = ?";

        String gamePlatformsSql = "SELECT store_OS_name FROM store_platform WHERE game_details_id = ?";

        String gameOsSql = "SELECT os_name FROM operating_system WHERE game_details_id = ?";

        String gameKeysSql = "SELECT key_code FROM game_key WHERE game_details_id = ?";

        try ( Connection conn = new DBContext().getConnection()) {
            try ( PreparedStatement psProduct = conn.prepareStatement(productSql)) {
                psProduct.setInt(1, productId);
                try ( ResultSet rs = psProduct.executeQuery()) {
                    if (rs.next()) {
                        product = new Product();
                        product.setProductId(rs.getInt("product_id"));
                        product.setName(rs.getString("name"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getBigDecimal("price"));
                        product.setQuantity(rs.getInt("quantity"));

                        if (hasColumn(rs, "active_product")) {
                            product.setActiveProduct(rs.getInt("active_product"));
                        }

                        product.setCategoryId(rs.getInt("category_id"));
                        product.setBrandId((Integer) rs.getObject("brand_id"));
                        product.setGameDetailsId((Integer) rs.getObject("game_details_id"));

                        if (hasColumn(rs, "category_name")) {
                            product.setCategoryName(rs.getString("category_name"));
                        }
                        if (hasColumn(rs, "brand_name")) {
                            product.setBrandName(rs.getString("brand_name"));
                        }

                        if (hasColumn(rs, "created_at")) {
                            product.setCreatedAt(rs.getTimestamp("created_at"));
                        }
                        if (hasColumn(rs, "updated_at")) {
                            product.setUpdatedAt(rs.getTimestamp("updated_at"));
                        }

                        BigDecimal salePrice = rs.getBigDecimal("sale_price");
                        
                        Object discountActiveObj = rs.getObject("discount_active");

                        if (salePrice != null && discountActiveObj != null) {
                            int discountActive = (int) discountActiveObj;
                            product.setDiscount(new Discount(discountActive, salePrice));
                            product.setSalePrice(salePrice);
                        }

                        if (product.getGameDetailsId() != null) {
                            int gameDetailsId = product.getGameDetailsId();
                            GameDetails details = mapResultSetToGameDetails(rs);
                            details.setGameDetailsId(gameDetailsId);
                            product.setGameDetails(details);

                            List<String> platforms = new ArrayList<>();
                            try ( PreparedStatement psPlatforms = conn.prepareStatement(gamePlatformsSql)) {
                                psPlatforms.setInt(1, gameDetailsId);
                                try ( ResultSet rsPlatforms = psPlatforms.executeQuery()) {
                                    while (rsPlatforms.next()) {
                                        platforms.add(rsPlatforms.getString("store_OS_name"));
                                    }
                                }
                            }
                            List<String> operatingSystems = new ArrayList<>();
                            try ( PreparedStatement psOS = conn.prepareStatement(gameOsSql)) {
                                psOS.setInt(1, gameDetailsId);
                                try ( ResultSet rsOS = psOS.executeQuery()) {
                                    while (rsOS.next()) {
                                        operatingSystems.add(rsOS.getString("os_name"));
                                    }
                                }
                            }
                            List<String> gameKeys = new ArrayList<>();
                            try ( PreparedStatement psKeys = conn.prepareStatement(gameKeysSql)) {
                                psKeys.setInt(1, gameDetailsId);
                                try ( ResultSet rsKeys = psKeys.executeQuery()) {
                                    while (rsKeys.next()) {
                                        gameKeys.add(rsKeys.getString("key_code"));
                                    }
                                }
                            }

                        }
                    } else {
                        // Sản phẩm không tìm thấy với productId đã cho

                        return null; // Trả về null nếu không tìm thấy sản phẩm
                    }
                }
            }

            if (product != null) {
                List<String> imageUrls = new ArrayList<>();
                try ( PreparedStatement psImages = conn.prepareStatement(imagesSql)) {
                    psImages.setInt(1, productId);
                    try ( ResultSet rsImages = psImages.executeQuery()) {
                        while (rsImages.next()) {
                            imageUrls.add(rsImages.getString("image_URL"));
                        }
                    }
                }
                product.setImageUrls(imageUrls);

                List<ProductAttribute> attributes = new ArrayList<>();
                try ( PreparedStatement psAttr = conn.prepareStatement(attributeSql)) {
                    psAttr.setInt(1, productId);
                    try ( ResultSet rsAttr = psAttr.executeQuery()) {
                        while (rsAttr.next()) {
                            ProductAttribute attr = new ProductAttribute();
                            attr.setAttributeName(rsAttr.getString("attribute_name"));
                            attr.setValue(rsAttr.getString("value"));
                            attributes.add(attr);
                        }
                    }
                }
                product.setAttributes(attributes);
            }
        } catch (SQLException e) {

            return null; // Trả về null khi có lỗi SQL
        } catch (Exception e) {

            return null; // Trả về null khi có lỗi khác
        }
        return product;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setSalePrice(rs.getBigDecimal("sale_price"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setBrandId((Integer) rs.getObject("brand_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setBrandName(rs.getString("brand_name"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        product.setActiveProduct(rs.getInt("active_product"));
        return product;
    }

    private GameDetails mapResultSetToGameDetails(ResultSet rs) throws SQLException {
        GameDetails details = new GameDetails();
        details.setGameDetailsId(rs.getInt("game_details_id"));
        details.setDeveloper(rs.getString("developer"));
        details.setGenre(rs.getString("genre"));
        details.setReleaseDate(rs.getDate("release_date"));
        return details;
    }

    public List<Product> getAccessoryProducts() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.price, p.quantity, "
                + "c.name AS category_name, b.brand_name, "
                + "d.sale_price, d.active, " // 👈 thêm
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN brand b ON p.brand_id = b.brand_id "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "WHERE p.game_details_id IS NULL AND p.active_product = 1 AND p.quantity > 0 "
                + "ORDER BY p.product_id";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productList.add(mapProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getGameProducts() {
        List<Product> productList = new ArrayList<>();

        String sql = "SELECT p.product_id, p.name, p.price, p.quantity, "
                + "gd.developer AS brand_name, "
                + "d.sale_price, d.active, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN game_details gd ON p.game_details_id = gd.game_details_id "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "WHERE p.game_details_id IS NOT NULL AND p.active_product = 1 AND p.quantity > 0"
                + "ORDER BY p.product_id";

        try (
                 Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                productList.add(mapProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    private Product mapProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setBrandName(rs.getString("brand_name"));
        product.setActive(rs.getInt("active"));

        // Lấy thông tin discount nếu có
        if (hasColumn(rs, "sale_price") && hasColumn(rs, "active")) {
            BigDecimal salePrice = rs.getBigDecimal("sale_price");
            int active = rs.getInt("active");
            Discount discount = new Discount(active, salePrice);
            product.setDiscount(discount);
        }

        // Lấy ảnh sản phẩm
        String singleImageUrl = rs.getString("image_url");
        List<String> imageUrls = new ArrayList<>();
        if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
            imageUrls.add(singleImageUrl);
        }
        product.setImageUrls(imageUrls);

        return product;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Product> searchProductsByName(String query) throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, " // Chọn tất cả các cột từ bảng product
                + "c.name AS category_name, "
                + "d.sale_price, d.active AS discount_active, "
                + "(SELECT TOP 1 i.image_URL FROM image i WHERE i.product_id = p.product_id ORDER BY i.image_id) AS image_url "
                + "FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.category_id "
                + "LEFT JOIN (\n"
                + "    SELECT *\n"
                + "    FROM (\n"
                + "        SELECT *, \n"
                + "               ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY discount_id DESC) AS rn\n"
                + "        FROM discount\n"
                + "        WHERE active = 1\n"
                + "    ) AS filtered\n"
                + "    WHERE rn = 1\n"
                + ") d ON p.product_id = d.product_id\n"
                + "WHERE LOWER(p.name) LIKE ?";

        try ( Connection connection = this.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + query.toLowerCase() + "%");

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setCategoryName(rs.getString("category_name"));
                    product.setSalePrice(rs.getBigDecimal("sale_price"));
                    product.setActiveProduct(rs.getInt("active_product"));

                    String singleImageUrl = rs.getString("image_url");
                    List<String> imageUrls = new ArrayList<>();
                    if (singleImageUrl != null && !singleImageUrl.isEmpty()) {
                        imageUrls.add(singleImageUrl);
                    }
                    product.setImageUrls(imageUrls);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return products;
    }

    public double getAverageStarsForProduct(int productId) {
        // ISNULL (hoặc COALESCE cho các CSDL khác) để trả về 0 nếu kết quả AVG là NULL
        String sql = "SELECT ISNULL(AVG(CAST(value AS FLOAT)), 0.0) AS average_stars "
                + "FROM star_review WHERE product_id = ?";
        double averageStars = 0.0;

        // Sử dụng try-with-resources để đảm bảo kết nối được đóng đúng cách
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    averageStars = rs.getDouble("average_stars");
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sao trung bình cho product_id " + productId);
            e.printStackTrace();
        }
        return averageStars;
    }

    /**
     * Cập nhật trạng thái HIỂN THỊ SẢN PHẨM (cột 'active_product').
     */
    public boolean updateProductVisibility(int productId, int newStatus) throws SQLException {
        String sql = "UPDATE product SET active_product = ?, updated_at = GETDATE() WHERE product_id = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStatus);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    private void insertOrUpdateProductAttributes(Connection conn, int productId, List<ProductAttribute> attributes) throws SQLException {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }

        String findAttrSql = "SELECT attribute_id FROM attribute WHERE name = ?";
        String insertAttrSql = "INSERT INTO attribute (name) VALUES (?)";
        String insertProductAttrSql = "INSERT INTO product_attribute (product_id, attribute_id, value) VALUES (?, ?, ?)";

        try ( PreparedStatement psFind = conn.prepareStatement(findAttrSql);  PreparedStatement psInsertAttr = conn.prepareStatement(insertAttrSql, Statement.RETURN_GENERATED_KEYS);  PreparedStatement psInsertProductAttr = conn.prepareStatement(insertProductAttrSql)) {

            for (ProductAttribute pa : attributes) {
                String attributeName = pa.getAttributeName().trim();
                String attributeValue = pa.getValue().trim();
                int attributeId = -1;

                psFind.setString(1, attributeName);
                try ( ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        attributeId = rs.getInt("attribute_id");
                    }
                }

                if (attributeId == -1) {
                    psInsertAttr.setString(1, attributeName);
                    psInsertAttr.executeUpdate();
                    try ( ResultSet rs = psInsertAttr.getGeneratedKeys()) {
                        if (rs.next()) {
                            attributeId = rs.getInt(1);
                        } else {
                            throw new SQLException("Create attribute '" + attributeName + "' failed, no ID received.");
                        }
                    }
                }

                if (attributeId != -1) {
                    psInsertProductAttr.setInt(1, productId);
                    psInsertProductAttr.setInt(2, attributeId);
                    psInsertProductAttr.setString(3, attributeValue);
                    psInsertProductAttr.addBatch();
                }
            }
            psInsertProductAttr.executeBatch();
        }
    }

    public int writeReviewIntoDb(int[] productId, int customerId, int value, int orderId) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO star_review (product_id, customer_id, value, order_id)\n"
                + "VALUES ");
        Object[] params = new Object[productId.length * 4];
        int index = 0;

        for (int i = 0; i < productId.length; i++) {
            query.append("(?, ?, ?, ?)");
            if (i < productId.length - 1) {
                query.append(", ");
            }
            params[index++] = productId[i];
            params[index++] = customerId;
            params[index++] = value;
            params[index++] = orderId; // dùng cùng một order_id cho tất cả
        }

        return execQuery(query.toString(), params);
    }

    public boolean isOrderReviewed(int orderId, int customerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM star_review sr "
                + "JOIN order_detail od ON sr.product_id = od.product_id "
                + "WHERE od.order_id = ? AND sr.customer_id = ? AND od.order_id = sr.order_id";
        Object[] params = {orderId, customerId};
        ResultSet rs = execSelectQuery(query, params);
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean isProductReviewed(int orderId, int customerId, int productId) throws SQLException {
        String query = "SELECT COUNT(*) FROM star_review sr\n"
                + "WHERE sr.order_id = ? AND sr.customer_id = ? AND sr.product_id = ?; ";
        Object[] params = {orderId, customerId, productId};
        ResultSet rs = execSelectQuery(query, params);
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;

    }

}
