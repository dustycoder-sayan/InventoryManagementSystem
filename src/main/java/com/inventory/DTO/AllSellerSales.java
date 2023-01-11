package com.inventory.DTO;

public class AllSellerSales {
    private String userName;
    private double totalSales;
    private int userId;

    public AllSellerSales(int userId, String uName, double totalSales) {
        this.userId = userId;
        this.userName = uName;
        this.totalSales = totalSales;
    }

    public int getUserId() { return userId; }

    public String getUserName() {
        return userName;
    }

    public double getTotalSales() {
        return totalSales;
    }
}
