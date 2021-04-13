package com.example.android.carzenia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class DisplayCarsFragment extends Fragment
{
    private DBManager helper;
    private ListView listView;
    private ArrayList<CarModel> carsList;
    private CarListAdapter2 adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_display_cars, container, false);

        helper = new DBManager(getContext());
        carsList = helper.getCarsData();
        if(carsList.isEmpty())
            Toast.makeText(getContext(), "No Cars To Show !", Toast.LENGTH_SHORT).show();
        else {
            listView = view.findViewById(R.id.list_view_users_display);
            adapter = new CarListAdapter2(getContext(), R.layout.car_for_users, carsList);
            listView.setAdapter(adapter);
        }
        return view;
    }
}