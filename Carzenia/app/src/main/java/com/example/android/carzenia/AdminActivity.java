package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        androidx.cardview.widget.CardView addCard = (androidx.cardview.widget.CardView)findViewById(R.id.AddCarBtn);
        androidx.cardview.widget.CardView removeCard = (androidx.cardview.widget.CardView)findViewById(R.id.RemoveCarBtn);
        androidx.cardview.widget.CardView displayCard = (androidx.cardview.widget.CardView)findViewById(R.id.DisplayCarsBtn);
        androidx.cardview.widget.CardView logoutCard = (androidx.cardview.widget.CardView)findViewById(R.id.LogoutBtn);

        addCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddCarActivity.class));
            }
        });
        removeCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                CarDB helper = new CarDB(AdminActivity.this);
                helper.clearCarTable();
                //helper.onUpgrade(helper.getWritableDatabase(), 1, 2); // To Drop Car Table
            }
        });
        displayCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, DisplayCarsActivity.class));
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

    }
}