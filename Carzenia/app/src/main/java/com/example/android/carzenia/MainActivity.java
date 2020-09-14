package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // VARIABLES
    Animation topAnimation, bottomAnimation;
    ImageView logoImage;
    TextView titleView, rightsView, descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Assignments
        logoImage = findViewById(R.id.logoImage);
        titleView = findViewById(R.id.titleView);
        rightsView = findViewById(R.id.rightsView);
        descriptionView = findViewById(R.id.descriptionView);

        //Linkage
        logoImage.setAnimation(topAnimation);
        titleView.setAnimation(bottomAnimation);
        rightsView.setAnimation(bottomAnimation);
        descriptionView.setAnimation(topAnimation);

        // Activity Transition
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, 8000);
    }
}