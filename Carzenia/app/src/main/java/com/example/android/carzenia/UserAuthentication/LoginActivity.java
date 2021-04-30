package com.example.android.carzenia.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carzenia.AdminActivities.AdminActivity;
import com.example.android.carzenia.SystemDatabase.DBHolders;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.example.android.carzenia.SystemDatabase.UserType;
import com.example.android.carzenia.UserFragments.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText mailText, passwordText;
    private ImageView logoImageView;
    private TextView loginTextView;
    private Button login, signUp;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseRef;
    private ProgressBar circularProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        makeHooks();
        circularProgress.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail, pass;
                mail = mailText.getText().toString().trim();
                pass = passwordText.getText().toString().trim();
                validateData(mail, pass);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });

    }

    private void makeHooks() {
        mailText = findViewById(R.id.text_input_edit_text_login_username);
        passwordText = findViewById(R.id.text_input_edit_text_login_password);
        login = findViewById(R.id.button_login);
        signUp = findViewById(R.id.button_signup_from_login);
        logoImageView = findViewById(R.id.login_image_view_logo);
        loginTextView = findViewById(R.id.text_view_login);
        circularProgress = findViewById(R.id.progress_login_circular);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference(DBHolders.USERS_DATABASE_INFO_ROOT);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void validateData(String mail, String pass){
    if (mail.equals(""))
        Toast.makeText(LoginActivity.this, R.string.toast_empty_mail, Toast.LENGTH_SHORT).show();
    else if (pass.equals(""))
        Toast.makeText(LoginActivity.this, R.string.toast_empty_pass, Toast.LENGTH_SHORT).show();
    else
        loginUser(mail, pass);
}

    private void loginUser(String mail, String pass) {
        circularProgress.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    databaseRef.child(firebaseAuth.getCurrentUser().getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        UserModel userModel = task.getResult().getValue(UserModel.class);
                                        if (userModel.getType().equals(UserType.CUSTOMER))
                                            startActivity(new Intent(LoginActivity.this,
                                                    HomeActivity.class)); // FOR CUSTOMERS
                                        else
                                            startActivity(new Intent(LoginActivity.this,
                                                    AdminActivity.class)); // FOR ADMINS
                                        finish();
                                    } else {
                                        if (!isNetworkConnected())
                                            Toast.makeText(LoginActivity.this,
                                            getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                                    }
                                    circularProgress.setVisibility(View.INVISIBLE);
                                }
                            });

                    } else {
                        if (!isNetworkConnected()) {
                            Toast.makeText(LoginActivity.this,
                                    R.string.toast_no_network, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    R.string.toast_no_such_user, Toast.LENGTH_SHORT).show();
                        }
                    circularProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
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