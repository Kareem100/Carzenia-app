package com.example.android.carzenia.SystemDatabase;

public class UserModel {

    private String name;
    private String mail;
    private String phone;
    private UserType type;
    private String imageUrl;
    public static final String NO_IMAGE = "No Current Image";

    public UserModel(){
        // Don't Remove This Constructor
    }

    public UserModel (String name, String mail, String phone, UserType type, String imageUrl) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.type = type;
        this.imageUrl = imageUrl;
        if (imageUrl.equals(""))
            this.imageUrl = NO_IMAGE;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public UserType getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
