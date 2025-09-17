/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author PHUCHH
 */
public class CartItem {

    private int cartId;
    private int customerId;
    private int productId;
    private int cartQuantity;
    private int productQuantity;

    private String productName;
    private double price;
    private Double salePrice;
    private String imageUrl;
    private String categoryName;

   
    public CartItem(int cartId, int customerId, int productId, int cartQuantity, int productQuantity,
            String productName, double price, Double salePrice, String imageUrl, String categoryName) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.productId = productId;
        this.cartQuantity = cartQuantity;
        this.productQuantity = productQuantity;
        this.productName = productName;
        this.price = price;
        this.salePrice = salePrice;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public CartItem() {
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isCanIncrease() {
        return this.cartQuantity < this.productQuantity;
    }

    public boolean isCanDecrease() {
        return this.cartQuantity > 1;
    }

}
