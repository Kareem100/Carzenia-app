package com.example.android.carzenia.UserFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.carzenia.Adapters.UserCarsListAdapter;
import com.example.android.carzenia.CarsDatabase.CarModel;
import com.example.android.carzenia.CarsDatabase.DBManager;
import com.example.android.carzenia.R;

import java.util.ArrayList;

public class DisplayCarsFragment extends Fragment
{
    private DBManager helper;
    private ListView listView;
    private ArrayList<CarModel> carsList;
    private UserCarsListAdapter adapter;

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
            adapter = new UserCarsListAdapter(getActivity(), R.layout.layout_user_car_item, carsList);
            listView.setAdapter(adapter);
        }
        return view;
    }
}