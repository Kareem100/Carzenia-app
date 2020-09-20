package com.example.android.carzenia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<CarModel> carsList;

    public CarListAdapter(Context context, int layout, ArrayList<CarModel> carsList) {
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
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.imageView = (ImageView) row.findViewById(R.id.carImageT);
            holder.idView = (TextView) row.findViewById(R.id.carIDT);
            holder.typeView = (TextView) row.findViewById(R.id.carTypeT);
            holder.occasionView = (TextView) row.findViewById(R.id.carOccasionT);
            holder.priceView = (TextView) row.findViewById(R.id.carPriceT);

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


        return row;
    }
}
