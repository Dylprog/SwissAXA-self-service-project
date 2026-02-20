package com.example.axaproject;

public class Policy {
    private String title;
    private String number;
    private String expiryDate;
    private final boolean external;

    public Policy(String title, String number, String expiryDate, boolean external) {
        this.title = title;
        this.number = number;
        this.expiryDate = expiryDate;
        this.external = external;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExternal() {
        return external;
    }
}
