package com.example.android.carzenia.AdminUserMessages;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.SystemDatabase.DBManager;
import com.example.android.carzenia.Adapters.CustomerMessagesAdapter;
import com.example.android.carzenia.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserMessagesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBManager db;
    ArrayList<String> Messages;
    ArrayList<String> Answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_messages);
        db = new DBManager(this);

        recyclerView = findViewById(R.id.recycler_view_admin_messages);
        db.getAnswersForUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        Messages = db.getMessage1();
        Answers = db.getAnswer1();
        try {
            if (!Messages.isEmpty()) {
                CustomerMessagesAdapter customerMessagesAdapter = new CustomerMessagesAdapter(this, Messages, Answers);
                recyclerView.setAdapter(customerMessagesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else {
                Toast.makeText(this, getString(R.string.toast_no_messages), Toast.LENGTH_LONG);
            }

        } catch (NullPointerException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
        }
    }
}