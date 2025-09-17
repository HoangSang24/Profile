package api;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import shop.dao.CartDAO;
import shop.model.Cart;
import shop.model.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "CartApiServlet", urlPatterns = {"/api/cart"})
public class CartApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JsonObject jsonRequest = com.google.gson.JsonParser.parseString(sb.toString()).getAsJsonObject();

            String action = jsonRequest.get("action").getAsString();
            int productId = jsonRequest.get("productId").getAsInt();
            int quantity = jsonRequest.get("quantity").getAsInt();

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentCustomer") == null) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "You must be logged in.");
                out.print(jsonResponse.toString());
                return;
            }

            Customer customer = (Customer) session.getAttribute("currentCustomer");
            int customerId = customer.getCustomerId();

            CartDAO cartDAO = new CartDAO();
            Cart existingCart = cartDAO.findCartItem(customerId, productId);
            int productQuantity = cartDAO.getProductQuantity(productId);

            switch (action) {
                case "increase":
                    if (existingCart != null) {
                        int currentQuantity = existingCart.getQuantity();
                        if (currentQuantity < productQuantity) {
                            existingCart.setQuantity(currentQuantity + quantity);
                            cartDAO.updateCart(existingCart);

                            double unitPrice = cartDAO.getUnitPrice(productId);
                            boolean canIncrease = existingCart.getQuantity() < productQuantity;
                            boolean canDecrease = existingCart.getQuantity() > 1;

                            jsonResponse.addProperty("status", "success");
                         
                            jsonResponse.addProperty("newQuantity", existingCart.getQuantity());
                            jsonResponse.addProperty("newTotal", unitPrice * existingCart.getQuantity());
                            jsonResponse.addProperty("canIncrease", canIncrease);
                            jsonResponse.addProperty("canDecrease", canDecrease);
                            jsonResponse.addProperty("deleted", false);
                           
                        } else {
                             request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);
                        }
                    } else {
                        jsonResponse.addProperty("status", "error");
                        jsonResponse.addProperty("message", "Product not found in cart.");
                    }
                    break;

                case "decrease":
                    if (existingCart != null) {
                        int currentQuantity = existingCart.getQuantity();
                        if (currentQuantity <= quantity) {
                            cartDAO.delete(productId, customerId);
                            jsonResponse.addProperty("status", "success");
                            jsonResponse.addProperty("message", "Removed product from cart.");
                            jsonResponse.addProperty("deleted", true);

                        } else {
                            existingCart.setQuantity(currentQuantity - quantity);
                            cartDAO.updateCart(existingCart);

                            double unitPrice = cartDAO.getUnitPrice(productId);
                            boolean canIncrease = existingCart.getQuantity() < productQuantity;
                            boolean canDecrease = existingCart.getQuantity() > 1;

                            jsonResponse.addProperty("status", "success");
                          
                            jsonResponse.addProperty("newQuantity", existingCart.getQuantity());
                            jsonResponse.addProperty("newTotal", unitPrice * existingCart.getQuantity());
                            jsonResponse.addProperty("canIncrease", canIncrease);
                            jsonResponse.addProperty("canDecrease", canDecrease);
                            jsonResponse.addProperty("deleted", false);
                        }
                    } else {
                         request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);
                    }
                    break;

                case "delete":
                    if (existingCart != null) {
                        cartDAO.delete(productId, customerId);

                        jsonResponse.addProperty("status", "success");
                    
                        jsonResponse.addProperty("deleted", true);
                       
                    } else {
                        request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);
                    }

                    break;

                default:
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Invalid action.");
                    break;
            }

            
            int cartCount = cartDAO.countCartItems(customerId);
            session.setAttribute("cartCount", cartCount);

           
            double summaryTotal = cartDAO.getCartSummaryTotal(customerId);
            jsonResponse.addProperty("summaryTotal", summaryTotal);

        } catch (Exception e) {
            e.printStackTrace();
             request.getRequestDispatcher("/WEB-INF/error/not-found.jsp").forward(request, response);
        }

        out.print(jsonResponse.toString());
        out.flush();
    }
}
