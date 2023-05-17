package com.example.it_food.model;

public class GetUserResponse {
    private String status;
    private String message;
    private User result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getResult() {
        return result;
    }

    public void setResult(User result) {
        this.result = result;
    }

    public GetUserResponse(User result) {
        this.result = result;
    }
}
