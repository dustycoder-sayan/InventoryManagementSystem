package com.inventory.DTO;

public class MaxCustomer {
    private String cName;
    private double totalBuys;

    public String getcName() {
        return cName;
    }

    public double getTotalBuys() {
        return totalBuys;
    }

    public MaxCustomer(String cName, double totalBuys) {
        this.cName = cName;
        this.totalBuys = totalBuys;
    }
}
