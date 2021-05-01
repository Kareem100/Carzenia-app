package com.example.android.carzenia.UserFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.android.carzenia.Adapters.CustomerCarsListAdapter;
import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;

public class DisplayCarsFragment extends Fragment {

    private ListView listView;
    private CustomerCarsListAdapter adapter;
    private final int RELOAD_TIME = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_display_cars, container, false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DBHolder.carsData.isEmpty())
                    Toast.makeText(getContext(), getString(R.string.toast_no_cars_to_show), Toast.LENGTH_SHORT).show();
                else {
                    listView = view.findViewById(R.id.list_view_users_display);
                    adapter = new CustomerCarsListAdapter(getActivity(), R.layout.layout_user_car_item, DBHolder.carsData);
                    listView.setAdapter(adapter);
                }
            }
        }, RELOAD_TIME);


        return view;
    }

}