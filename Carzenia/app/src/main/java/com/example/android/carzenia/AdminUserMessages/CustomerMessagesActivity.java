package com.example.android.carzenia.AdminUserMessages;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.Adapters.CustomerMessagesAdapter;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CustomerMessagesActivity extends AppCompatActivity {

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
            CustomerMessagesAdapter customerMessagesAdapter = new CustomerMessagesAdapter(this, messagesList);
            recyclerView.setAdapter(customerMessagesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else
            noMessagesTextView.setVisibility(View.VISIBLE);
    }

    private void setMessages () {
        String currentUserMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        messagesList = new ArrayList<>();
        List<MessageModel> tempMsgList = new ArrayList<>();

        // Walking through all the messages that is from the current user
        // sorting the messages so that the seen messages are at the top
        // if seen view the admin response
        // else view no response yet
        for (MessageModel message : DBHolder.messagesData)
        {
            if (message.getFrom().getMail().equals(currentUserMail))
            {
                if (message.isSeen())
                    messagesList.add(message);
                else {
                    message.setResponse(getString(R.string.no_response));
                    tempMsgList.add(message);
                }
            }
        }

        for(MessageModel message : tempMsgList)
            messagesList.add(message);
    }

}