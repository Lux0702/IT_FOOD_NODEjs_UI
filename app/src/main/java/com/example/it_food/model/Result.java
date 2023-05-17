package com.example.it_food.model;

public class Result {
    private String status;
    private String message;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Result(String status, String message, String result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }
}
