package com.example.android.carzenia.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerMessagesAdapter extends RecyclerView.Adapter<CustomerMessagesAdapter.MyViewHolder> {

    List<MessageModel> messagesList;
    Context context;

    public CustomerMessagesAdapter(Context context, List<MessageModel> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_customer_messages_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.subjectTxt.setText(messagesList.get(position).getSubject());
        holder.messageTxt.setText(messagesList.get(position).getBody());
        holder.responseTxt.setText(messagesList.get(position).getResponse());
        if (holder.responseTxt.getText().toString().equals(context.getString(R.string.no_response)))
            holder.responseTxt.setBackground(context.getDrawable(R.drawable.border));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView subjectTxt;
        TextView messageTxt;
        TextView responseTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectTxt = itemView.findViewById(R.id.text_view_customer_message_subject);
            messageTxt = itemView.findViewById(R.id.text_view_customer_message_content);
            responseTxt = itemView.findViewById(R.id.text_view_customer_message_response);
        }

    }

}
