package com.inventory.DTO;

public class CustomerDTO {
    private int customerId;

    public void setLocationCount(int locationCount) {
        this.locationCount = locationCount;
    }

    public int getLocationCount() {
        return locationCount;
    }

    private int locationCount;
    private String customerName, customerPhone, customerLocation;
    private Double sales;

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getSales() {
        return sales;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }
}
