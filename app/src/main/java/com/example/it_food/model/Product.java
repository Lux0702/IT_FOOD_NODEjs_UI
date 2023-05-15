package com.example.it_food.model;

public class Product {
    private String _id;
    private String name;
    private String description;
    private Double price;
    private String image;
    private Integer quantity;


    public Product(String _id, String name, String description, Double price, String image, Integer quantity) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Product{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
