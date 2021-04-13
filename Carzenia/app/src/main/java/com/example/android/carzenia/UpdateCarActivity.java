package com.example.android.carzenia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class UpdateCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DBManager DB;
    private ArrayList<Integer> arrayList;
    private ArrayAdapter<Integer> arrayAdapter;
    private Spinner spinner;
    private Bitmap bitmap;
    private ImageView image;
    private EditText typeTxt, occasionTxt, priceTxt;
    private Button button;
    private boolean imageChanged;
    private static int GALLERY_REQUEST_CODE=1;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        // INSTANTIATION
        spinner = findViewById(R.id.spinner_update_car);
        image = findViewById(R.id.image_view_updated_car);
        typeTxt = findViewById(R.id.text_input_edit_text_car_type);
        occasionTxt = findViewById(R.id.text_input_edit_text_car_occasion);
        priceTxt = findViewById(R.id.text_input_edit_text_car_price);
        button = findViewById(R.id.saveUpdatedCarBtn);
        DB = new DBManager(this);
        arrayList = DB.getAllCarsID();
        arrayAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_list_item_1, arrayList);

        //SETTING ADAPTER & SELECT LISTENER
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        // SETTING THE DEFAULT VALUES
        if(arrayList.isEmpty())
            Toast.makeText(this, "Please Add Some Cars First !!",
                    Toast.LENGTH_SHORT).show();
        else {
            selectedID = getIntent().getIntExtra("CarID", 1);
            spinner.setSelection(selectedID-1);
        }

        // SAVE BUTTON & IMAGE CLICK LISTENER
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick An Image !"),
                        GALLERY_REQUEST_CODE);
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(arrayList.isEmpty())
                    Toast.makeText(UpdateCarActivity.this, "No Cars To Update...",
                            Toast.LENGTH_SHORT).show();
                else {
                    if(!imageChanged)bitmap=DB.getCarImage(selectedID);
                    else imageChanged=false;
                    String type, occasion, price;
                    type = typeTxt.getText().toString();
                    occasion = occasionTxt.getText().toString();
                    price = priceTxt.getText().toString();

                    if(validData(type, occasion, price)){
                        DB.updateCarData(selectedID, type, occasion, price, bitmap);
                        Toast.makeText(UpdateCarActivity.this, "Car Data Updated !!",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private boolean validData(String type, String occasion, String price){
        if(image.getDrawable() == null){
            Toast.makeText(this, "Please Add The Car Image !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(type.isEmpty()){
            Toast.makeText(this, "Please Mention The Car Type !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (occasion.isEmpty()){
            Toast.makeText(this, "Please Mention The Car Occasion !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (price.isEmpty()){
                Toast.makeText(this, "Please Mention The Car Price !", Toast.LENGTH_SHORT).show();
                return false;
            }

            for(int i = 0; i <price.length(); ++i)
                if(price.charAt(i)<'0' || price.charAt(i)>'9'){
                    Toast.makeText(this, "Please Enter The Price Per Hour Only As Numbers !",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
                imageChanged=true;
            } catch (IOException e) { }
            image.setImageURI(imageData);
        }
        else Toast.makeText(this, "No New Image Selected !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
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