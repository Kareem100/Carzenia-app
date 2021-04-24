package com.example.android.carzenia.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carzenia.AdminActivities.AdminActivity;
import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;
import com.example.android.carzenia.UserFragments.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText name, password;
    private ImageView logoImageView;
    private TextView loginTextView;
    private Button login, signUp;
    private DBManager DB;
    public static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        makeHooks();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, pass;
                username = name.getText().toString().trim();
                pass = password.getText().toString().trim();

                if(username.equals(""))
                    Toast.makeText(LoginActivity.this, "Please Enter The Username !!",
                            Toast.LENGTH_SHORT).show();
                else if (pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please Enter The Password !!",
                            Toast.LENGTH_SHORT).show();
                else if (DB.isSuccessLogin(username, pass)){
                    Toast.makeText(LoginActivity.this, "Logged In Successfully As "
                            + username +" !!", Toast.LENGTH_LONG).show();
                    if(DB.getUserType(username).equals("user")){
                        HomeActivity.username = username;
                        user=username;
                        startActivity(new Intent(LoginActivity.this,
                                HomeActivity.class)); // FOR USERS
                    }
                    else
                        startActivity(new Intent(LoginActivity.this,
                                AdminActivity.class)); // FOR ADMINS
                    finish();
                }
                else
                    Toast.makeText(LoginActivity.this, "No Such User Exists !!",
                            Toast.LENGTH_SHORT).show();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    private void makeHooks() {
        name = findViewById(R.id.text_input_edit_text_login_username);
        password = findViewById(R.id.text_input_edit_text_login_password);
        login = findViewById(R.id.button_login);
        signUp = findViewById(R.id.button_signup_from_login);
        logoImageView = findViewById(R.id.login_image_view_logo);
        loginTextView = findViewById(R.id.text_view_login);
        DB = new DBManager(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logoImageView.setVisibility(View.INVISIBLE);
        loginTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logoImageView.setVisibility(View.VISIBLE);
        loginTextView.setVisibility(View.VISIBLE);
    }
}