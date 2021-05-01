package com.example.android.carzenia.UserAuthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPasswordActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private TextInputLayout mailTxt;
    private Button nextButton;
    private ProgressBar circularProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        makeHooks();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });

    }

    private void makeHooks() {
        countryCodePicker = findViewById(R.id.country_code_picker);
        mailTxt = findViewById(R.id.edit_text_forget_password_mail);
        nextButton = findViewById(R.id.button_forget_password_next);
        circularProgress = findViewById(R.id.progress_forget_password);

        circularProgress.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        countryCodePicker.setAnimation(animation);
        mailTxt.setAnimation(animation);
        nextButton.setAnimation(animation);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void verifyEmail() {
        if (isNetworkConnected())
        {
            final String mail = mailTxt.getEditText().getText().toString().trim();
            if (!mail.isEmpty())
            {
                circularProgress.setVisibility(View.VISIBLE);
                Query userQuery = FirebaseDatabase.getInstance()
                        .getReference(DBHolder.USERS_DATABASE_INFO_ROOT).orderByChild("mail").equalTo(mail);
                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                mailTxt.setError(null);
                                mailTxt.setErrorEnabled(false);
                                FirebaseAuth.getInstance().sendPasswordResetEmail(mail);
                                Toast.makeText(ForgetPasswordActivity.this,
                                        getString(R.string.toast_we_send_email_to_you),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                mailTxt.setError(getString(R.string.toast_no_such_user));
                                mailTxt.requestFocus();
                            }
                        circularProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ForgetPasswordActivity.this,
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                mailTxt.setError(getString(R.string.enter_mail));
                mailTxt.requestFocus();
            }
        }
        else
            Toast.makeText(this, getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
    }

}