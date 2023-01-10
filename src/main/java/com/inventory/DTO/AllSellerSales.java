package com.inventory.DTO;

public class AllSellerSales {
    private String uName;
    private double totalSales;

    public AllSellerSales(String uName, double totalSales) {
        this.uName = uName;
        this.totalSales = totalSales;
    }

    public String getuName() {
        return uName;
    }

    public double getTotalSales() {
        return totalSales;
    }
}
