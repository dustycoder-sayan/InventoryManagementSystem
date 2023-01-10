package com.inventory.DTO;

public class IssueProductNamesDTO {
    private String userName;
    private String customerName;
    private String productName;
    private String productBrand;
    private String customerLocation;
    private String dateSold;
    private int quantity;
    private double totalSale;

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setTotalSale(double totalSale) {
        this.totalSale = totalSale;
    }

    public double getTotalSale() {
        return totalSale;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserName() {
        return userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }

    public String getDateSold() {
        return dateSold;
    }

    public int getQuantity() {
        return quantity;
    }
}
