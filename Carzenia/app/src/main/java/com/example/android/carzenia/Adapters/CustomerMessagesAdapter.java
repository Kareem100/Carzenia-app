package com.example.android.carzenia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.R;

import java.util.ArrayList;

public class CustomerMessagesAdapter extends RecyclerView.Adapter<CustomerMessagesAdapter.MyViewHolder> {

    ArrayList<String> msg , ans;
    Context context;
    public CustomerMessagesAdapter(Context ct , ArrayList<String> messages , ArrayList<String> answers ) {
        msg=messages;
        ans=answers;
        context=ct;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_customer_messages, parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.msg.setText(msg.get(position));
        holder.ans.setText(ans.get(position));

    }

    @Override
    public int getItemCount() {
        return msg.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView msg , ans;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg=(TextView)itemView.findViewById(R.id.text_view_from_admin_message_content);
            ans=(TextView)itemView.findViewById(R.id.text_view_admin_name);

        }
    }
}
