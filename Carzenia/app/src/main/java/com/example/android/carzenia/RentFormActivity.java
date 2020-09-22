package com.example.android.carzenia;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RentFormActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener d1,d2;
    int dfYear,dfMonth,dfDay,dtYear,dtMonth,dtDay;
    Button DateTo, rent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_form);
        Calendar c = Calendar.getInstance();
        final int CurrentYear =c.get(Calendar.YEAR);
        final int CurrentMonth =c.get(Calendar.MONTH);
        final int CurrentDay =c.get(Calendar.DAY_OF_MONTH);
        rent =(Button) findViewById(R.id.button);
        DateTo =(Button) findViewById(R.id.button2);
        Button DateFrom = (Button) findViewById(R.id.button3);

        DateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(RentFormActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,d2,CurrentYear,CurrentMonth,CurrentDay);
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
                    TextView showselecteddate = (TextView) findViewById(R.id.textView3);
                    showselecteddate.setText(Cureentdate);
                    DateTo.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not valid date",Toast.LENGTH_LONG).show();
                }
            }
        };

        DateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datepicker = new DatePickerDialog(RentFormActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,d1,CurrentYear,CurrentMonth,CurrentDay);
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
                        Toast.makeText(getApplicationContext(),"Not valid date",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        SimpleDateFormat s=new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        String Cureentdate = s.format(c.getTime());
                        TextView showselecteddate = (TextView) findViewById(R.id.textView2);
                        showselecteddate.setText(Cureentdate);
                        TextView price=(TextView) findViewById(R.id.textView6);
                        price.setText(String.valueOf(((dtDay-dfDay)+(dtMonth-dfMonth)*30+(dtYear-dfYear)*365)*24+ " L.E"));
                        rent.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not valid date",Toast.LENGTH_LONG).show();
                }
            }
        };
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Rented successfully",Toast.LENGTH_LONG).show();
            }
        });
    }
}