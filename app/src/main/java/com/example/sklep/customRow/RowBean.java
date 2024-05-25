package com.example.sklep.customRow;

public class RowBean {
    public int id;
    public String productName;
    public String price;
    public String img;

    public RowBean() {
    }

    public RowBean(int id, String productName, String price, String img) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.img = img;
    }

}
