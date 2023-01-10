package com.inventory.DTO;

public class IssueProductDTO {
    private int uId, cId, pId, quantity;
    private String dateSold;

    public void setuId(int uId) {
        this.uId = uId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDateSold(String dateSold) {
        this.dateSold = dateSold;
    }

    public int getuId() {
        return uId;
    }

    public int getcId() {
        return cId;
    }

    public int getpId() {
        return pId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDateSold() {
        return dateSold;
    }
}
