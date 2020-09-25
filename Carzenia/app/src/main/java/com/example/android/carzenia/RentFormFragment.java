package com.example.android.carzenia;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RentFormFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    DatePickerDialog.OnDateSetListener d1,d2;
    int dfYear,dfMonth,dfDay,dtYear,dtMonth,dtDay;
    Button DateTo, rent;

    private DBManager DB;
    private Spinner spinner;
    private ImageView image;
    private TextView typeTxt, occasionTxt, priceTxt;
    private ArrayList<Integer> arrayList;
    private ArrayAdapter<Integer> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View classView = inflater.inflate(R.layout.rent_fragment, container, false);

        /**************************/ // INSTANTIATION
        DB = new DBManager(getContext());
        spinner = classView.findViewById(R.id.spinner3);
        image = classView.findViewById(R.id.rentedCarImage);
        typeTxt = classView.findViewById(R.id.rentedCarType);
        occasionTxt = classView.findViewById(R.id.rentedCarOccasion);
        priceTxt = classView.findViewById(R.id.rentedCarPrice);
        arrayList = DB.getAllCarsID();
        arrayAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        /**************************/ // DEFAULT VALUES
        if(arrayList.isEmpty())
            Toast.makeText(getContext(), "Sorry Still No Cars To Be Rented !!", Toast.LENGTH_LONG).show();
        else {
            image.setImageBitmap(DB.getCarImage(arrayList.get(0)));
            typeTxt.setText(DB.getCarType(arrayList.get(0)));
            occasionTxt.setText(DB.getCarOccasion(arrayList.get(0)));
            priceTxt.setText(DB.getCarPrice(arrayList.get(0)));
        }
        /**************************/

        Calendar c = Calendar.getInstance();
        final int CurrentYear =c.get(Calendar.YEAR);
        final int CurrentMonth =c.get(Calendar.MONTH);
        final int CurrentDay =c.get(Calendar.DAY_OF_MONTH);
        rent =(Button) classView.findViewById(R.id.confirmRentBtn);
        DateTo =(Button) classView.findViewById(R.id.dateToBtn);
        Button DateFrom = (Button) classView.findViewById(R.id.dateFromBtn);

        DateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,d2,CurrentYear,CurrentMonth,CurrentDay);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datepicker.show();
            }
        });

        d2=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dfYear=year;dfMonth=month;dfDay=dayOfMonth;
                if(dfYear >=CurrentYear && dfMonth >=CurrentMonth && dfDay >=CurrentDay)
                {
                    SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String Cureentdate = s.format(c.getTime());
                    TextView showselecteddate = (TextView) classView.findViewById(R.id.dateFromTxt);
                    showselecteddate.setText(Cureentdate);
                    DateTo.setVisibility(View.VISIBLE);
                    ((TextView) classView.findViewById(R.id.dateToTxt)).setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getContext(),"Not valid date",Toast.LENGTH_LONG).show();
                }
            }
        };
        DateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,d1,CurrentYear,CurrentMonth,CurrentDay);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datepicker.show();
            }

        });

        d1=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dtYear=year;dtMonth=month;dtDay=dayOfMonth;
                if((dtYear>=dfYear && dtMonth >= dfMonth && dtDay>=dfDay ))
                {
                    if(dtDay==dfDay && dtMonth == dfMonth && dtYear == dfYear)
                    {
                        Toast.makeText(getContext(),"Not valid date",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SimpleDateFormat s=new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String Cureentdate = s.format(c.getTime());
                        TextView showselecteddate = (TextView) classView.findViewById(R.id.dateToTxt);
                        showselecteddate.setText(Cureentdate);
                        TextView price=(TextView) classView.findViewById(R.id.totalCostTxt);
                        int hourCost = 1;
                        if(!priceTxt.getText().toString().isEmpty())
                            hourCost = Integer.parseInt(priceTxt.getText().toString());
                        price.setText(String.valueOf(((dtDay-dfDay)+(dtMonth-dfMonth)*30+(dtYear-dfYear)*365)*24 * hourCost + " L.E"));
                        rent.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Not valid date",Toast.LENGTH_LONG).show();
                }
            }
        };

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String T1 = ((TextView)classView.findViewById(R.id.dateFromTxt)).getText().toString();
                String T2 = ((TextView)classView.findViewById(R.id.dateToTxt)).getText().toString();
                if(T1.isEmpty() || T2.isEmpty())
                    Toast.makeText(getContext(), "Please Specify The Rental Duration !!", Toast.LENGTH_LONG).show();
                else
                    showAlertDialog();
            }
        });

        return classView;
    }

    private void showAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        alert.setTitle("RENTING");
        alert.setMessage("Do You Want To Confirm Your Rental ?");
        alert.setIcon(R.drawable.ic_top_car);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"Rented Successfully",Toast.LENGTH_LONG).show();
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        image.setImageBitmap(DB.getCarImage(arrayList.get(pos)));
        typeTxt.setText(DB.getCarType(arrayList.get(pos)));
        occasionTxt.setText(DB.getCarOccasion(arrayList.get(pos)));
        priceTxt.setText(DB.getCarPrice(arrayList.get(pos)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}