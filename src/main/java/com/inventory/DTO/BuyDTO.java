package com.inventory.DTO;

public class BuyDTO {
    private int prodId, quantity;

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProdId() {
        return prodId;
    }

    public int getQuantity() {
        return quantity;
    }
}
