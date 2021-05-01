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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class UpdateCarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> carsIdList;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner carsIdSpinner;
    private ImageView carImageView;
    private EditText typeTxt, occasionTxt, priceTxt;
    private Button saveChangesButton;
    private ProgressBar circularProgress;
    private String selectedID;
    private Uri carImageUri;
    private static int GALLERY_REQUEST_CODE = 1;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        // INSTANTIATIONS
        makeHooks();
        circularProgress.setVisibility(View.INVISIBLE);

        //SETTING ADAPTER & SELECT LISTENER
        carsIdSpinner.setAdapter(spinnerAdapter);
        carsIdSpinner.setOnItemSelectedListener(this);

        // SETTING THE DEFAULT VALUES
        if (carsIdList.isEmpty())
            Toast.makeText(this, getString(R.string.toast_add_cars), Toast.LENGTH_SHORT).show();
        else {
            try {
                selectedID = getIntent().getStringExtra("CarID");
            } catch (Exception e) {
                selectedID = "0";
            }
            if (selectedID == null) selectedID = "0";
            carsIdSpinner.setSelection(Integer.valueOf(selectedID));
        }

        // SAVE BUTTON & IMAGE CLICK LISTENER
        carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DBHolder.carsData.isEmpty())
                    Toast.makeText(UpdateCarActivity.this,
                            getString(R.string.toast_no_car_to_update), Toast.LENGTH_SHORT).show();
                else
                {
                    String type, occasion, price;
                    type = typeTxt.getText().toString();
                    occasion = occasionTxt.getText().toString();
                    price = priceTxt.getText().toString();

                    if (validData(type, occasion, price))
                        if (uploadTask!=null && uploadTask.isInProgress())
                            Toast.makeText(UpdateCarActivity.this,
                                    getString(R.string.toast_update_in_progress), Toast.LENGTH_SHORT).show();
                        else
                            updateCarData(type, occasion, price);
                }
            }
        });

    }

    private void makeHooks() {
        carsIdSpinner = findViewById(R.id.spinner_update_car);
        carImageView = findViewById(R.id.image_view_updated_car);
        typeTxt = findViewById(R.id.text_input_edit_text_car_type);
        occasionTxt = findViewById(R.id.text_input_edit_text_car_occasion);
        priceTxt = findViewById(R.id.text_input_edit_text_car_price);
        saveChangesButton = findViewById(R.id.saveUpdatedCarBtn);
        circularProgress = findViewById(R.id.progress_update_car);
        setCarsId();
        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, carsIdList);

        databaseReference = FirebaseDatabase.getInstance().getReference(DBHolder.CARS_DATABASE_INFO_ROOT);
        storageReference = FirebaseStorage.getInstance().getReference(DBHolder.CARS_DATABASE_IMG_ROOT);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean validData(String type, String occasion, String price) {
        if (carImageView.getDrawable() == null) {
            Toast.makeText(this, getString(R.string.toast_enter_car_image), Toast.LENGTH_SHORT).show();
            return false;
        } else if (type.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_car_type), Toast.LENGTH_SHORT).show();
            return false;
        } else if (occasion.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_enter_car_occasion), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (price.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_enter_car_price), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void setCarsId() {
        carsIdList = new ArrayList<>();

        for (int i = 0; i < DBHolder.carsData.size(); ++i)
            carsIdList.add(String.valueOf(i + 1));
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.toast_pick_image)), GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            carImageUri = data.getData();
            Picasso.get().load(carImageUri).into(carImageView);
        }
        else
            Toast.makeText(this, getString(R.string.toast_no_selected_img), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        selectedID = String.valueOf(pos);
        try {
            Picasso.get().load(DBHolder.carsData.get(pos).getImageUri())
                    .placeholder(R.mipmap.ic_launcher_round).into(carImageView);
            typeTxt.setText(DBHolder.carsData.get(pos).getType());
            occasionTxt.setText(DBHolder.carsData.get(pos).getOccasion());
            priceTxt.setText(DBHolder.carsData.get(pos).getPrice());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(),
                getString(R.string.toast_nothing_selected), Toast.LENGTH_SHORT).show();
    }

    private void updateCarData(final String type, final String occasion, final String price) {
        circularProgress.setVisibility(View.VISIBLE);
        final int carIdx = Integer.valueOf(selectedID);
        final String carID = DBHolder.carsData.get(carIdx).getId();

        if (carImageUri != null)
        {
            storageReference.child(carID).delete();
            StorageReference imageRef = storageReference.child(carID);
            uploadTask = imageRef.putFile(carImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> imageTask)
                        {
                            if (imageTask.isSuccessful())
                            {
                                Task<Uri> uriTask = imageTask.getResult().getMetadata().getReference().getDownloadUrl();
                                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task)
                                    {
                                        if (task.isSuccessful())
                                            updateDatabase(carIdx, carID, type, occasion, price, task.getResult().toString());
                                        else if (!isNetworkConnected())
                                            Toast.makeText(UpdateCarActivity.this,
                                                    getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(UpdateCarActivity.this,
                                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        circularProgress.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if (!isNetworkConnected()) {
                                Toast.makeText(UpdateCarActivity.this,
                                        getString(R.string.toast_no_network), Toast.LENGTH_SHORT).show();
                                circularProgress.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(UpdateCarActivity.this,
                                        imageTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                circularProgress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }

        else
        {
            String imageUri = DBHolder.carsData.get(carIdx).getImageUri();
            updateDatabase(carIdx, carID, type, occasion, price, imageUri);
            circularProgress.setVisibility(View.INVISIBLE);
        }

    }

    private void updateDatabase(int carIdx, String carID, String type, String occasion,
                                    String price, String imageUri) {
        CarModel carModel = new CarModel(carID, type, occasion, price, imageUri);
        databaseReference.child(carID).setValue(carModel);
        Toast.makeText(UpdateCarActivity.this,
                getString(R.string.toast_car_with_id_updated,
                        String.valueOf(carIdx+1)), Toast.LENGTH_SHORT).show();
        carImageUri = null;
    }

}