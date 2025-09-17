/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author PC
 */
public class Discount {

    private int discount_id;
    private int active;
    private LocalDate starDate;
    private LocalDate endaDate;
    private BigDecimal sale_price;
    private Product product;
    private String description;

    public Discount(int discount_id, int active) {
        this.discount_id = discount_id;
        this.active = active;
    }

  

    public Discount(int discount_id, int active, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price, Product product, String description) {
        this.discount_id = discount_id;
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
        this.product = product;
        this.description = description;
    }

   

    public Discount(int discount_id, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price, String description) {
        this.discount_id = discount_id;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
        this.description = description;
    }

    public Discount(int active, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price, Product product, String description) {
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
        this.product = product;
        this.description = description;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSale_price() {
        return sale_price;
    }

    public Discount(int discount_id, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price) {
        this.discount_id = discount_id;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
    }

    public Discount(int discount_id, int active, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price) {
        this.discount_id = discount_id;
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
    }

    public Discount(int discount_id, int active, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price, Product product) {
        this.discount_id = discount_id;
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
        this.product = product;
    }

    public Discount(int discount_id, Product productId, Product productName, int active, LocalDate starDate, LocalDate endaDate, BigDecimal sale_price, Product product) {
        this.discount_id = discount_id;
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
        this.sale_price = sale_price;
        this.product = product;
    }

    public Discount(int active, BigDecimal sale_price) {
this.active = active;
        this.sale_price = sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public Discount(int discount_id, Product product, int active, LocalDate starDate, LocalDate endaDate) {
        this.discount_id = discount_id;
        this.product = product;
        this.active = active;
        this.starDate = starDate;
        this.endaDate = endaDate;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public Discount() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public LocalDate getStarDate() {
        return starDate;
    }

    public void setStarDate(LocalDate starDate) {
        this.starDate = starDate;
    }

    public LocalDate getEndaDate() {
        return endaDate;
    }

    public void setEndaDate(LocalDate endaDate) {
        this.endaDate = endaDate;
    }
}
