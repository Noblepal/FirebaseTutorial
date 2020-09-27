package com.sample.firebasetutorial.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sample.firebasetutorial.R;
import com.sample.firebasetutorial.models.House;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseView>{

    private List<House> houses;
    private Activity mCtx;

    public HouseAdapter(List<House> houses, Activity mCtx) {
        this.houses = houses;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public HouseView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HouseView(LayoutInflater.from(mCtx).inflate(R.layout.single_house, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HouseView holder, int position) {
        House house = houses.get(position);
        holder.estate.setText(house.getEstate());
        holder.name.setText(house.getName());
        Glide.with(mCtx).load(house.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    public static class HouseView extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView name, estate;

        public HouseView(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_house);
            name = itemView.findViewById(R.id.txt_house_name);
            estate = itemView.findViewById(R.id.txt_house_estate);

        }


    }

}
