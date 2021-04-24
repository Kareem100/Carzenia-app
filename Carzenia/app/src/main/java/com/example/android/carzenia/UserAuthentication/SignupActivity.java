package com.example.android.carzenia.UserAuthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

public class SignupActivity extends AppCompatActivity {

    private EditText username, usermail, userphone, userpassword, reUserPassword;
    private Button signUp, login;
    private DBManager DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.text_input_edit_text_signup_fullname);
        usermail = findViewById(R.id.text_input_edit_text_signup_email);
        userphone = findViewById(R.id.text_input_edit_text_signup_phone);
        userpassword = findViewById(R.id.text_input_edit_text_signup_pass);
        reUserPassword = findViewById(R.id.text_input_edit_text_signup_confirm_pass);
        signUp = findViewById(R.id.button_signup);
        login = findViewById(R.id.button_login_from_signup);

        DB = new DBManager(this);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name, mail, phone, pass, repass;
                name = username.getText().toString().trim();
                mail = usermail.getText().toString().trim();
                phone = userphone.getText().toString().trim();
                pass = userpassword.getText().toString().trim();
                repass = reUserPassword.getText().toString().trim();

                if (goodData(name, mail, phone, pass, repass)){
                    if(!DB.isAvailableUsername(name))
                        Toast.makeText(SignupActivity.this,
                                "A User With The Same Name Is Already Registered !!",
                                Toast.LENGTH_LONG).show();
                    else {
                        DB.addUserData(name, mail, phone, pass, "user");
                        Toast.makeText(SignupActivity.this, "Registered Successfully As "
                                + name + "!!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
                    }

                }
            }
        });
    }

    private Boolean goodData(String name, String mail, String phone, String pass, String rePass){
        if (name.equals("") || mail.equals("") || phone.equals("") || pass.equals("") || rePass.equals("")){
            Toast.makeText(this, "Please Fill The Empty Fields !!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (name.length() < 3){
            Toast.makeText(this, "Name Must Be At Least Of 3 Characters !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (pass.length() < 3){
            Toast.makeText(this, "Password Must Be At Least Of 3 Characters !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!pass.equals(rePass)){
            Toast.makeText(this, "The Passwords Don't Match.. !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (phone.length()!=11){
            Toast.makeText(this, "Phone Number Must Consist Of 11 Digits !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            for (int i = 0; i < phone.length(); ++i)
                if(phone.charAt(i) > '9' || phone.charAt(i) < '0'){
                    Toast.makeText(this, "Phone Number Must Consist Of Only Numbers !!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}