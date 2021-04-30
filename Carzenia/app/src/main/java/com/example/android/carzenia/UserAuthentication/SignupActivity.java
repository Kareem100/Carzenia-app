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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.carzenia.SystemDatabase.DBHolders;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.UserType;
import com.example.android.carzenia.UserFragments.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText userName, userMail, userPhone, userPassword, reUserPassword;
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
                name = userName.getText().toString().trim();
                mail = userMail.getText().toString().trim();
                phone = userPhone.getText().toString().trim();
                pass = userPassword.getText().toString().trim();
                rePass = reUserPassword.getText().toString().trim();

                if (isValidData(name, mail, phone, pass, rePass))
                    registerUser(name, mail, phone, pass);
            }
        });
    }

    private void makeHooks() {
        userName = findViewById(R.id.text_input_edit_text_signup_fullname);
        userMail = findViewById(R.id.text_input_edit_text_signup_email);
        userPhone = findViewById(R.id.text_input_edit_text_signup_phone);
        userPassword = findViewById(R.id.text_input_edit_text_signup_pass);
        reUserPassword = findViewById(R.id.text_input_edit_text_signup_confirm_pass);
        circularProgress = findViewById(R.id.progress_circular);
        signUp = findViewById(R.id.button_signup);
        login = findViewById(R.id.button_login_from_signup);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private Boolean isValidData(String name, String mail, String phone, String pass, String rePass) {
        if (name.equals("") || mail.equals("") || phone.equals("") || pass.equals("") || rePass.equals("")) {
            Toast.makeText(this, getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.length() < 3) {
            Toast.makeText(this, getString(R.string.toast_name_validation), Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.length() < 3) {
            Toast.makeText(this, getString(R.string.toast_pass_validation), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!pass.equals(rePass)) {
            Toast.makeText(this, getString(R.string.toast_pass_match), Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() != 11) {
            Toast.makeText(this, getString(R.string.toast_phone_size_validation), Toast.LENGTH_SHORT).show();
            return false;
        } else
            for (int i = 0; i < phone.length(); ++i)
                if (phone.charAt(i) > '9' || phone.charAt(i) < '0') {
                    Toast.makeText(this, getString(R.string.toast_phone_size_validation),Toast.LENGTH_SHORT).show();
                    return false;
                }

        return true;
    }

    private void registerUser(final String userName, final String mail, final String phone, final String pass) {
        circularProgress.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Storing the Additional fields in the firebase Database
                    UserModel userModel = new UserModel(userName, mail, phone, UserType.CUSTOMER, "");
                    FirebaseDatabase.getInstance().getReference(DBHolders.USERS_DATABASE_INFO_ROOT)
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
                                // Check The internet Connection
                                if (!isNetworkConnected()) {
                                    Toast.makeText(SignupActivity.this,
                                            getString(R.string.toast_no_network), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignupActivity.this,
                                            getString(R.string.toast_try_again_later), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                } else {
                    if (!isNetworkConnected()) {
                        Toast.makeText(SignupActivity.this,
                                getString(R.string.toast_no_network), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignupActivity.this,
                                getString(R.string.toast_already_registered_user), Toast.LENGTH_LONG).show();
                    }
                    circularProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}