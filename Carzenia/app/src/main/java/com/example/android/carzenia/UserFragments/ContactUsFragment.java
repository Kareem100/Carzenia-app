package com.example.android.carzenia.UserFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.R;
import com.example.android.carzenia.AdminUserMessages.CustomerMessagesActivity;
import com.example.android.carzenia.SystemDatabase.MessageModel;
import com.example.android.carzenia.SystemDatabase.MessageType;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUsFragment extends Fragment {

    private RadioGroup typeRadioGroup;
    private RadioButton typeRadioButton;
    private TextInputEditText subjectEditTxt;
    private TextInputEditText bodyEditTxt;
    private ProgressBar circularProgress;
    private Button sendButton;
    private Button viewResponsesButton;
    private View fragmentView;
    private boolean inProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_contact, container, false);

        makeHooks();
        inProgress = false;
        circularProgress.setVisibility(View.INVISIBLE);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!inProgress)
                {
                    int selectedRadioID = typeRadioGroup.getCheckedRadioButtonId();
                    String subject = subjectEditTxt.getText().toString().trim();
                    String body = bodyEditTxt.getText().toString().trim();

                    if (isValidData(selectedRadioID, subject, body)) {
                        typeRadioButton = fragmentView.findViewById(selectedRadioID);
                        MessageType type = MessageType.valueOf(typeRadioButton.getText().toString());
                        addMessageToFirebase(type, subject, body);
                    }
                }
                else
                    Toast.makeText(getContext(), getString(R.string.toast_update_in_progress), Toast.LENGTH_SHORT).show();
            }
        });

        viewResponsesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUsFragment.this.getContext(), CustomerMessagesActivity.class));
            }
        });

        return fragmentView;
    }

    private void makeHooks () {
        typeRadioGroup = fragmentView.findViewById(R.id.radio_group_message_type);
        subjectEditTxt = fragmentView.findViewById(R.id.text_input_edit_text_message_subject);
        bodyEditTxt = fragmentView.findViewById(R.id.text_input_edit_text_message_body);
        circularProgress = fragmentView.findViewById(R.id.progress_contact);
        sendButton = fragmentView.findViewById(R.id.button_send_message);
        viewResponsesButton = fragmentView.findViewById(R.id.button_view_responses);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean isValidData(int selectedRadioID, String subject, String body) {
        if (selectedRadioID == -1) {
            Toast.makeText(getContext(), getString(R.string.toast_choose_message_type), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (subject.equals("")) {
            Toast.makeText(getContext(), getString(R.string.toast_empty_msg_subj), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (body.equals("")) {
            Toast.makeText(getContext(), getString(R.string.toast_empty_msg_body), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addMessageToFirebase (final MessageType type, final String subject, final String body) {
        if (isNetworkConnected())
        {
            inProgress = true;
            circularProgress.setVisibility(View.VISIBLE);
            String uID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(DBHolder.USERS_DATABASE_INFO_ROOT);
            usersRef.child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel from = snapshot.getValue(UserModel.class);

                    DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference(DBHolder.MSSGS_DATABASE_INFO_ROOT);
                    DatabaseReference postMsgRef = msgRef.push();
                    MessageModel messageModel = new MessageModel(postMsgRef.getKey(), from, type, subject, body, false, "");

                    postMsgRef.setValue(messageModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(getContext(), getString(R.string.toast_message_sent), Toast.LENGTH_LONG).show();

                                    else
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    inProgress = false;
                                    circularProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
            Toast.makeText(getContext(), getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
    }

}
