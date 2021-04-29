package com.example.android.carzenia.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carzenia.AdminActivities.AdminActivity;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.CarModel;
import com.example.android.carzenia.SystemDatabase.DBHolders;
import com.example.android.carzenia.SystemDatabase.UserType;
import com.example.android.carzenia.UserFragments.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class SplashActivity extends AppCompatActivity {

    private Animation topAnimation, bottomAnimation;
    private ImageView logoImage;
    private TextView titleView, rightsView, descriptionView;
    private final int TRANSITION_DELAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Assignments
        logoImage = findViewById(R.id.splash_image_view_logo);
        titleView = findViewById(R.id.titleView);
        rightsView = findViewById(R.id.rightsView);
        descriptionView = findViewById(R.id.descriptionView);

        //Linkage
        logoImage.setAnimation(topAnimation);
        titleView.setAnimation(bottomAnimation);
        rightsView.setAnimation(bottomAnimation);
        descriptionView.setAnimation(topAnimation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null)
        {
            // Handle The Logged in User
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                    .child(DBHolders.USERS_DATABASE_INFO_ROOT)
                    .child(firebaseAuth.getCurrentUser().getUid()).child("type");
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    UserType userType = snapshot.getValue(UserType.class);
                    setExhibition();
                    manageLoggedInUser(userType);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    toLoginActivity();
                }
            });
        }

        else {
            setExhibition();
            toLoginActivity();
        }

    }

    private void setExhibition () {
        DatabaseReference carsRef = FirebaseDatabase.getInstance()
                .getReference(DBHolders.CARS_DATABASE_INFO_ROOT);
        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DBHolders.carsData.clear();
                for(DataSnapshot carObject : snapshot.getChildren()) {
                    CarModel carModel = carObject.getValue(CarModel.class);
                    DBHolders.carsData.add(carModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manageLoggedInUser(UserType userType) {
        if (userType == UserType.Admin)
            startActivity(new Intent(SplashActivity.this, AdminActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        else
            startActivity(new Intent(SplashActivity.this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

    private void toLoginActivity() {
        // Activity Transition
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logoImage, "logo_image");
                pairs[1] = new Pair<View, String>(titleView, "logo_text");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.
                            makeSceneTransitionAnimation(SplashActivity.this, pairs);
                    startActivity(new Intent(SplashActivity.this,
                            LoginActivity.class), options.toBundle());
                    finish();
                }
            }
        }, TRANSITION_DELAY_TIME);
    }

}