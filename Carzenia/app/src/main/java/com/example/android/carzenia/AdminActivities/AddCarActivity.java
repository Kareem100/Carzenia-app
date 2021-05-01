package com.example.android.carzenia.AdminActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.CarModel;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddCarActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView carImageView;
    private EditText typeInputText, occasionInputText, priceInputText;
    private ProgressBar circularProgress;
    private Uri carImageUri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        makeHooks();
        circularProgress.setVisibility(View.INVISIBLE);

        findViewById(R.id.UploadNewCarBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        findViewById(R.id.button_add_new_car).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (uploadTask != null && uploadTask.isInProgress())
                    Toast.makeText(AddCarActivity.this,
                            getString(R.string.toast_update_in_progress), Toast.LENGTH_SHORT).show();
                else {
                    if (validData())
                        // ADD CAR TO DATABASE
                        addCarToFirebase(carImageUri, typeInputText.getText().toString(),
                                occasionInputText.getText().toString(), priceInputText.getText().toString());
                }
            }
        });

    }

    private void makeHooks() {
        carImageView = findViewById(R.id.image_view_new_car);
        typeInputText = findViewById(R.id.CarTypeInput);
        occasionInputText = findViewById(R.id.CarOccasionInput);
        priceInputText = findViewById(R.id.CarPriceInput);
        circularProgress = findViewById(R.id.progress_add_car);
        databaseReference = FirebaseDatabase.getInstance().getReference(DBHolder.CARS_DATABASE_INFO_ROOT);
        storageReference = FirebaseStorage.getInstance().getReference(DBHolder.CARS_DATABASE_IMG_ROOT);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean validData() {

        if (carImageView.getDrawable() == null) {
            Toast.makeText(this, getString(R.string.toast_enter_car_image), Toast.LENGTH_SHORT).show();
            return false;
        } else if (typeInputText.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_car_type), Toast.LENGTH_SHORT).show();
            return false;
        } else if (occasionInputText.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_car_occasion), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String text = priceInputText.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_enter_car_price), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.toast_pick_image)),
                GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            carImageUri = data.getData();
            Picasso.get().load(carImageUri).into(carImageView);
        } else
            Toast.makeText(this, getString(R.string.toast_no_selected_img), Toast.LENGTH_SHORT).show();
    }

    private void addCarToFirebase(Uri imageUri, final String type, final String occasion, final String price) {
        circularProgress.setVisibility(View.VISIBLE);
        final DatabaseReference postRef = databaseReference.push();
        final String uID = postRef.getKey();
        StorageReference imageRef = storageReference.child(uID);
        uploadTask = imageRef.putFile(imageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> imageTask) {
                        if (imageTask.isSuccessful()) {
                            Task<Uri> uriTask = imageTask.getResult().getMetadata().getReference().getDownloadUrl();
                            uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        CarModel carModel = new CarModel(uID, type,
                                                occasion, price, task.getResult().toString());
                                        postRef.setValue(carModel);
                                        clearFields();
                                        Toast.makeText(AddCarActivity.this,
                                                getString(R.string.toast_enter_car_added), Toast.LENGTH_SHORT).show();
                                    } else if (!isNetworkConnected())
                                        Toast.makeText(AddCarActivity.this,
                                                getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(AddCarActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    circularProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                        } else if (!isNetworkConnected()) {
                            Toast.makeText(AddCarActivity.this,
                                    getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                            circularProgress.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(AddCarActivity.this,
                                    imageTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            circularProgress.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void clearFields() {
        carImageView.setImageDrawable(null);
        typeInputText.setText("");
        occasionInputText.setText("");
        priceInputText.setText("");
    }

}