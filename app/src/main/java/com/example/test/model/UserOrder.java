package com.example.test.model;

public class UserOrder {

    private String orderId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private int orderItemStatus;
    private int orderStatus;

    private double orderTotal;
    private String orderDate;
    private String orderTime;


    public UserOrder(String orderId, String productName, int quantity, double unitPrice, double totalPrice, int orderItemStatus, int orderStatus, double orderTotal, String orderDate, String orderTime) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.orderItemStatus = orderItemStatus;
        this.orderStatus = orderStatus;

        this.orderTotal = orderTotal;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(int orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }
}