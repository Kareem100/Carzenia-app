package com.example.android.carzenia;

import android.graphics.Bitmap;

public class CarModel {

    private int id;
    private String type;
    private String occasion;
    private String price;
    private Bitmap bitmap;

    public CarModel(int id, String type, String occasion, String price, Bitmap bitmap) {
        this.id = id;
        this.type = type;
        this.occasion = occasion;
        this.price = price;
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
