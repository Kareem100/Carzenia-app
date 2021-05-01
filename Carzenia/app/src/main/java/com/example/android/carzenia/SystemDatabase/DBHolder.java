package com.example.android.carzenia.SystemDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBHolder {

    public static final String USERS_DATABASE_INFO_ROOT = "Users";
    public static final String CARS_DATABASE_INFO_ROOT = "Cars";
    public static final String MSSGS_DATABASE_INFO_ROOT = "Messages";
    public static final String USERS_DATABASE_IMG_ROOT = "UsersImages";
    public static final String CARS_DATABASE_IMG_ROOT = "CarsImages";

    public static List<CarModel> carsData = new ArrayList<>();
    public static List<MessageModel> messagesData = new ArrayList<>();

}
