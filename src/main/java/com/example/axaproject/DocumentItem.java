package com.example.axaproject;

public class DocumentItem {
    private final String name;
    private final String type;
    private final String status;

    public DocumentItem(String name, String type, String status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
