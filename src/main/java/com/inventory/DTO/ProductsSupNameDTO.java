package com.inventory.DTO;

public class ProductsSupNameDTO {
    private int productId;
    private String productName;
    private String productBrand;
    private Double costPrice;
    private Double sellingPrice;
    private int currentStock;
    private String dateAdded;
    private String supplierName;
    private String supplierLocation;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierLocation(String supplierLocation) {
        this.supplierLocation = supplierLocation;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierLocation() {
        return supplierLocation;
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
