package shop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONObject;
import shop.dao.CartDAO;
import shop.model.Cart;
import shop.model.CartItem;
import shop.model.Customer;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);  
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        int customerId = customer.getCustomerId();

        try {
            CartDAO cartDAO = new CartDAO();
            List<CartItem> cartItems = cartDAO.getCartItemsByCustomerId(customerId);

            request.setAttribute("cartItems", cartItems);
            request.getRequestDispatcher("/WEB-INF/home/cart-list.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro when get list", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Customer customer = (Customer) session.getAttribute("currentCustomer");
        int customerId = customer.getCustomerId();

        String action = request.getParameter("action");
        String page = request.getParameter("page") != null ? request.getParameter("page") : "cart";

        response.setContentType("application/json");
        JSONObject json = new JSONObject();

        try (PrintWriter out = response.getWriter()) {
            CartDAO cartDAO = new CartDAO();

            switch (action) {
                case "add": {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));

                    Cart existingCart = cartDAO.findCartItem(customerId, productId);
                    if (existingCart != null) {
                        existingCart.setQuantity(existingCart.getQuantity() + quantity);
                        cartDAO.updateCart(existingCart);
                    } else {
                        Cart newCart = new Cart();
                        newCart.setCustomerId(customerId);
                        newCart.setProductId(productId);
                        newCart.setQuantity(quantity);
                        cartDAO.insertCart(newCart);
                    }

                    int cartCount = cartDAO.countCartItems(customerId);
                    session.setAttribute("cartCount", cartCount);

                     response.sendRedirect(request.getContextPath() + "/" + page);

                    break;
                }}}
//
//                case "increase": {
//                    int productId = Integer.parseInt(request.getParameter("productId"));
//                    int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//                    Cart existingCart = cartDAO.findCartItem(customerId, productId);
//                    if (existingCart != null) {
//                        int currentQuantity = existingCart.getQuantity();
//                        existingCart.setQuantity(currentQuantity + quantity);
//                        cartDAO.updateCart(existingCart);
//
//                        CartItem updatedItem = cartDAO.getCartItemByCustomerIdAndProductId(customerId, productId);
//
//                        json.put("status", "success");
//                        json.put("message", "Increased quantity successfully.");
//                        json.put("cartQuantity", updatedItem.getCartQuantity());
//                        json.put("canIncrease", updatedItem.getCartQuantity() < updatedItem.getProductQuantity());
//                        double unitPrice = updatedItem.getSalePrice() != null ? updatedItem.getSalePrice() : updatedItem.getPrice();
//                        json.put("totalPrice", updatedItem.getCartQuantity() * unitPrice);
//
//                    } else {
//                        json.put("status", "error");
//                        json.put("message", "Product not found in cart.");
//                    }
//
//                    out.print(json.toString());
//                    break;
//                }

//                case "decrease": {
//                    int productId = Integer.parseInt(request.getParameter("productId"));
//                    int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//                    Cart existingCart = cartDAO.findCartItem(customerId, productId);
//                    if (existingCart != null) {
//                        int currentQuantity = existingCart.getQuantity();
//                        if (currentQuantity <= quantity || currentQuantity == 1) {
//                            cartDAO.delete(productId, customerId);
//                            json.put("status", "success");
//                            json.put("message", "Removed product from cart.");
//                            json.put("deleted", true);
//                        } else {
//                            existingCart.setQuantity(currentQuantity - quantity);
//                            cartDAO.updateCart(existingCart);
//
//                            CartItem updatedItem = cartDAO.getCartItemByCustomerIdAndProductId(customerId, productId);
//                            double unitPrice = updatedItem.getSalePrice() != null ? updatedItem.getSalePrice() : updatedItem.getPrice();
//
//                            json.put("status", "success");
//                            json.put("message", "Decreased quantity successfully.");
//                            json.put("cartQuantity", updatedItem.getCartQuantity());
//                            json.put("canIncrease", updatedItem.getCartQuantity() < updatedItem.getProductQuantity());
//                            json.put("totalPrice", updatedItem.getCartQuantity() * unitPrice);
//                            json.put("deleted", false);
//                        }
//                    } else {
//                        json.put("status", "error");
//                        json.put("message", "Product not found in cart.");
//                    }
//
//                    out.print(json.toString());
//                    break;
//                }
//
//                case "delete": {
//                    int productId = Integer.parseInt(request.getParameter("productId"));
//
//                    int rowsDeleted = cartDAO.delete(productId, customerId);
//                    if (rowsDeleted == 1) {
//                        json.put("status", "success");
//                        json.put("message", "Deleted product from cart.");
//                        json.put("deleted", true);
//                    } else {
//                        json.put("status", "error");
//                        json.put("message", "Product not found or could not be deleted.");
//                    }
//
//                    out.print(json.toString());
//                    break;
//                }
//
//                default:
//                    json.put("status", "error");
//                    json.put("message", "Invalid action.");
//                    out.print(json.toString());
//                    break;
//            }

//        } catch (Exception e) {
//            e.printStackTrace();
//            JSONObject errorJson = new JSONObject();
//            errorJson.put("status", "error");
//            errorJson.put("message", "An error occurred: " + e.getMessage());
//            response.getWriter().print(errorJson.toString());
        }
    

    @Override
    public String getServletInfo() {
        return "CartServlet handles all cart operations (add, increase, decrease, delete, list) with JSON response support for AJAX.";
    }
}
