package com.example.it_food.model;

import java.io.Serializable;
import com.example.it_food.model.User.Data;

public class User implements Serializable {
    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String id;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public User(String id, String phoneNumber, String name, String email, String gender, String avatar, String address) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.avatar = avatar;
        this.address = address;
    }

    public User(String phoneNumber, String name, String email, String gender, String address, String password) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.password = password;
    }

    private String phoneNumber, name, email, gender, avatar, address;
    private String password;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data data;


    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Data implements Serializable {
        public Data(String id, String phoneNumber, String name, String email, String gender, String avatar, String address) {
            this.id = id;
            this.phoneNumber = phoneNumber;
            this.name = name;
            this.email = email;
            this.gender = gender;
            this.avatar = avatar;
            this.address = address;
        }

        private String id;
        private String phoneNumber;
        private String name;
        private String email;
        private String gender;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String avatar;
        private String address;

        // Constructors, getters, and setters for the fields above

        // ...

        @Override
        public String toString() {
            return "Data{" +
                    "id='" + id + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", gender='" + gender + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", address='" + address + '\'' +
                    '}';

        }

    }
}

