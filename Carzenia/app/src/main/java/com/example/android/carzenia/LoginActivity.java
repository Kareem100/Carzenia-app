package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText name, password;
    private Button login, signUp;
    private DBManager DB;
    protected static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signupTransferBtn);

        DB = new DBManager(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, pass;
                username = name.getText().toString().trim();
                pass = password.getText().toString().trim();

                if(username.equals(""))
                    Toast.makeText(LoginActivity.this, "Please Enter The Username !!", Toast.LENGTH_SHORT).show();
                else if (pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Please Enter The Password !!", Toast.LENGTH_SHORT).show();
                else if (DB.isSuccessLogin(username, pass)){
                    Toast.makeText(LoginActivity.this, "Logged In Successfully As "+ username +" !!", Toast.LENGTH_LONG).show();
                    if(DB.getUserType(username).equals("user")){
                        HomeActivity.username = username;
                        user=username;
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class)); // FOR USERS
                        finish();
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class)); // FOR ADMINS
                        finish();
                    }
                }
                else
                    Toast.makeText(LoginActivity.this, "No Such User Exists !!", Toast.LENGTH_SHORT).show();
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
}