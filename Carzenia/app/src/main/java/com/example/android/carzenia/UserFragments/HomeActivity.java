package com.example.android.carzenia.UserFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.example.android.carzenia.UserAuthentication.LoginActivity;
import com.example.android.carzenia.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private UserModel userModel;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        // MAKING HOOKS
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // FOR USER DATA
        databaseRef = FirebaseDatabase.getInstance().getReference(DBHolder.USERS_DATABASE_INFO_ROOT);
        databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModel = snapshot.getValue(UserModel.class);
                        View header = navigationView.getHeaderView(0);
                        ImageView userImageView = header.findViewById(R.id.image_view_nav_user_image);
                        if (userModel.getImageUrl() != UserModel.NO_IMAGE)
                            Picasso.get().load(userModel.getImageUrl())
                                    .placeholder(R.mipmap.ic_launcher_round)
                                    .into(userImageView);
                        ((TextView) header.findViewById(R.id.text_view_nav_username)).setText(userModel.getName());
                        ((TextView) header.findViewById(R.id.text_view_nav_user_mail)).setText(userModel.getMail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // FOR NAVIGATION TOGGLE ICON
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.HighlightColor));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // HANDLING
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DisplayCarsFragment()).commit();
            navigationView.setCheckedItem(R.id.DisplayCarsNav);
        }
    }

    // NAV BAR EVENTS
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.DisplayCarsNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DisplayCarsFragment()).commit();
                break;
            case R.id.RentCarNav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentFormFragment()).commit();
                break;
            case R.id.ProfileNav:
                Bundle bundle = new Bundle();
                bundle.putString("ImageUrl", userModel.getImageUrl());
                bundle.putString("UserName", userModel.getName());
                bundle.putString("UserMail", userModel.getMail());
                bundle.putString("UserPhone", userModel.getPhone());
                bundle.putString("UserPassword", "123456");
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userProfileFragment).commit();
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
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle(R.string.alert_title);
        alert.setMessage(R.string.alert_logout_body);
        alert.setIcon(R.drawable.logout_icon);
        alert.setPositiveButton(R.string.alert_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        alert.setNegativeButton(R.string.alert_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

}