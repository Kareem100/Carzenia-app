package com.example.android.carzenia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        androidx.cardview.widget.CardView addCard = (androidx.cardview.widget.CardView)findViewById(R.id.AddCarBtn);
        androidx.cardview.widget.CardView removeCard = (androidx.cardview.widget.CardView)findViewById(R.id.RemoveCarBtn);
        androidx.cardview.widget.CardView updateCard = (androidx.cardview.widget.CardView)findViewById(R.id.UpdateCarBtn);
        androidx.cardview.widget.CardView displayCard = (androidx.cardview.widget.CardView)findViewById(R.id.DisplayCarsBtn);
        androidx.cardview.widget.CardView adminCard = (androidx.cardview.widget.CardView)findViewById(R.id.AddAdminBtn);
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
                startActivity(new Intent(AdminActivity.this, RemoveCarsActivity.class));
            }
        });
        updateCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, UpdateCarActivity.class));
            }
        });
        displayCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, DisplayCarsActivity.class));
            }
        });

        adminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddAdminActivity.class));
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }
    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle("ALERT");
        alert.setMessage("Do You Want To Logout ?");
        alert.setIcon(R.drawable.logout_icon);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }
}