package com.example.android.carzenia.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.android.carzenia.SystemDatabase.CarModel;
import com.example.android.carzenia.R;
import com.example.android.carzenia.UserFragments.RentFormFragment;

import java.util.ArrayList;

public class UserCarsListAdapter extends BaseAdapter {

    private FragmentActivity activity;
    private Context context;
    private int layout;
    private ArrayList<CarModel> carsList;

    public UserCarsListAdapter(FragmentActivity activity, int layout, ArrayList<CarModel> carsList) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.layout = layout;
        this.carsList = carsList;
    }

    @Override
    public int getCount() {
        return carsList.size();
    }

    @Override
    public Object getItem(int position) {
        return carsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView idView, typeView, occasionView, priceView;
        Button rentButton;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.image_view_users_car_image_item);
            holder.idView = (TextView) row.findViewById(R.id.text_view_users_car_id_item);
            holder.typeView = (TextView) row.findViewById(R.id.text_view_users_car_type_item);
            holder.occasionView = (TextView) row.findViewById(R.id.text_view_users_car_occasion_item);
            holder.priceView = (TextView) row.findViewById(R.id.text_view_users_car_price_item);
            holder.rentButton = (Button) row.findViewById(R.id.rentBtn);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        CarModel carModel = carsList.get(position);

        holder.imageView.setImageBitmap(carModel.getBitmap());
        holder.idView.setText("ID: "+String.valueOf(carModel.getId()));
        holder.typeView.setText(carModel.getType());
        holder.occasionView.setText(carModel.getOccasion());
        holder.priceView.setText(carModel.getPrice()+" LE. Per Hour");
        holder.rentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, RentalActivity.class);
                intent.putExtra("CarID", position);
                context.startActivity(intent);*/

                Bundle bundle = new Bundle();
                bundle.putString("CarID", String.valueOf(position));
                RentFormFragment rentFormFragment = new RentFormFragment();
                rentFormFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, rentFormFragment).commit();
            }
        });

        return row;
    }
}
