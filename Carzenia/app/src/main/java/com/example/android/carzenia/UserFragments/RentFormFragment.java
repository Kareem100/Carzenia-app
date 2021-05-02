package com.example.android.carzenia.UserFragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RentFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private int dfYear, dfMonth, dfDay, dtYear, dtMonth, dtDay;
    private DatePickerDialog.OnDateSetListener dateSetListener_1, dateSetListener_2;
    private Button DateTo, DateFrom, rent;
    private Spinner carsIdSpinner;
    private List<String> carsIdList;
    private ArrayAdapter<String> spinnerAdapter;
    private ImageView carImageView;
    private TextView typeTxt, occasionTxt, priceTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View classView = inflater.inflate(R.layout.fragment_rent, container, false);

        /**************************/ // INSTANTIATION
        makeHooks(classView);
        /**************************/ // DEFAULT VALUES
        setDefaults();
        /**************************/

        Calendar c = Calendar.getInstance();
        final int CurrentYear = c.get(Calendar.YEAR);
        final int CurrentMonth = c.get(Calendar.MONTH);
        final int CurrentDay = c.get(Calendar.DAY_OF_MONTH);

        /************ DATA FROM **************/
        DateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener_1, CurrentYear, CurrentMonth, CurrentDay);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        dateSetListener_1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dfYear = year;
                dfMonth = month;
                dfDay = dayOfMonth;
                if (dfYear >= CurrentYear && dfMonth >= CurrentMonth && dfDay >= CurrentDay) {
                    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar calendear = Calendar.getInstance();
                    calendear.set(Calendar.YEAR, year);
                    calendear.set(Calendar.MONTH, month);
                    calendear.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String currentDate = s.format(calendear.getTime());
                    TextView showSelectedDate = classView.findViewById(R.id.text_view_date_from);
                    showSelectedDate.setText(currentDate);
                    DateTo.setVisibility(View.VISIBLE);
                    (classView.findViewById(R.id.text_view_date_to)).setVisibility(View.VISIBLE);
                } else
                    Toast.makeText(getContext(), getString(R.string.toast_not_valid_date), Toast.LENGTH_LONG).show();
            }
        };

        /************ DATA TO **************/
        DateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener_2, CurrentYear, CurrentMonth, CurrentDay);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }

        });

        dateSetListener_2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dtYear = year;
                dtMonth = month;
                dtDay = dayOfMonth;
                if ((dtYear >= dfYear && dtMonth >= dfMonth && dtDay >= dfDay)) {
                    if (dtDay == dfDay && dtMonth == dfMonth && dtYear == dfYear) {
                        Toast.makeText(getContext(), getString(R.string.toast_not_valid_date), Toast.LENGTH_LONG).show();
                    } else {
                        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String currentDate = s.format(c.getTime());
                        TextView showSelectedDate = classView.findViewById(R.id.text_view_date_to);
                        showSelectedDate.setText(currentDate);
                        TextView price = classView.findViewById(R.id.text_view_total_rent_cost);
                        int hourCost = 1;
                        if (!priceTxt.getText().toString().isEmpty() &&
                                !priceTxt.getText().toString().equals(getString(R.string.car_price_per_hour))) {
                            hourCost = Integer.parseInt(priceTxt.getText().toString());
                            price.setText(((dtDay - dfDay) + (dtMonth - dfMonth) * 30
                                    + (dtYear - dfYear) * 365) * 24 * hourCost + " L.E");
                            rent.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_not_valid_date), Toast.LENGTH_LONG).show();
                }
            }
        };

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String T1 = ((TextView) classView.findViewById(R.id.text_view_date_from)).getText().toString();
                String T2 = ((TextView) classView.findViewById(R.id.text_view_date_to)).getText().toString();
                if (T1.isEmpty() || T2.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.toast_add_rental_duration),
                            Toast.LENGTH_LONG).show();
                else
                    showAlertDialog();
            }
        });

        return classView;
    }

    private void makeHooks(View classView) {
        carsIdSpinner = classView.findViewById(R.id.spinner_rent_form);
        carImageView = classView.findViewById(R.id.image_view_rented_car);
        typeTxt = classView.findViewById(R.id.text_view_rented_car_type);
        occasionTxt = classView.findViewById(R.id.text_view_rented_car_occasion);
        priceTxt = classView.findViewById(R.id.text_view_rented_car_price);
        rent = classView.findViewById(R.id.button_confirm_rent);
        DateTo = classView.findViewById(R.id.button_date_to);
        DateFrom = classView.findViewById(R.id.button_date_from);

        setCarsId();
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, carsIdList);
        carsIdSpinner.setAdapter(spinnerAdapter);
        carsIdSpinner.setOnItemSelectedListener(this);
    }

    private void setDefaults() {
        String carIdx = "";
        try {
            carIdx = this.getArguments().getString("CarID");
        } catch (Exception e) {
            carIdx = "-1";
        }

        if (DBHolder.carsData.isEmpty())
            Toast.makeText(getContext(), getString(R.string.toast_no_cars_to_rent), Toast.LENGTH_LONG).show();
        else if (!carIdx.equals("-1"))
            carsIdSpinner.setSelection(Integer.valueOf(carIdx));
        else
            setUI(0);
    }

    private void setCarsId() {
        carsIdList = new ArrayList<>();

        for (int i = 0; i < DBHolder.carsData.size(); ++i)
            carsIdList.add(String.valueOf(i + 1));
    }

    private void setUI(int pos) {
        Picasso.get().load(DBHolder.carsData.get(pos).getImageUri()).into(carImageView);
        typeTxt.setText(DBHolder.carsData.get(pos).getType());
        occasionTxt.setText(DBHolder.carsData.get(pos).getOccasion());
        priceTxt.setText(DBHolder.carsData.get(pos).getPrice());
    }

    private void showAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),
                R.style.AlertDialogCustom));
        alert.setTitle(getString(R.string.alert_title));
        alert.setMessage(getString(R.string.alert_confirm_rental_body));
        alert.setIcon(R.drawable.ic_top_car);
        alert.setPositiveButton(getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), getString(R.string.toast_car_rented), Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton(getString(R.string.alert_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        setUI(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}