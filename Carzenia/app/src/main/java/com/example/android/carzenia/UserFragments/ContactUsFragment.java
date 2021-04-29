package com.example.android.carzenia.UserFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.android.carzenia.SystemDatabase.DBManager;
import com.example.android.carzenia.R;
import com.example.android.carzenia.AdminUserMessages.UserMessagesActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ContactUsFragment extends Fragment {
    private String userMsg;
    private static String userName;
    private TextInputEditText msg;
    private DBManager helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        msg = (TextInputEditText) view.findViewById(R.id.text_input_edit_text_message_body);
        view.findViewById(R.id.button_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userMsg = msg.getText().toString();
                userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                helper = new DBManager(ContactUsFragment.this.getContext());

                helper.InsertMessages(userMsg, userName);
                Toast.makeText(getContext(), "Your Message Has Been Sent !!", Toast.LENGTH_LONG).show();
            }
        });
        view.findViewById(R.id.button_view_responses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper = new DBManager(ContactUsFragment.this.getContext());
                userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                helper.getAnswersForUser(userName);
                startActivity(new Intent(ContactUsFragment.this.getContext(), UserMessagesActivity.class));
            }
        });
        return view;
    }
}