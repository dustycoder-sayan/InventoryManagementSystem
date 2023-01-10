package com.inventory.DTO;

public class ProductsDTO {
    private int productId, sId, currentStock;
    private String productName, productBrand, dateAdded;
    private double costPrice;
    private double sellingPrice;

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalSales() {
        return totalSales;
    }

    private double totalSales;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getProductId() {
        return productId;
    }

    public int getsId() {
        return sId;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
