package com.inventory.DTO;

public class SellerOwnSales {
    private String customerName, customerLocation, productName, productBrand, productQuantity, dateSold;
    private double totalBuyingCost;

    public SellerOwnSales() {

    }

    public SellerOwnSales(String cName, String cLocation, String pName, String pBrand, String pQuantity, String dateSold,
                          double totalBuyingCost) {
        this.customerName = cName;
        this.customerLocation = cLocation;
        this.productName = pName;
        this.productBrand = pBrand;
        this.productQuantity = pQuantity;
        this.dateSold = dateSold;
        this.totalBuyingCost = totalBuyingCost;
    }

    public double getTotalBuyingCost() {
        return totalBuyingCost;
    }

    public void setTotalBuyingCost(double totalBuyingCost) {
        this.totalBuyingCost = totalBuyingCost;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public String getDateSold() {
        return dateSold;
    }
}
