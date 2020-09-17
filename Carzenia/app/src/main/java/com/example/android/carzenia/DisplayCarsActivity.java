package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayCarsActivity extends AppCompatActivity {

    private CarDB helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cars);

        helper = new CarDB(this);
        ArrayList<CarModel> arrayList = helper.getCarsData();
        if(arrayList.isEmpty())
            Toast.makeText(this, "No Cars To Show !", Toast.LENGTH_SHORT).show();
        else {
            CarModel model = arrayList.get(arrayList.size()-1);
            ((TextView)findViewById(R.id.test1)).setText(String.valueOf(model.getId()));
            ((TextView)findViewById(R.id.test2)).setText(model.getType());
            ((TextView)findViewById(R.id.test3)).setText(model.getOccasion());
            ((TextView)findViewById(R.id.test4)).setText(model.getPrice());
            ((ImageView)findViewById(R.id.test5)).setImageBitmap(model.getBitmap());
        }
    }
}