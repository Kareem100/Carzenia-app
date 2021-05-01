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
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class RemoveCarsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> carsIdList;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner carsIdSpinner;
    private ImageView carImageView;
    private TextView typeTxt, occasionTxt, priceTxt;
    private Button removeOneBtn, removeAllBtn;
    private String selectedID; // zero based
    private StorageReference imgRef;
    private DatabaseReference carsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_cars);

        makeHooks();

        //SETTING ADAPTER & SELECT LISTENER
        carsIdSpinner.setAdapter(spinnerAdapter);
        carsIdSpinner.setOnItemSelectedListener(this);

        // SETTING THE DEFAULT VALUES
        if (carsIdList.isEmpty())
            Toast.makeText(this, getString(R.string.toast_add_cars), Toast.LENGTH_LONG).show();
        else {
            try {
                selectedID = getIntent().getStringExtra("CarID");
            } catch (Exception e) {
                selectedID = "0";
            }
            if (selectedID == null) selectedID = "0";
            carsIdSpinner.setSelection(Integer.valueOf(selectedID));
        }

        // BUTTONS CLICK LISTENERS
        removeOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carsIdList.isEmpty())
                    Toast.makeText(RemoveCarsActivity.this,
                            getString(R.string.toast_no_car_to_remove), Toast.LENGTH_LONG).show();
                else
                    removeCarFromFirebase(selectedID);
            }
        });

        removeAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carsIdList.isEmpty())
                    Toast.makeText(RemoveCarsActivity.this,
                            getString(R.string.toast_no_existing_cars), Toast.LENGTH_SHORT).show();
                else
                    showAlertDialog();
            }
        });
    }

    private void makeHooks() {
        carsIdSpinner = findViewById(R.id.spinner_removed_cars);
        carImageView = findViewById(R.id.image_view_removed_car);
        typeTxt = findViewById(R.id.text_view_removed_car_type);
        occasionTxt = findViewById(R.id.text_view_removed_car_occasion);
        priceTxt = findViewById(R.id.text_view_removed_car_price);
        removeOneBtn = findViewById(R.id.button_remove_selected_car);
        removeAllBtn = findViewById(R.id.button_remove_all_cars);
        setCarsId();
        spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, carsIdList);

        carsRef = FirebaseDatabase.getInstance().getReference(DBHolder.CARS_DATABASE_INFO_ROOT);
        imgRef = FirebaseStorage.getInstance().getReference(DBHolder.CARS_DATABASE_IMG_ROOT);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.alert_title));
        alert.setMessage(getString(R.string.alert_remove_all_cars_body));
        alert.setIcon(R.drawable.ic_top_car);
        alert.setPositiveButton(getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeAllCarsFromFirebase();
            }
        });
        alert.setNegativeButton(getString(R.string.alert_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    private void setCarsId() {
        carsIdList = new ArrayList<>();

        for (int i = 0; i < DBHolder.carsData.size(); ++i)
            carsIdList.add(String.valueOf(i + 1));
    }

    private void removeCarFromFirebase(String carIdx) {
        int removedID = Integer.valueOf(carIdx);
        String carID = DBHolder.carsData.get(removedID).getId();
        carsRef.child(carID).removeValue();
        imgRef.child(carID).delete();
        carsIdList.remove(removedID);
        for (int i = removedID; i < carsIdList.size(); ++i)
            carsIdList.set(i, String.valueOf(i + 1));
        spinnerAdapter.notifyDataSetChanged();

        Toast.makeText(RemoveCarsActivity.this,
                getString(R.string.toast_car_with_id_removed, String.valueOf(removedID + 1)), Toast.LENGTH_LONG).show();

        resetFields();
        if (removedID == 0 && DBHolder.carsData.size() >= 2)
            setUI(removedID + 1);
    }

    private void removeAllCarsFromFirebase() {
        for (int i = 0; i < DBHolder.carsData.size(); ++i)
            imgRef.child(DBHolder.carsData.get(i).getId()).delete();
        carsRef.removeValue();
        carsIdList.clear();
        spinnerAdapter.notifyDataSetChanged();
        resetFields();
        Toast.makeText(RemoveCarsActivity.this,
                getString(R.string.toast_all_cars_removed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long ID) {
        selectedID = String.valueOf(pos);
        try {
            setUI(pos);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(),
                getString(R.string.toast_nothing_selected), Toast.LENGTH_SHORT).show();
    }

    private void setUI(int pos) {
        Picasso.get().load(DBHolder.carsData.get(pos).getImageUri())
                .placeholder(R.mipmap.ic_launcher_round).into(carImageView);
        typeTxt.setText(DBHolder.carsData.get(pos).getType());
        occasionTxt.setText(DBHolder.carsData.get(pos).getOccasion());
        priceTxt.setText(DBHolder.carsData.get(pos).getPrice());
    }

    private void resetFields() {
        carImageView.setImageDrawable(null);
        typeTxt.setText(getString(R.string.car_type));
        occasionTxt.setText(getString(R.string.car_occasion));
        priceTxt.setText(getString(R.string.car_price_per_hour));
        selectedID = "0";
        carsIdSpinner.setSelection(0); // default position
    }

}