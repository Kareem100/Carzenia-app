package com.example.android.carzenia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    private SQLiteDatabase CarzeniaDatabase;
    private ByteArrayOutputStream stream;
    private byte[] ImageBytes;
    private Context context;

    public DBManager(@Nullable Context context) {
        super(context, "CarzeniaDatabase", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table Car(ID INTEGER primary key, Type TEXT not null, " +
                        "Occasion TEXT not null, Price TEXT not null, Image BLOB not null)");
        database.execSQL("create table User(Name TEXT primary key, Mail TEXT not null," +
                "Phone TEXT not null, Password TEXT not null, Type TEXT not null, Image BLOP)");

        database.execSQL("insert into User(Name, Mail, Phone, Password, Type)"+
                "values('admin', 'kareimshreif@yahoo.com', '01143734174', 'admin', 'admin')");
        database.execSQL("insert into User(Name, Mail, Phone, Password, Type)"+
                "values('user', 'user1@gmail.com', '01282496823', 'user', 'user')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists Car");
        database.execSQL("drop table if exists User");
        onCreate(database);
    }

    /********* START COMMON METHODS *********/
    private void processImage(Bitmap bitmap){
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        ImageBytes = stream.toByteArray();
    }
    /********* END COMMON METHODS *********/

    /********* START CAR DATA *********/
    public void clearCarTable(){
        CarzeniaDatabase=getWritableDatabase();
        CarzeniaDatabase.execSQL("DELETE FROM Car");
        CarzeniaDatabase.close();
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

    public void updateCarData(int ID, String type, String occasion, String price, Bitmap bitmap){
        ContentValues values = new ContentValues();
        processImage(bitmap);
        values.put("Type", type);
        values.put("Occasion", occasion);
        values.put("Price", price);
        values.put("Image", ImageBytes);
        CarzeniaDatabase = getWritableDatabase();
        CarzeniaDatabase.update("Car", values, "ID = "+ID+";", null);
        CarzeniaDatabase.close();
    }

    public void removeCar(int ID){
        CarzeniaDatabase = getWritableDatabase();
        CarzeniaDatabase.delete("Car", "ID = " + ID, null);
        CarzeniaDatabase.close();
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

    public ArrayList<Integer> getAllCarsID(){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT ID FROM Car", null);
        while(cursor.moveToNext())
            arrayList.add(cursor.getInt(0));

        return arrayList;
    }

    public Bitmap getCarImage(int ID){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Image FROM Car WHERE ID = ?", new String[]{String.valueOf(ID)});
        Bitmap bitmap=null;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            ImageBytes = cursor.getBlob(0);
            if(ImageBytes!=null)
                bitmap = BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.length);
        }
        CarzeniaDatabase.close();
        return bitmap;
    }

    public String getCarType(int ID){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Type FROM Car WHERE ID = ?", new String []{String.valueOf(ID)});
        String type="";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            type = cursor.getString(0);
        }
        CarzeniaDatabase.close();
        return type;
    }

    public String getCarOccasion(int ID){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Occasion FROM Car WHERE ID = ?", new String []{String.valueOf(ID)});
        String occasion="";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            occasion = cursor.getString(0);
        }
        CarzeniaDatabase.close();
        return occasion;
    }

    public String getCarPrice(int ID){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Price From Car WHERE ID = ?", new String[]{String.valueOf(ID)});
        String price = "";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            price = cursor.getString(0);
        }
        CarzeniaDatabase.close();
        return price;
    }
    /********* END CAR DATA *********/

    /********* START USER DATA *********/
    public void clearUserTable(){
        CarzeniaDatabase=getWritableDatabase();
        CarzeniaDatabase.execSQL("DELETE FROM User");
        CarzeniaDatabase.close();
        Toast.makeText(context, "ALL USERS WERE REMOVED !", Toast.LENGTH_SHORT).show();
    }

    public void addUserImage(String name, Bitmap bitmap){
        if(isAvailableUsername(name))
            Toast.makeText(context, "Error Loading The Image...", Toast.LENGTH_SHORT).show();
        else{
            processImage(bitmap);
            ContentValues value = new ContentValues();
            value.put("Image", ImageBytes);
            CarzeniaDatabase = getWritableDatabase();
            CarzeniaDatabase.update("User", value, "Name = '"+name+"';", null);
            CarzeniaDatabase.close();
        }
    }

    public void addUserData(String name, String mail, String phone, String pass, String type){
        ContentValues row = new ContentValues();
        row.put("Name", name);
        row.put("Mail", mail);
        row.put("Phone", phone);
        row.put("Password", pass);
        row.put("Type", type);
        row.put("Image", "null");
        CarzeniaDatabase = getWritableDatabase();
        CarzeniaDatabase.insert("User", null, row);
        CarzeniaDatabase.close();
    }

    public void updateUserData(String name, String mail, String phone, String pass){
        ContentValues values = new ContentValues();
        values.put("Mail", mail);
        values.put("Phone", phone);
        values.put("Password", pass);
        CarzeniaDatabase = getWritableDatabase();
        CarzeniaDatabase.update("User", values, "Name = '"+name+"';", null);
        CarzeniaDatabase.close();
    }

    public Boolean isAvailableUsername(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT * FROM USER WHERE Name = ?", new String[]{username});
        if(cursor.getCount()>0){ // username exists
            CarzeniaDatabase.close();
            return false;
        }
        else{
            CarzeniaDatabase.close();
            return true;
        }
    }

    public Boolean isSuccessLogin(String username, String password){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT * FROM User WHERE Name = ? and Password = ?", new String[]{username, password});
        if(cursor.getCount()>0){ // there's a user fit to this data
            CarzeniaDatabase.close();
            return true;
        }
        else{
            CarzeniaDatabase.close();
            return false;
        }
    }

    public String getUserType(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Type FROM User WHERE Name = ?", new String []{username});
        cursor.moveToFirst();
        String type = cursor.getString(0);
        CarzeniaDatabase.close();
        return type;
    }

    public Bitmap getUserImage(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Image FROM User WHERE Name = ?", new String []{username});

        cursor.moveToFirst();
        ImageBytes = cursor.getBlob(0);
        Bitmap bitmap=null;
        if(ImageBytes!=null)
          bitmap = BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.length);
        CarzeniaDatabase.close();
        return bitmap;
    }

    public String getUserMail(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Mail FROM User WHERE Name = ?", new String[]{username});
        cursor.moveToFirst();
        String mail = cursor.getString(0);
        CarzeniaDatabase.close();
        return mail;
    }

    public String getUserPhone(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Phone FROM User WHERE Name = ?", new String[]{username});
        cursor.moveToFirst();
        String phone = cursor.getString(0);
        CarzeniaDatabase.close();
        return phone;
    }

    public String getUserPassword(String username){
        CarzeniaDatabase = getReadableDatabase();
        Cursor cursor = CarzeniaDatabase.rawQuery("SELECT Password FROM User WHERE Name = ?", new String[]{username});
        cursor.moveToFirst();
        String password = cursor.getString(0);
        CarzeniaDatabase.close();
        return password;
    }
    /********* END USER DATA *********/
}
