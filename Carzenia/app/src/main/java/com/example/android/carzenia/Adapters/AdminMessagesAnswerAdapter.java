package com.example.android.carzenia.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.AdminUserMessages.AdminAnswersActivity;
import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

import java.util.ArrayList;

public class AdminMessagesAnswerAdapter extends RecyclerView.Adapter<AdminMessagesAnswerAdapter.MyViewHolder> {

    ArrayList<String> msg , userN;
    Context context;
    DBManager db;
    public AdminMessagesAnswerAdapter(Context ct , ArrayList<String> messages , ArrayList<String> usernames ) {
        msg=messages;
        userN=usernames;
        context=ct;
        db=new DBManager(ct);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_admin_answers , parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.msg.setText(msg.get(position));
        holder.userN.setText(userN.get(position));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.btn.getContext(), AdminAnswersActivity.class);
                AlertDialog.Builder answer = new AlertDialog.Builder(context);
                answer.setTitle("Enter Your Respond");
                final EditText enter = new EditText(context);
                enter.setInputType(InputType.TYPE_CLASS_TEXT);
                answer.setView(enter);

                answer.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.InsertAdminAnswer(enter.getText().toString(),userN.get(position),msg.get(position));
                        Toast.makeText(context,"Your Response was sent Successfully" , Toast.LENGTH_LONG).show();

                    }
                });
                answer.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView msg , userN;
        Button btn;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            msg=(TextView)itemView.findViewById(R.id.text_view_message_content);
            userN=(TextView)itemView.findViewById(R.id.text_view_message_username);
            btn=itemView.findViewById(R.id.button_admin_message_respond);



        }
    }
}
