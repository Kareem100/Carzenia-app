package com.example.android.carzenia.AdminUserMessages;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.Adapters.AdminMessagesAnswerAdapter;
import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

import java.util.ArrayList;

public class AdminAnswersActivity extends AppCompatActivity {

    ArrayList<String> username;
    ArrayList<String> messages;
    DBManager db;
    RecyclerView recyclerView;
    Button btn;
    TextView user;
    TextView prob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ancor);
        db = new DBManager(this);

        recyclerView = findViewById(R.id.rec);
        db.getMessagesForAdmin();
        messages = db.getMsg();
        username = db.getUsername();
        btn=findViewById(R.id.button_admin_message_respond);
        try {
            if (!messages.isEmpty()) {
                AdminMessagesAnswerAdapter adminMessagesAnswerAdapter = new AdminMessagesAnswerAdapter(this,messages , username);
                recyclerView.setAdapter(adminMessagesAnswerAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

            } else {
                Toast.makeText(this, "No Messages yet", Toast.LENGTH_LONG);
            }

        } catch (NullPointerException ex) {
            Toast.makeText(this, " Null pointer Exception", Toast.LENGTH_LONG);
        }

    }
}