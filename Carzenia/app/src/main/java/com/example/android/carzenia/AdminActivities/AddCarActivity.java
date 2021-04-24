package com.example.android.carzenia.AdminActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

import java.io.IOException;

public class AddCarActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView carImage;
    private Bitmap bitmap;
    private EditText typeInput, occasionInput, priceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        final DBManager helper = new DBManager(this);
        carImage = (ImageView)findViewById(R.id.image_view_new_car);
        typeInput = (EditText)findViewById(R.id.CarTypeInput);
        occasionInput = (EditText)findViewById(R.id.CarOccassionInput);
        priceInput = (EditText)findViewById(R.id.CarPriceInput);

        findViewById(R.id.UploadNewCarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        findViewById(R.id.button_add_new_car).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(validData()){
                    helper.addCarToDB(typeInput.getText().toString(), occasionInput.getText().toString(), priceInput.getText().toString(), bitmap);
                    Toast.makeText(AddCarActivity.this, "Car Added...", Toast.LENGTH_SHORT).show();
                    carImage.setImageDrawable(null);
                    typeInput.setText("");
                    occasionInput.setText("");
                    priceInput.setText("");
                }
            }
        });

    }

    private boolean validData(){
        if(carImage.getDrawable() == null){
            Toast.makeText(this, "Please Add The Car Image !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(typeInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Mention The Car Type !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (occasionInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Mention The Car Occasion !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            String text = priceInput.getText().toString();
            if (text.isEmpty()){
                Toast.makeText(this, "Please Mention The Car Price !", Toast.LENGTH_SHORT).show();
                return false;
            }

            for(int i = 0; i <text.length(); ++i)
                if(text.charAt(i)<'0' || text.charAt(i)>'9'){
                    Toast.makeText(this, "Please Enter The Price Per Hour Only As Numbers !", Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
        return true;
    }
    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick An Image !"), GALLERY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
            } catch (IOException e) { }
            carImage.setImageURI(imageData);
        }
        else Toast.makeText(this, "No New Image Selected !", Toast.LENGTH_SHORT).show();
    }
}