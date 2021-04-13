package com.example.android.carzenia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public static String username = "User123";
    private DBManager DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // FOR USER DATA
        DB = new DBManager(this);
        View header = navigationView.getHeaderView(0);
        ((TextView)header.findViewById(R.id.text_view_nav_username)).setText(username);
        ((TextView)header.findViewById(R.id.text_view_nav_user_mail)).setText(DB.getUserMail(username));
        ImageView image = header.findViewById(R.id.image_view_nav_user_image);
        Bitmap bm = DB.getUserImage(username);
        if(bm!=null)
            image.setImageBitmap(bm);

        // FOR TOGGLE ICON
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#FB8C00"));

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisplayCarsFragment()).commit();
            navigationView.setCheckedItem(R.id.DisplayCarsNav);
        }
    }

    // NAV BAR EVENTS
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.DisplayCarsNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisplayCarsFragment()).commit();
                break;
            case R.id.RentCarNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentFormFragment()).commit();
                break;
            case R.id.ProfileNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserProfileFragment()).commit();
                break;
            case R.id.FeedbackNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment()).commit();
                break;
            case R.id.AboutNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.LogoutNav:
                showAlertDialog();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
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
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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