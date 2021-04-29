package com.example.android.carzenia.SystemDatabase;

public class CarModel {

    private String id;
    private String type;
    private String occasion;
    private String price;
    private String imageUri;

    public  CarModel () {
        // Don't Remove The Constructor.
    }

    public CarModel(String id, String type, String occasion, String price, String imageUri) {
        this.id = id;
        this.type = type;
        this.occasion = occasion;
        this.price = price;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getOccasion() {
        return occasion;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }
}
