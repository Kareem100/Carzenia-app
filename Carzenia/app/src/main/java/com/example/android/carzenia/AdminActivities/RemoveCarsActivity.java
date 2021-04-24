package com.example.android.carzenia.AdminActivities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

import java.util.ArrayList;

public class RemoveCarsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DBManager DB;
    private ArrayList<Integer> arrayList;
    private ArrayAdapter<Integer> arrayAdapter;
    private Spinner spinner;
    private ImageView image;
    private TextView typeTxt, occasionTxt, priceTxt;
    private Button removeOneBtn, removeAllBtn;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_cars);

        // INSTANTIATION
        spinner = findViewById(R.id.spinner_removed_cars);
        image = findViewById(R.id.image_view_removed_car);
        typeTxt = findViewById(R.id.text_view_removed_car_type);
        occasionTxt = findViewById(R.id.text_view_removed_car_occasion);
        priceTxt = findViewById(R.id.text_view_removed_car_price);
        removeOneBtn = findViewById(R.id.button_remove_selected_car);
        removeAllBtn = findViewById(R.id.button_remove_all_cars);
        DB = new DBManager(this);
        arrayList = DB.getAllCarsID();
        arrayAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, arrayList);

        //SETTING ADAPTER & SELECT LISTENER
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        // SETTING THE DEFAULT VALUES
        if(arrayList.isEmpty())
            Toast.makeText(this, "Please Add Some Cars First !!", Toast.LENGTH_LONG).show();
        else {
            selectedID = getIntent().getIntExtra("CarID", 1);
            spinner.setSelection(selectedID-1);
        }

        // BUTTONS CLICK LISTENERS
        removeOneBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(arrayList.isEmpty())
                    Toast.makeText(RemoveCarsActivity.this, "There's No Car To Remove !!",
                            Toast.LENGTH_LONG).show();
                else {
                    DB.removeCar(selectedID);
                    image.setImageDrawable(null);
                    typeTxt.setText("Car Type");
                    occasionTxt.setText("Car Occasion");
                    priceTxt.setText("Car Price/Hour");
                    Toast.makeText(RemoveCarsActivity.this, "Car With ID: "
                            +selectedID+" Has Been Removed", Toast.LENGTH_LONG).show();
                }
            }
        });

        removeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.isEmpty())
                    Toast.makeText(RemoveCarsActivity.this, "There's No Existing Cars !!",
                            Toast.LENGTH_SHORT).show();
                else
                    showAlertDialog();
            }
        });
    }

    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom));
        alert.setTitle("ALERT");
        alert.setMessage("Are You Sure ?\nDo You Want To Remove All The Existing Cars ?");
        alert.setIcon(R.drawable.ic_top_car);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DB.clearCarTable();
                image.setImageDrawable(null);
                typeTxt.setText("Car Type");
                occasionTxt.setText("Car Occasion");
                priceTxt.setText("Car Price/Hour");
                Toast.makeText(RemoveCarsActivity.this, "All Cars Have Been Removed !!",
                        Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long ID) {
        image.setImageBitmap(DB.getCarImage(arrayList.get(pos)));
        typeTxt.setText(DB.getCarType(arrayList.get(pos)));
        occasionTxt.setText(DB.getCarOccasion(arrayList.get(pos)));
        priceTxt.setText(DB.getCarPrice(arrayList.get(pos)));
        selectedID=arrayList.get(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "NOTHING SELECTED !!", Toast.LENGTH_SHORT).show();
    }
}