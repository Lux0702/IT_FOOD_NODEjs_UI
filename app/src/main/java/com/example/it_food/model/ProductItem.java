package com.example.it_food.model;

import java.io.Serializable;

public class ProductItem {
    private String _id;
    private String name;
    private String description;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    private String categoryId;
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    private Data data;
    public ProductItem(String name, String image) {
        this.name = name;
        this.image = image;
    }

    private Double price;
    private String image;
    private Integer quantity;

    public ProductItem(String name, Double price, String image, Integer quantity) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public ProductItem(String name, String description, Double price, String image, Integer quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public ProductItem(String _id, String name, String description, Double price, String image, Integer quantity) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }
    public ProductItem(String _id, String name, String description, Double price, String image, String categoryId) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryId = categoryId;
    }

    public ProductItem(String _id, String name, String description, Double price, String image) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
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

    public static class Data implements Serializable {


        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        private String _id;

        @Override
        public String toString() {
            return "Data{" +
                    "_id='" + _id + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", price='" + price + '\'' +
                    ", quantity='" + quantity + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }

        public Data(String _id, String name, String description, String price, String quantity, String image) {
            this._id = _id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.image = image;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }



        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String name;
        private String description;
        private String price;
        private String quantity;
        private String image;



    }
}
