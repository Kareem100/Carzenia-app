package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class DisplayCarsActivity extends AppCompatActivity {

    private DBManager helper;
    private ListView listView;
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
            listView = findViewById(R.id.list_view_users_display);
            adapter = new CarListAdapter(this, R.layout.cars_list_item, carsList);
            listView.setAdapter(adapter);
        }
    }
}