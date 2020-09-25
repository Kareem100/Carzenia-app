package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayCarsActivity extends AppCompatActivity {

    private DBManager helper;
    private GridView gridView;
    private ArrayList<CarModel> carsList;
    private CarListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cars);

        helper = new DBManager(this);
        carsList = helper.getCarsData();
        if(carsList.isEmpty())
            Toast.makeText(this, "No Cars To Show !", Toast.LENGTH_SHORT).show();
        else {
            gridView = (GridView) findViewById(R.id.displayGridView);
            adapter = new CarListAdapter(this, R.layout.car_data, carsList);
            gridView.setAdapter(adapter);
        }
    }
}