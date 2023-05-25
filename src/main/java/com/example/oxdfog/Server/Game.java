package com.example.oxdfog.Server;

import java.io.Serializable;

public class Game implements Serializable {
    private String id;
    private String title;
    private String developer;
    private String genre;
    private String filePath;
    private double price;
    private int releasedYear;
    private int reviews;
    private int size;
    private int minAge;
    private boolean controller;

    public Game(String id, String title, String developer, String genre, String filePath, double price, int releasedYear, int reviews, int size, int minAge, boolean controller) {
        this.id = id;
        this.title = title;
        this.developer = developer;
        this.genre = genre;
        this.filePath = filePath;
        this.price = price;
        this.releasedYear = releasedYear;
        this.reviews = reviews;
        this.size = size;
        this.minAge = minAge;
        this.controller = controller;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getGenre() {
        return genre;
    }

    public String getFilePath() {
        return filePath;
    }

    public double getPrice() {
        return price;
    }

    public int getReleasedYear() {
        return releasedYear;
    }

    public int getReviews() {
        return reviews;
    }

    public int getSize() {
        return size;
    }

    public int getMinAge() {
        return minAge;
    }

    public boolean isController() {
        return controller;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", developer='" + developer + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
    public String bigToString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", developer='" + developer + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", releasedYear=" + releasedYear +
                ", reviews=" + reviews +
                ", size=" + size +
                ", minAge=" + minAge +
                ", controller=" + controller +
                '}';
    }
}