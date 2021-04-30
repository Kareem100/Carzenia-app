package com.example.android.carzenia.AdminActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;
import com.example.android.carzenia.AdminUserMessages.AdminAnswersActivity;
import com.example.android.carzenia.UserAuthentication.LoginActivity;
import com.example.android.carzenia.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        androidx.cardview.widget.CardView addCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_add_car);
        androidx.cardview.widget.CardView removeCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_remove_car);
        androidx.cardview.widget.CardView updateCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_update_car);
        androidx.cardview.widget.CardView displayCard = (androidx.cardview.widget.CardView)findViewById(R.id.card_view_display_cars);
        androidx.cardview.widget.CardView adminCard = (androidx.cardview.widget.CardView)findViewById(R.id.AddAdminBtn);
        androidx.cardview.widget.CardView logoutCard = (androidx.cardview.widget.CardView)findViewById(R.id.LogoutBtn);
        ImageView viewMessages = (ImageView)findViewById(R.id.image_view_messages);

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
        viewMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminAnswersActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.alert_title));
        alert.setMessage(getString(R.string.alert_logout_body));
        alert.setIcon(R.drawable.logout_icon);
        alert.setPositiveButton(getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.alert_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

}