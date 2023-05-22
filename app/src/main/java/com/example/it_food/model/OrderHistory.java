package com.example.it_food.model;

public class OrderHistory {
    private String _id, address, phoneNumber, delivery, status, totalPrice;

    public OrderHistory(String _id, String address, String phoneNumber, String delivery, String status, String totalPrice) {
        this._id = _id;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.delivery = delivery;
        this.status = status;
        this.totalPrice = totalPrice;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
