package com.example.android.carzenia.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.UserType;
import com.example.android.carzenia.UserFragments.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout userNameInp, userMailInp, userPhoneInp, userPasswordInp, userRePasswordInp;
    private Button signUp, login;
    private ProgressBar circularProgress;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        makeHooks();
        circularProgress.setVisibility(View.INVISIBLE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, mail, phone, pass, rePass;
                name = userNameInp.getEditText().getText().toString().trim();
                mail = userMailInp.getEditText().getText().toString().trim();
                phone = userPhoneInp.getEditText().getText().toString().trim();
                pass = userPasswordInp.getEditText().getText().toString().trim();
                rePass = userRePasswordInp.getEditText().getText().toString().trim();

                if (isValidData(name, mail, phone, pass, rePass))
                    registerUser(name, mail, phone, pass);
            }
        });
    }

    private void makeHooks() {
        userNameInp = findViewById(R.id.text_input_signup_fullname);
        userMailInp = findViewById(R.id.text_input_signup_email);
        userPhoneInp = findViewById(R.id.text_input_signup_phone);
        userPasswordInp = findViewById(R.id.text_input_signup_pass);
        userRePasswordInp = findViewById(R.id.text_input_signup_confirm_pass);
        circularProgress = findViewById(R.id.progress_circular);
        signUp = findViewById(R.id.button_signup);
        login = findViewById(R.id.button_login_from_signup);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean isValidData(String name, String mail, String phone, String pass, String rePass) {
        clearErrors();
        if (name.isEmpty()) {
            userNameInp.setError(getString(R.string.toast_empty_fields));
            userNameInp.requestFocus();
            return false;
        } else if (mail.isEmpty()) {
            userMailInp.setError(getString(R.string.toast_empty_fields));
            userMailInp.requestFocus();
            return false;
        } else if (phone.isEmpty()) {
            userPhoneInp.setError(getString(R.string.toast_empty_fields));
            userPhoneInp.requestFocus();
            return false;
        } else if (pass.isEmpty()) {
            userPasswordInp.setError(getString(R.string.toast_empty_fields));
            userPasswordInp.requestFocus();
            return false;
        } else if (rePass.isEmpty()) {
            userRePasswordInp.setError(getString(R.string.toast_empty_fields));
            userRePasswordInp.requestFocus();
            return false;
        } else if (name.length() < 3) {
            userNameInp.setError(getString(R.string.toast_name_validation));
            userNameInp.requestFocus();
            return false;
        } else if (pass.length() < 3) {
            userPasswordInp.setError(getString(R.string.toast_pass_validation));
            userPasswordInp.requestFocus();
            return false;
        } else if (!pass.equals(rePass)) {
            userRePasswordInp.setError(getString(R.string.toast_pass_match));
            userRePasswordInp.requestFocus();
            return false;
        } else if (phone.length() != 11) {
            userPhoneInp.setError(getString(R.string.toast_phone_size_validation));
            userPhoneInp.requestFocus();
            return false;
        }
        return true;
    }

    private void clearErrors() {
        userNameInp.setError(null);
        userNameInp.setErrorEnabled(false);
        userMailInp.setError(null);
        userMailInp.setErrorEnabled(false);
        userPhoneInp.setError(null);
        userPhoneInp.setErrorEnabled(false);
        userPasswordInp.setError(null);
        userPasswordInp.setErrorEnabled(false);
        userRePasswordInp.setError(null);
        userRePasswordInp.setErrorEnabled(false);
    }

    private void registerUser(final String userName, final String mail, final String phone, final String pass) {
        // Check The internet Connection
        if (!isNetworkConnected()) {
            Toast.makeText(SignupActivity.this,
                    getString(R.string.toast_no_network), Toast.LENGTH_LONG).show();
            return;
        }

        circularProgress.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Storing the Additional fields in the firebase Database
                            UserModel userModel = new UserModel(userName, mail, phone, UserType.CUSTOMER, "");
                            FirebaseDatabase.getInstance().getReference(DBHolder.USERS_DATABASE_INFO_ROOT)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    circularProgress.setVisibility(View.INVISIBLE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this,
                                                getString(R.string.toast_registered_successfully) + " "
                                                        + userName + "!!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                                        finish();
                                    } else {
                                            Toast.makeText(SignupActivity.this,
                                                    getString(R.string.toast_try_again_later), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                            {
                                userMailInp.setError(task.getException().getMessage());
                                userMailInp.requestFocus();
                            }
                            circularProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}