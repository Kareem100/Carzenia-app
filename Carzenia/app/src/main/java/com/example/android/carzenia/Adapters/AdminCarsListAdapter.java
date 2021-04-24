package com.example.android.carzenia.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.carzenia.AdminActivities.RemoveCarsActivity;
import com.example.android.carzenia.AdminActivities.UpdateCarActivity;
import com.example.android.carzenia.CarsDatabase.CarModel;
import com.example.android.carzenia.R;

import java.util.ArrayList;

public class AdminCarsListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<CarModel> carsList;

    public AdminCarsListAdapter(Context context, int layout, ArrayList<CarModel> carsList) {
        this.context = context;
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
        Button updateBtn, removeBtn;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.image_view_car_item);
            holder.idView = (TextView) row.findViewById(R.id.text_view_car_id_item);
            holder.typeView = (TextView) row.findViewById(R.id.carTypeT);
            holder.occasionView = (TextView) row.findViewById(R.id.text_view_car_occasion_item);
            holder.priceView = (TextView) row.findViewById(R.id.text_view_car_price_item);
            holder.updateBtn = (Button) row.findViewById(R.id.button_car_update_item);
            holder.removeBtn = (Button) row.findViewById(R.id.button_car_remove_item);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        CarModel carModel = carsList.get(position);

        holder.imageView.setImageBitmap(carModel.getBitmap());
        holder.idView.setText(String.valueOf(carModel.getId()));
        holder.typeView.setText(carModel.getType());
        holder.occasionView.setText(carModel.getOccasion());
        holder.priceView.setText(carModel.getPrice()+" LE. Per Hour");
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateCarActivity.class);
                intent.putExtra("CarID", position+1);
                context.startActivity(intent);
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RemoveCarsActivity.class);
                intent.putExtra("CarID", position+1);
                context.startActivity(intent);
            }
        });

        return row;
    }
}
