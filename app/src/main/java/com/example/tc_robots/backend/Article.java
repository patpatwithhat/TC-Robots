package com.example.tc_robots.backend;

public class Article {
    private int requiredQuantity;
    private int availableQuantityInStorage;
    private String name;

    public Article(String name) {
        this.name = name;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public int getAvailableQuantityInStorage() {
        return availableQuantityInStorage;
    }

    public void setAvailableQuantityInStorage(int availableQuantityInStorage) {
        this.availableQuantityInStorage = availableQuantityInStorage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
