package com.example.android.carzenia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ContactUsFragment extends Fragment
{
    private String usermsg;
    private static String username;
    private TextInputEditText msg;
    private DBManager helper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);

        msg=(TextInputEditText)view.findViewById(R.id.text_input_edit_text_message_body);
        view.findViewById(R.id.button_send_message).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                usermsg=msg.getText().toString();
                username=LoginActivity.user;
                helper=new DBManager(ContactUsFragment.this.getContext());

                helper.InsertMessages(usermsg,username);
                Toast.makeText(getContext(), "Your Message Has Been Sent !!", Toast.LENGTH_LONG).show();
            }
        });
        view.findViewById(R.id.button_view_responses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper=new DBManager(ContactUsFragment.this.getContext());
                username=LoginActivity.user;
                helper.getAnswersForUser(username);
                startActivity(new Intent(ContactUsFragment.this.getContext(),UserMessagesActivity.class));
            }
        });
        return view;
    }
}