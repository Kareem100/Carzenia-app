package com.example.android.carzenia.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.MessageModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminMessagesAdapter extends RecyclerView.Adapter<AdminMessagesAdapter.MyViewHolder> {

    private Context context;
    private List<MessageModel> messagesList;
    private TextView noMessagesTextView;

    public AdminMessagesAdapter(Context context, TextView noMessagesTextView, List<MessageModel> messagesList) {
        this.context = context;
        this.noMessagesTextView = noMessagesTextView;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_admin_messages_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.cstMailText.setText(messagesList.get(position).getFrom().getMail());
        holder.subjectText.setText(messagesList.get(position).getSubject());
        holder.bodyText.setText(messagesList.get(position).getBody());

        holder.respondButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                makeAlertDialog(position);
            }
        });

        holder.showCstInfoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showCstInfo(messagesList.get(position).getFrom().getName(),
                        messagesList.get(position).getFrom().getMail(),
                        messagesList.get(position).getFrom().getPhone());
            }
        });

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void makeAlertDialog(final int pos) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        dialog.setTitle(context.getString(R.string.alert_messages_title));
        final EditText enteredText = new EditText(context);
        enteredText.setTextColor(context.getColor(R.color.FontColor));
        enteredText.setHint(context.getString(R.string.alert_write_response_here));
        enteredText.setHintTextColor(context.getColor(R.color.HighlightColor));
        dialog.setView(enteredText);

        dialog.setPositiveButton(context.getString(R.string.alert_message_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ************************ SAVE ADMIN RESPONSE ************************ //
                String response = enteredText.getText().toString();
                addResponseToFirebase(pos, response);
                messagesList.remove(pos);
                notifyDataSetChanged();
                if (messagesList.isEmpty())
                    noMessagesTextView.setVisibility(View.VISIBLE);
            }
        });

        dialog.setNegativeButton(context.getString(R.string.alert_message_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void addResponseToFirebase(int position, String response) {
        String ID = messagesList.get(position).getID();
        if (!isNetworkConnected())
            Toast.makeText(context, context.getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
        else if (!response.equals(""))
        {
            DatabaseReference msgRef = FirebaseDatabase.getInstance().getReference(DBHolder.MSSGS_DATABASE_INFO_ROOT);
            msgRef.child(ID).child("seen").setValue(true);
            msgRef.child(ID).child("response").setValue(response);
            Toast.makeText(context, context.getText(R.string.toast_sent_response), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCstInfo(String name, String mail, String phone) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        dialog.setTitle(context.getString(R.string.alert_cst_info_title));

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(8, 8, 8, 8);
        final TextView nameText = new TextView(context);
        nameText.setTextColor(context.getColor(R.color.HighlightColor));
        nameText.setText(name);
        final TextView mailText = new TextView(context);
        mailText.setTextColor(context.getColor(R.color.FontColor));
        mailText.setText(mail);
        final TextView phoneText = new TextView(context);
        phoneText.setTextColor(context.getColor(R.color.FontColor));
        phoneText.setText(phone);

        linearLayout.addView(nameText);
        linearLayout.addView(mailText);
        linearLayout.addView(phoneText);

        dialog.setView(linearLayout);

        dialog.setPositiveButton(context.getString(R.string.alert_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).create().show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cstMailText;
        TextView subjectText;
        TextView bodyText;
        Button respondButton;
        Button showCstInfoButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cstMailText = itemView.findViewById(R.id.text_view_message_from_mail);
            subjectText = itemView.findViewById(R.id.text_view_admin_message_subject);
            bodyText = itemView.findViewById(R.id.text_view_message_content);
            respondButton = itemView.findViewById(R.id.button_admin_message_respond);
            showCstInfoButton = itemView.findViewById(R.id.button_admin_message_show_cst);
        }

    }

}
