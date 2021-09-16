package com.example.tc_robots.backend;

import android.widget.ImageView;

import java.util.Objects;

public class Article implements Comparable<Article> {
    private int requiredQuantity;
    private int availableQuantityInStorage;
    private String name;
    private int imageID;

    public Article(String name) {
        this.name = name;
    }

    public Article(String name, int imageID) {
        this(name);
        this.setImageID(imageID);
    }

    public Article(String name, int imageID, int availableQuantityInStorage) {
        this(name, imageID);
        this.setAvailableQuantityInStorage(availableQuantityInStorage);
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

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    @Override
    public int compareTo(Article article) {
        return article.getName().equals(this.getName()) ? 1 : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(name, article.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredQuantity, availableQuantityInStorage, name);
    }


}
