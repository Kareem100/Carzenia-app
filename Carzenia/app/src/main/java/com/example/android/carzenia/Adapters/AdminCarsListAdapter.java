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
import com.example.android.carzenia.SystemDatabase.CarModel;
import com.example.android.carzenia.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminCarsListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<CarModel> carsList;

    public AdminCarsListAdapter(Context context, int layout, List<CarModel> carsList) {
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

            holder.imageView = row.findViewById(R.id.image_view_car_item);
            holder.idView = row.findViewById(R.id.text_view_car_id_item);
            holder.typeView = row.findViewById(R.id.carTypeT);
            holder.occasionView = row.findViewById(R.id.text_view_car_occasion_item);
            holder.priceView = row.findViewById(R.id.text_view_car_price_item);
            holder.updateBtn = row.findViewById(R.id.button_car_update_item);
            holder.removeBtn = row.findViewById(R.id.button_car_remove_item);

            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        CarModel carModel = carsList.get(position);

        Picasso.get().load(carModel.getImageUri()).placeholder(R.mipmap.ic_launcher_round).into(holder.imageView);
        //holder.idView.setText(String.valueOf(carModel.getId()));
        holder.idView.setText(String.valueOf(position+1));
        holder.typeView.setText(carModel.getType());
        holder.occasionView.setText(carModel.getOccasion());
        holder.priceView.setText(carModel.getPrice()+ " " + context.getString(R.string.price_per_hour));

        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateCarActivity.class);
                intent.putExtra("CarID", String.valueOf(position));
                context.startActivity(intent);
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RemoveCarsActivity.class);
                intent.putExtra("CarID", String.valueOf(position));
                context.startActivity(intent);
            }
        });

        return row;
    }
}
