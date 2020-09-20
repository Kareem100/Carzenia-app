package com.example.android.carzenia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CarDB extends SQLiteOpenHelper {

    private SQLiteDatabase CarzeniaDatabase;
    private ByteArrayOutputStream stream;
    private byte[] ImageBytes;
    private Context context;

    public CarDB(@Nullable Context context) {
        super(context, "CarzeniaDatabase", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table Car(ID INTEGER primary key, Type TEXT not null, " +
                        "Occasion TEXT not null, Price TEXT not null, Image BLOB not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists Car");
        onCreate(database);
    }

    public void clearCarTable(){
        CarzeniaDatabase=getWritableDatabase();
        CarzeniaDatabase.execSQL("DELETE FROM Car");
        CarzeniaDatabase.close();
        Toast.makeText(context, "ALL CARS WERE DELETED !", Toast.LENGTH_SHORT).show();
    }

    public void addCarToDB(String type, String occasion, String price, Bitmap bitmap){
        processImage(bitmap);
        ContentValues row = new ContentValues();
        row.put("Type", type);
        row.put("Occasion", occasion);
        row.put("Price", price);
        row.put("Image", ImageBytes);
        CarzeniaDatabase = getWritableDatabase();
        CarzeniaDatabase.insert("Car", null, row);
        CarzeniaDatabase.close();
    }
    private void processImage(Bitmap bitmap){
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ImageBytes = stream.toByteArray();
    }

    public ArrayList<CarModel> getCarsData(){
        Cursor cursor = getCarsFromDB();
        ArrayList<CarModel> arrayList = new ArrayList<CarModel>();

        while(!cursor.isAfterLast()){

            // FIVE ATTRIBUTES FOR CAR INFO....
            int ID = cursor.getInt(0);
            String type = cursor.getString(1);
            String occasion = cursor.getString(2);
            String price = cursor.getString(3);
            ImageBytes = cursor.getBlob(4);
            Bitmap bitmap = BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.length);
            arrayList.add(new CarModel(ID, type, occasion, price, bitmap));

            cursor.moveToNext();
        }
        return arrayList;
    }

    private Cursor getCarsFromDB(){
        CarzeniaDatabase = getReadableDatabase();
        String[] row = {"ID", "Type", "Occasion", "Price", "Image"};
        Cursor cursor = CarzeniaDatabase.query("Car", row, null, null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();
        CarzeniaDatabase.close();
        return cursor;
    }

}
