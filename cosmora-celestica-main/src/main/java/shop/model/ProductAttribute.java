/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shop.model;

/**
 *
 * @author HoangSang
 */
public class ProductAttribute {

    private int productId;
    private int attributeId;
    private String value;
    private String attributeName;

    public ProductAttribute() {
    }

    public ProductAttribute(String value, String attributeName) {
        this.value = value;
        this.attributeName = attributeName;
    }

    public ProductAttribute(int productId, int attributeId, String value, String attributeName) {
        this.productId = productId;
        this.attributeId = attributeId;
        this.value = value;
        this.attributeName = attributeName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute value cannot be null or empty.");
        }
        this.value = value.trim();
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        if (attributeName == null || attributeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Attribute name cannot be null or empty.");
        }
        this.attributeName = attributeName.trim();
    }

}
