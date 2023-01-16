package com.inventory.DTO;

public class MaxCustomer {
    private String customerName, customerPhone;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public MaxCustomer(int cId, String cName, String cPhone, double totalBuys) {
        this.customerId = cId;
        this.customerName = cName;
        this.totalBuys = totalBuys;
        this.customerPhone = cPhone;
    }
}
