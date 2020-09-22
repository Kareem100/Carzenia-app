package com.example.android.carzenia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ContactFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_fragment);

        findViewById(R.id.sendMessageBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(ContactFragment.this, "Your Message Has Been Sent !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}