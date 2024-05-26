package com.example.sklep.customOrder;

public class OrderRowBean {
    public int id;
    public String productName;
    public String price;
    public String img;
    public String addons;
    public int amount;


    public OrderRowBean(int id, String productName, String price, String img, String addons, int amount) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.img = img;
        this.addons = addons;
        this.amount = amount;
    }
}
