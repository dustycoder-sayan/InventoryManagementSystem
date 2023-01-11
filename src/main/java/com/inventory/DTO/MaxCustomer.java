package com.inventory.DTO;

public class MaxCustomer {
    private String customerName;
    private double totalBuys;
    private int customerId;

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getTotalBuys() {
        return totalBuys;
    }

    public MaxCustomer(int cId, String cName, double totalBuys) {
        this.customerId = cId;
        this.customerName = cName;
        this.totalBuys = totalBuys;
    }
}
