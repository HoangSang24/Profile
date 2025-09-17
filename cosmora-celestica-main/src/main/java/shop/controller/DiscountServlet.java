/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shop.dao.DiscountDAO;
import shop.dao.ProductDAO;
import shop.dao.VouchersDAO;
import shop.model.Discount;
import shop.model.Product;
import shop.model.Voucher;

/**
 *
 * @author PC
 */
@WebServlet(name = "DiscountServlet", urlPatterns = {"/manage-discounts"})
public class DiscountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String view = request.getParameter("view");
        DiscountDAO pD = new DiscountDAO();

        if (view == null || view.isEmpty() || view.equals("list")) {

            try {
                ArrayList<Discount> discountList = (ArrayList) pD.getListDicounts();

                request.setAttribute("discountlist", discountList);
                request.getRequestDispatcher("/WEB-INF/dashboard/discounts-list.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (view.equals("edit")) {

            int id = Integer.parseInt(request.getParameter("id"));
            try {
                Discount product = pD.getOne(id);
                request.setAttribute("product", product);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (view.equals("create")) {
            try {
                ProductDAO productDAO = new ProductDAO();
                List<Product> productList = productDAO.getAllProductsOfDiscount(); // hoặc lọc active_product = 1 nếu muốn

                request.setAttribute("productList", productList);
                request.getRequestDispatcher("/WEB-INF/dashboard/discount-create.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
//
        } else if (view.equals("search")) {
            try {
                ArrayList<Discount> Productlist;
                String keyword = request.getParameter("keyword").trim();
                if (keyword != null && !keyword.trim().isEmpty()) {
                    Productlist = pD.searchDiscountByProductName(keyword);
                    request.setAttribute("discountlist", Productlist);

                } else {
                    Productlist = pD.getListDicounts();
                    request.setAttribute("discountlist", Productlist);
                }

                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/WEB-INF/dashboard/discounts-list.jsp").forward(request, response);

            } catch (Exception ex) {
                Logger.getLogger(VoucherServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        DiscountDAO pD = new DiscountDAO();

        if (action != null) {
            switch (action) {
                case "edit":
    try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    BigDecimal saleprice = new BigDecimal(request.getParameter("saleprice"));
                    LocalDate startDate = LocalDate.parse(request.getParameter("start_date"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("end_date"));
                    String description = request.getParameter("description");

                    // Tạo object Discount chỉ với các field cần cập nhật (KHÔNG cần active)
                    Discount discount = new Discount(id, startDate, endDate, saleprice, description);

                    if (pD.editDiscount(discount) != 0) {
                        request.getSession().setAttribute("message", "Discount update successfully!");
                        response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");
                    } else {
                        request.setAttribute("product", discount);
                        request.setAttribute("message", "Failed to update discount!");
                        request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DiscountServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.setAttribute("error", "Invalid data error!");
                    request.getRequestDispatcher("/WEB-INF/dashboard/discount-edit.jsp").forward(request, response);
                }
                break;
                case "edit-active":
    try {
                    int discountId = Integer.parseInt(request.getParameter("voucherId"));
                    int active = Integer.parseInt(request.getParameter("active"));
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    Discount discount = new Discount(discountId, active);
                    LocalDate startDate = LocalDate.parse(request.getParameter("start_date"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("end_date"));
                    LocalDate today = LocalDate.now();
                    if ((startDate.isAfter(today) || endDate.isBefore(today)) && active == 1) {
                        request.getSession().setAttribute("message", "Invalid date error!");

                        response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");
                        break;
                    }
                    if (pD.checkIfProductHasAnotherActiveDiscount(productId, discountId) == 0) {
                        if (pD.editActiveForDiscount(discount) != 0) {
                            request.getSession().setAttribute("message", "Discount status updated successfully!");
                        } else {
                            request.getSession().setAttribute("message", "Failed to update discount status! The product may already have another active discount.");
                        }
                    } else {
                        request.getSession().setAttribute("message", "Failed to update discount status! The product may already have another active discount.");
                    }
                    response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");

                } catch (Exception ex) {
                    Logger.getLogger(DiscountServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.getSession().setAttribute("message", "Invalid data error!");
                    response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");
                }
                break;
                case "create":
                     try {
                    int productId = Integer.parseInt(request.getParameter("product_id"));
                    Product id = new Product(productId);
                    BigDecimal salePrice = new BigDecimal(request.getParameter("saleprice"));
                    BigDecimal price = new BigDecimal(request.getParameter("price"));
                    int active = Integer.parseInt(request.getParameter("active"));
                    LocalDate startDate = LocalDate.parse(request.getParameter("start_date"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("end_date"));
                    String description = request.getParameter("description");
                    BigDecimal halfMinOrder = price.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
                    Discount discount = new Discount(active, startDate, endDate, salePrice, id, description);

                    if (salePrice.compareTo(halfMinOrder) >= 0) {
                        request.setAttribute("message", "Discount value must be less than half of the value of product.");
                        request.getRequestDispatcher("/WEB-INF/dashboard/discount-create.jsp").forward(request, response);

                    } else {
                        if (pD.createDiscount(discount) != 0) {

                            request.getSession().setAttribute("message", "Discount created successfully!");
                            response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");
                        } else {
                            request.setAttribute("message", "Failed to create discount!");
                            request.getRequestDispatcher("/WEB-INF/dashboard/discount-create.jsp").forward(request, response);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(VoucherServlet.class
                            .getName()).log(Level.SEVERE, null, ex);
                    request.setAttribute("message", "Invalid data error!");
                    request.getRequestDispatcher("/WEB-INF/dashboard/discount-create.jsp").forward(request, response);
                }
                break;
                case "delete":
    try {
                    int discountId = Integer.parseInt(request.getParameter("discount_id"));
                    if (pD.deleteById(discountId) == 1) {
                        request.getSession().setAttribute("message", "Discount deleted successfully!");
                    } else {
                        request.getSession().setAttribute("message", "Failed to delete discount!");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(DiscountServlet.class.getName()).log(Level.SEVERE, null, ex);
                    request.getSession().setAttribute("message", "Error occurred while deleting discount!");
                }
                response.sendRedirect(request.getContextPath() + "/manage-discounts?view=list");
                break;

            }
        }
    }
}
