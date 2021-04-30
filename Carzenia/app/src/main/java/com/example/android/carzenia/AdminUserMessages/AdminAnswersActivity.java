package com.example.android.carzenia.AdminUserMessages;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.Adapters.AdminMessagesAdapter;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolders;

public class AdminAnswersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_messages);

        recyclerView = findViewById(R.id.recycler_view_admin_messages);
            if (!DBHolders.messagesData.isEmpty())
            {
                AdminMessagesAdapter adminMessagesAdapter =
                        new AdminMessagesAdapter(this, DBHolders.messagesData);
                recyclerView.setAdapter(adminMessagesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            else
                Toast.makeText(this, getString(R.string.toast_no_messages), Toast.LENGTH_LONG).show();
    }
}