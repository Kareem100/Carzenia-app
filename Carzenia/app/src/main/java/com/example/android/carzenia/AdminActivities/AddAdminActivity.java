package com.example.android.carzenia.AdminActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.carzenia.SystemDatabase.DBManager;
import com.example.android.carzenia.R;

public class AddAdminActivity extends AppCompatActivity {

    private DBManager DB;
    private EditText nameTxt, passTxt;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        //INSTANTIATION
        DB = new DBManager(this);
        nameTxt = findViewById(R.id.text_input_edit_text_admin_name);
        passTxt = findViewById(R.id.text_input_edit_text_admin_password);
        button = findViewById(R.id.button_add_admin);

        //CLICK LISTENER
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = nameTxt.getText().toString();
                String pass = passTxt.getText().toString();
                if(validData(name, pass))
                    if(DB.isAvailableUsername(name)) {
                        DB.addUserData(name, "", "", pass, "admin");
                        Toast.makeText(AddAdminActivity.this, "New Admin Has Been Added !!", Toast.LENGTH_LONG).show();
                        nameTxt.setText("");
                        passTxt.setText("");
                    }
            }
        });
    }

    private boolean validData(String name, String pass){
        if(name.length()<3){
            Toast.makeText(this, "Name Must Be At Least Of 3 Characters !!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (pass.length()<3){
            Toast.makeText(this, "Password Must Be At Least Of 3 Characters !!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}