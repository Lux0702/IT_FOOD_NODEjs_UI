package com.example.it_food.model;

public class Address {
    private String _id, address;

    public Address(String _id, String address) {
        this._id = _id;
        this.address = address;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
