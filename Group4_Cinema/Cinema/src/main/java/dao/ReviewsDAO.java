/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Review;
import utils.DBContext;

/**
 *
 * @author HoangSang
 */
public class ReviewsDAO extends DBContext {

    public ReviewsDAO() {
        super();
    }

    // Lấy tất cả đánh giá, bao gồm ID và tên người dùng, ID và tên phim
    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.reviewID, r.userID, u.username, r.movieID, m.title, r.rating, r.comment "
                + "FROM Reviews r "
                + "JOIN Users u ON r.userID = u.userID "
                + "JOIN Movies m ON r.movieID = m.movieID";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Review review = new Review();
                review.setReviewID(rs.getInt("reviewID"));
                review.setUserID(rs.getInt("userID")); // Thêm userID
                review.setUsername(rs.getString("username"));
                review.setMovieID(rs.getInt("movieID")); // Thêm movieID
                review.setMovieTitle(rs.getString("title"));
                review.setRating(rs.getInt("rating"));
                review.setComment(rs.getString("comment"));
                list.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thông tin một đánh giá theo ID
    public Review getReviewByID(int reviewID) {
        String sql = "SELECT * FROM Reviews WHERE ReviewID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reviewID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("UserID"),
                        rs.getInt("MovieID"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        new java.sql.Date(rs.getTimestamp("ReviewDate").getTime())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm đánh giá mới
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (ReviewID, UserID, MovieID, Rating, Comment, ReviewDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, review.getReviewID());
            ps.setInt(2, review.getUserID());
            ps.setInt(3, review.getMovieID());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật đánh giá
    public boolean updateReview(int reviewID, int rating, String comment) {
        String sql = "UPDATE Reviews SET Rating = ?, Comment = ? WHERE ReviewID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rating);
            ps.setString(2, comment);
            ps.setInt(3, reviewID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa đánh giá
    public boolean deleteReview(int reviewID) {
        String sql = "DELETE FROM Reviews WHERE ReviewID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reviewID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
