package com.example.gamestore.domain.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameEditModel {
    private Integer Id;
    private String title;
    private double price;
    private double size;
    private String trailer;
    private String thumbnailURL;
    private String description;
    private Date releaseDate;

    public GameEditModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = Double.parseDouble(price);
    }

    public double getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = Double.parseDouble(size);
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        String datePattern = "dd-MM-yyyy";

        if (this.releaseDate == null) {
            releaseDate = releaseDate.split(" ")[0];
            datePattern = "yyyy-dd-MM";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

        try {
            this.releaseDate = simpleDateFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}