package com.example.gymtrainer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class workoutItemAdapter  extends RecyclerView.Adapter{

    ArrayList<String> workout_image_urls;
    ArrayList<String> workout_names;
    String workoutsDetailsJsonString;
    Context context;
    //constructor
    public workoutItemAdapter(ArrayList<String> ImgUrl, ArrayList<String> workoutNameText,
                              String workoutsDetailsJsonString_, Context context_)
    {
        this.workout_image_urls = ImgUrl;
        this.workout_names = workoutNameText;
        this.workoutsDetailsJsonString = workoutsDetailsJsonString_;
        this.context = context_;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView image;
        private final TextView text;
        private final LinearLayout linearLayout;

        public ViewHolder(View v)
        {
            super(v);
            image = v.findViewById(R.id.image);
            text = v.findViewById(R.id.text_one);
            linearLayout = v.findViewById(R.id.linearlayout);
        }

        public ImageView getImage(){ return this.image;}
        public void setTextInView(String text){ this.text.setText(text); }
    }

    @NonNull
    @Override
    public workoutItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_workout_list_sample_entry, parent, false);
        return new workoutItemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Glide.with(this.context)
                .load(workout_image_urls.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.all)
                .into(((ViewHolder)holder).getImage());
        ((ViewHolder)holder).setTextInView(workout_names.get(position));
        ((ViewHolder)holder).linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    String workoutName = workout_names.get(holder.getAdapterPosition());
                    Intent intent = new Intent(context, IndividualWorkoutDetailsActivity.class);
                    intent.putExtra("name", workoutName);
                    intent.putExtra("workoutsDetailsJsonString", workoutsDetailsJsonString);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount()
    {
        return workout_image_urls.size();
    }

}