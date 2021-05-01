package com.example.android.carzenia.AdminUserMessages;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.Adapters.AdminMessagesAdapter;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class AdminResponsesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MessageModel> messagesList;
    private TextView noMessagesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerView = findViewById(R.id.recycler_view_admin_messages);
        noMessagesTextView = findViewById(R.id.text_view_admin_messages_empty);
        noMessagesTextView.setVisibility(View.INVISIBLE);
        setMessages();

            if (!messagesList.isEmpty())
            {
                AdminMessagesAdapter adminMessagesAdapter =
                        new AdminMessagesAdapter(this, noMessagesTextView, messagesList);
                recyclerView.setAdapter(adminMessagesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            else
                noMessagesTextView.setVisibility(View.VISIBLE);
    }

    private void setMessages() {
        messagesList = new ArrayList<>();

        // if the admin have seen the message, then admin can't see it again.
        for (MessageModel message : DBHolder.messagesData)
        {
            if (!message.isSeen())
                messagesList.add(message);
        }
    }

}