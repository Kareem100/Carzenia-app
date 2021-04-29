package com.example.android.carzenia.AdminActivities;

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

import com.example.android.carzenia.R;
import com.example.android.carzenia.UserAuthentication.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AddAdminActivity extends AppCompatActivity {

    private EditText nameTxt, passTxt;
    private Button button;
    private ProgressBar circularProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        //INSTANTIATION
        nameTxt = findViewById(R.id.text_input_edit_text_admin_mail);
        passTxt = findViewById(R.id.text_input_edit_text_admin_password);
        button = findViewById(R.id.button_add_admin);
        circularProgress = findViewById(R.id.progress_add_admin);
        circularProgress.setVisibility(View.INVISIBLE);

        //CLICK LISTENER
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = nameTxt.getText().toString();
                String pass = passTxt.getText().toString();
                if (validData(mail, pass))
                    addAdminToFirebase (mail, pass);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean validData(String mail, String pass) {
        if (mail.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_empty_mail), Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_empty_pass), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addAdminToFirebase(String mail, String pass) {
        circularProgress.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(AddAdminActivity.this,
                            getString(R.string.toast_new_admin_added), Toast.LENGTH_LONG).show();
                    resetFields();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AddAdminActivity.this, LoginActivity.class));
                }
                else if (!isNetworkConnected())
                    Toast.makeText(AddAdminActivity.this,
                            getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AddAdminActivity.this,
                            task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                circularProgress.setVisibility(View.INVISIBLE);
            }
        });



    }

    private void resetFields() {
        nameTxt.setText("");
        passTxt.setText("");
    }
}