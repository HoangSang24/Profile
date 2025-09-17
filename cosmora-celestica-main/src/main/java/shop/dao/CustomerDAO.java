/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.db.DBContext;
import shop.model.Customer;

/**
 *
 * @author CE190449 - Le Anh Khoa
 */
public class CustomerDAO extends DBContext {

    public List<Customer> getPaginatedCustomerList(int currentPage, int pageSize) {
        List<Customer> list = new ArrayList<>();
        try {
            String query = "SELECT *\n"
                    + "FROM customer\n"
                    + "ORDER BY customer_id\n"
                    + "OFFSET ? ROWS\n"
                    + "FETCH NEXT ? ROWS ONLY";
            Object[] params = {(currentPage - 1) * pageSize, pageSize};
            ResultSet rs = execSelectQuery(query, params);
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int getTotalCustomerCount() {
        try {
            String query = "SELECT COUNT(customer_id)\n"
                    + "from customer";
            ResultSet rs = execSelectQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Customer> getPaginatedCustomersBySearch(String name, int currentPage, int pageSize) {
        List<Customer> list = new ArrayList<>();
        try {
            String query = "SELECT *\n"
                    + "FROM customer\n"
                    + "WHERE username LIKE ?\n"
                    + "ORDER BY customer_id\n"
                    + "OFFSET ? ROWS\n"
                    + "FETCH NEXT ? ROWS ONLY";
            Object[] params = {
                "%" + name + "%",
                (currentPage - 1) * pageSize,
                pageSize
            };
            ResultSet rs = execSelectQuery(query, params);
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int getTotalCustomerCountBySearch(String name) {
        try {
            String query = "SELECT COUNT(customer_id)\n"
                    + "FROM customer\n"
                    + "WHERE username LIKE ?";
            Object[] params = {
                "%" + name + "%"
            };
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public Customer getAccountByEmail(String email) {
        try {
            String query = "SELECT *\n"
                    + "FROM customer c\n"
                    + "WHERE c.email = ?";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Customer getAccountByGoogleId(String googleId, String email) {
        try {
            String query = "SELECT *\n"
                    + "FROM customer c\n"
                    + "WHERE c.google_id = ? OR email = ?";
            Object[] params = {googleId, email};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Customer getAccountByEmailAndVerify(String email) {
        try {
            String query = "SELECT *\n"
                    + "FROM customer c\n"
                    + "WHERE c.email = ? \n"
                    + "	AND c.is_deactivated = 0;";
            Object[] params = {email};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Customer getAccountById(int id) {
        try {
            String query = "SELECT *\n"
                    + "FROM customer c\n"
                    + "WHERE c.customer_id = ?";
            Object[] params = {id};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("avatar_url"),
                        rs.getDate("date_of_birth"),
                        rs.getBoolean("is_deactivated"),
                        rs.getBoolean("has_set_password"),
                        rs.getTimestamp("last_login"),
                        rs.getString("google_id"),
                        rs.getString("reset_token"),
                        rs.getTimestamp("reset_token_expiry"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean isUsernameOrEmailTaken(String username, String email) {
        try {
            String query = "SELECT COUNT(*) FROM customer \n"
                    + "WHERE username = ? OR email = ?";
            Object[] params = {username, email};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean isUsernameTakenByOthers(int id, String username) {
        try {
            String query = "SELECT COUNT(*) FROM customer \n"
                    + "WHERE username = ? AND customer_id != ?";
            Object[] params = {username, id};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int storeOtpForEmail(String email, String otp, Timestamp expiry) {
        try {
            String query = "UPDATE customer SET reset_token = ?, reset_token_expiry = ? WHERE email = ?";
            Object[] params = {otp, expiry, email};
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public boolean checkOtpForEmail(String email, String otp) {
        try {
            String query = "SELECT *\n"
                    + "FROM customer c\n"
                    + "WHERE c.email = ?\n"
                    + "  AND c.reset_token = ?\n"
                    + "  AND c.reset_token_expiry > GETDATE();";
            Object[] params = {email, otp};
            ResultSet rs = execSelectQuery(query, params);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int createGoogleCustomerAccount(Customer customer) {
        try {
            String query = "INSERT INTO customer (full_name, username, email, password_hash, avatar_url, google_id, has_set_password, last_login, created_at)\n"
                    + "VALUES (?, ?, ?, ?, ?, ?, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
            Object[] params = {
                customer.getFullName(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPasswordHash(),
                customer.getAvatarUrl(),
                customer.getGoogleId(),};
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int createCustomerIfUnique(Customer customer) {
        try {
            // Check if the username or email is already taken
            String checkQuery = "SELECT COUNT(*) FROM customer WHERE username = ? OR email = ?";
            Object[] params = {customer.getUsername(), customer.getEmail()};
            ResultSet rs = execSelectQuery(checkQuery, params);

            if (rs.next() && rs.getInt(1) <= 0) {
                // Proceed to insert the new customer
                String insertQuery = "INSERT INTO customer (full_name, username, email, password_hash, avatar_url, has_set_password, last_login, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
                Object[] insertParams = {
                    customer.getFullName(),
                    customer.getUsername(),
                    customer.getEmail(),
                    customer.getPasswordHash(),
                    customer.getAvatarUrl()
                };

                return execQuery(insertQuery, insertParams);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;  // Returns false if any error occurs
    }

    public int updateCustomer(Customer customer) {
        try {
            String query = "UPDATE Customer\n"
                    + "SET "
                    + "full_name = ?,\n"
                    + "	username = ?,\n"
                    + "	email = ?,\n"
                    + "	phone = ?,\n"
                    + "	address = ?\n"
                    + "WHERE customer_id = ?";
            Object[] params = {
                customer.getFullName(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCustomerId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int bindGoogleAccountAndUpdateLastLoginTime(Customer customer) {
        try {
            String query = "UPDATE Customer\n"
                    + "SET "
                    + "	google_id = ?,\n"
                    + " last_login = CURRENT_TIMESTAMP\n"
                    + "WHERE customer_id = ?;";
            Object[] params = {
                customer.getGoogleId(),
                customer.getCustomerId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateProfileCustomer(Customer customer) {
        try {
            String query = "UPDATE Customer\n"
                    + "SET full_name = ?,\n"
                    + "	username = ?,\n"
                    + "	email = ?,\n"
                    + "	phone = ?,\n"
                    + "	gender = ?,\n"
                    + "	address = ?,\n"
                    + "	avatar_url = ?,\n"
                    + "	date_of_birth = ?\n"
                    + "WHERE customer_id = ?";
            Object[] params = {
                customer.getFullName(),
                customer.getUsername(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getGender(),
                customer.getAddress(),
                customer.getAvatarUrl(),
                customer.getDateOfBirth(),
                customer.getCustomerId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateLastLoginTime(Customer customer) {
        try {
            String query = "UPDATE Customer\n"
                    + "SET last_login = CURRENT_TIMESTAMP\n"
                    + "WHERE customer_id = ?";
            Object[] params = {
                customer.getCustomerId()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int deleteCustomer(int id) {
        try {
            String query = "DELETE FROM customer\n"
                    + "WHERE customer_id = ?";
            Object[] params = {
                id
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateCustomerPassword(Customer customer) {
        try {
            String query = "UPDATE customer\n"
                    + "SET password_hash = ?,\n"
                    + "has_set_password = 1\n"
                    + "WHERE email = ?";
            Object[] params = {
                customer.getPasswordHash(),
                customer.getEmail()
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int updateCustomerStatus(int id, boolean status) {
        try {
            String query = "UPDATE Customer\n"
                    + "SET "
                    + " is_deactivated = ?\n"
                    + "WHERE customer_id = ?";
            Object[] params = {
                status,
                id
            };
            return execQuery(query, params);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
