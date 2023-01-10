package com.inventory.DTO;

public class TransactionsDTO {
    private int tId, cId;
    private double prodAmt, discount, gst, totalAmt;
    private String date, method;

    public void settId(int tId) {
        this.tId = tId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public void setProdAmt(double prodAmt) {
        this.prodAmt = prodAmt;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int gettId() {
        return tId;
    }

    public int getcId() {
        return cId;
    }

    public double getProdAmt() {
        return prodAmt;
    }

    public double getDiscount() {
        return discount;
    }

    public double getGst() {
        return gst;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public String getDate() {
        return date;
    }

    public String getMethod() {
        return method;
    }
}
