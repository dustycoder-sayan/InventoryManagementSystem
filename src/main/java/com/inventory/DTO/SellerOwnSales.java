package com.inventory.DTO;

public class SellerOwnSales {
    private String cName, cLocation, pName, pBrand, pQuantity, dateSold;
    private double totalBuyingCost;

    public SellerOwnSales() {

    }

    public SellerOwnSales(String cName, String cLocation, String pName, String pBrand, String pQuantity, String dateSold,
                          double totalBuyingCost) {
        this.cName = cName;
        this.cLocation = cLocation;
        this.pName = pName;
        this.pBrand = pBrand;
        this.pQuantity = pQuantity;
        this.dateSold = dateSold;
        this.totalBuyingCost = totalBuyingCost;
    }

    public double getTotalBuyingCost() {
        return totalBuyingCost;
    }

    public void setTotalBuyingCost(double totalBuyingCost) {
        this.totalBuyingCost = totalBuyingCost;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcLocation(String cLocation) {
        this.cLocation = cLocation;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public void setpBrand(String pBrand) {
        this.pBrand = pBrand;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public String getcName() {
        return cName;
    }

    public String getcLocation() {
        return cLocation;
    }

    public String getpName() {
        return pName;
    }

    public String getpBrand() {
        return pBrand;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public String getDateSold() {
        return dateSold;
    }
}
