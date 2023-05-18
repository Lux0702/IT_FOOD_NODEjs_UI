package com.example.it_food.model;

public class Delivery {
    private String _id;
    private String name;
    private int Price;

    public Delivery(String _id, String name, int price) {
        this._id = _id;
        this.name = name;
        Price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }
}
