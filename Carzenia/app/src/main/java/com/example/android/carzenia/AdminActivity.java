package com.example.android.carzenia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {
    private DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        androidx.cardview.widget.CardView addCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_add_car);
        androidx.cardview.widget.CardView removeCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_remove_car);
        androidx.cardview.widget.CardView updateCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_update_car);
        androidx.cardview.widget.CardView displayCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_display_cars);
        androidx.cardview.widget.CardView adminCard = (androidx.cardview.widget.CardView)findViewById(R.id.AddAdminBtn);
        androidx.cardview.widget.CardView logoutCard = (androidx.cardview.widget.CardView)findViewById(R.id.LogoutBtn);
        ImageView viewMessags = (ImageView)findViewById(R.id.image_view_messages);

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
        viewMessags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=new DBManager(AdminActivity.this);
                db.getMessagesForAdmin();
                startActivity(new Intent(AdminActivity.this,AdminAnswersActivity.class));
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