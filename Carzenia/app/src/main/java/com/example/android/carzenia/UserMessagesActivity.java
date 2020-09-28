package com.example.android.carzenia;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserMessagesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBManager db;
    ArrayList<String> Messages;
    ArrayList<String> Answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ancor);
        db = new DBManager(this);

        recyclerView = findViewById(R.id.rec);
        db.getAnswersForUser(LoginActivity.user);
        Messages = db.getMessage1();
        Answers = db.getAnswer1();
        try {
            if (!Messages.isEmpty()) {
                MessagesAdapter messagesAdapter = new MessagesAdapter(this, Messages, Answers);
                recyclerView.setAdapter(messagesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Toast.makeText(this, "No Messages yet", Toast.LENGTH_LONG);
            }

        } catch (NullPointerException ex) {
            Toast.makeText(this, " Null pointer Exception", Toast.LENGTH_LONG);
        }
    }
}