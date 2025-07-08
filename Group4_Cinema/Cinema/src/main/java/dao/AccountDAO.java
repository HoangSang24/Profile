package dao;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Account;
import utils.DBContext;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author HoangSang
 */
public class AccountDAO extends DBContext {

    public AccountDAO() {
        super();
    }

//    // Hàm cấp quyền admin cho user
//    public boolean grantAdmin(int userId) {
//        String sql = "UPDATE Users SET Role = 'Admin' WHERE UserID = ?";
//        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, userId);
//            int rowsUpdated = stmt.executeUpdate();
//            return rowsUpdated > 0; // Trả về true nếu update thành công
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    public Account verifyMD5(String user, String pass) {
        Account acc = new Account();
        String passMD5 = hashMD5(pass);
        String sql = "SELECT * FROM Users where Username=? and Password=?";
        try {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, passMD5);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("UserID");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String mail = rs.getString("Email");
                String role = rs.getString("Role");
                acc.setId(id);
                acc.setUsername(username);
                acc.setPassword(password);
                acc.setEmail(mail);
                acc.setRole(role);
                return acc;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return acc;
    }

    public String hashMD5(String pass) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] mess = md5.digest(pass.getBytes());
            StringBuilder str = new StringBuilder();
            //[0x0a, 0x12, 0x7a]
            for (byte mes : mess) {
                //byte 1: 0x0a
                String c = String.format("%02x", mes);
                //->0a
                str.append(c);
            }
            //0a127a
            return str.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT UserID, Username, Password, Email, Role FROM Users";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("UserID");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                String email = rs.getString("Email");
                String role = rs.getString("Role");
                // Tạo đối tượng Account và thêm vào danh sách
                Account account = new Account(id, username, password, email, role);
                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account getAccountByUsername(String username) {
        Account acc = null;
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                acc = new Account(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE Username = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu có kết quả
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT 1 FROM Users WHERE Email = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAccount(Account account) {
        if (isUsernameExists(account.getUsername()) || isEmailExists(account.getEmail())) {
            return false; // Nếu username hoặc email đã tồn tại
        }

        String getMaxId = "SELECT MAX(UserID) AS maxid FROM Users";
        int nextId = 1; // Mặc định UserID = 1 nếu bảng rỗng

        try ( PreparedStatement psGetMaxId = conn.prepareStatement(getMaxId);  ResultSet rsGetMaxId = psGetMaxId.executeQuery()) {

            if (rsGetMaxId.next()) {
                nextId = rsGetMaxId.getInt("maxid");
                if (rsGetMaxId.wasNull()) {
                    nextId = 1; // Nếu bảng rỗng, đặt UserID = 1
                } else {
                    nextId += 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return false; // Lỗi khi lấy UserID
        }

        String sql = "INSERT INTO Users (UserID, Username, Password, Email, Role) VALUES (?, ?, ?, ?, 'Customer')";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setString(2, account.getUsername());
            ps.setString(3, hashMD5(account.getPassword())); // Mã hóa mật khẩu
            ps.setString(4, account.getEmail());

            return ps.executeUpdate() > 0; // Trả về true nếu chèn thành công
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        }

        return false;
    }

    public Account getAccountById(int id) {
        String sql = "select * from Users where UserID = ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Account c = new Account(rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getString("Role"));
                return c;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


   public boolean deleteAccount(int userID) {
    String query = "DELETE FROM Users WHERE UserID = ?";
    try (
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setInt(1, userID);
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có bản ghi được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }
//    public boolean updatePassword(String email, String newPasswordHash) {
//    String sql = "UPDATE Users SET password = ? WHERE email = ?";
//    try (
//         PreparedStatement ps = conn.prepareStatement(sql)) {
//        ps.setString(1, newPasswordHash);
//        ps.setString(2, email);
//        int rowsUpdated = ps.executeUpdate();
//        System.out.println("Rows updated: " + rowsUpdated); // Debug
//        return rowsUpdated > 0;
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return false;
//    }
//}

    public int update(Account acc) {
        String sql = "update Users set Username = ?, Password = ?, Email = ?, Role = ? where UserID =  ?";
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, acc.getUsername());
            st.setString(2, acc.getPassword());
            st.setString(3, acc.getEmail());
            st.setString(4, acc.getRole());
            st.setInt(5, acc.getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                return 1; // Xoá thành công 
            } else {
                return 0; // Không có hàng nào bị xoá 
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public Account getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("Email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi chi tiết hơn
        }
        return null;
    }

    public boolean updateRole(int userId, String newRole) {
        String query = "UPDATE Users SET Role = ? WHERE UserID = ?";
        try (
                 PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, newRole);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
