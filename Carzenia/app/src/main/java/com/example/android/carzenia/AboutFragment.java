package com.example.android.carzenia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_app_fragment, container, false);
        view.findViewById(R.id.contactBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (savedInstanceState == null)
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment()).commit();
            }
        });
        return view;
    }
}
