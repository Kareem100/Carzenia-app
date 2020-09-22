package com.example.android.carzenia;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DisplayCarsFragment extends AppCompatActivity
{
    private CarDB helper;
    private GridView gridView;
    private ArrayList<CarModel> carsList;
    private CarListAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cars);

        helper = new CarDB(this);
        carsList = helper.getCarsData();
        if(carsList.isEmpty())
            Toast.makeText(this, "No Cars To Show !", Toast.LENGTH_SHORT).show();
        else {
            gridView = (GridView) findViewById(R.id.displayGridView);
            adapter = new CarListAdapter2(this, R.layout.car_for_users, carsList);
            gridView.setAdapter(adapter);
        }
        //adapter.notifyDataSetChanged();
    }
}
