package com.example.gymtrainer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


public class ExerciseFragment extends Fragment{


    private static String workoutsDetailsJsonString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadWorkoutCategories(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ArrayList<ImageView> workoutAreasImageViews = initializeWorkoutAreasImageViews(view);
        for(int i=0; i<workoutAreasImageViews.size(); i++)
        {
            workoutAreasImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View workoutAreaView) {
                    Intent intent = new Intent(getContext(), ExerciseListActivity.class);
                    String area = workoutAreaView.getTag().toString();
                    intent.putExtra("workoutArea", area);
                    intent.putExtra("workoutsDetailsJsonString", workoutsDetailsJsonString);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    public static void loadWorkoutCategories(Context context)
    {
        String workoutsDetailsFile = "https://raw.githubusercontent.com/sharmakamal1201/GymTrainer/main/workoutsDetailsLists.json";
        StringRequest request = new StringRequest(workoutsDetailsFile,
                response -> workoutsDetailsJsonString = response,
                error -> { });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    public ArrayList<ImageView> initializeWorkoutAreasImageViews(View view)
    {
        ArrayList<ImageView> workoutAreasImageViews = new ArrayList<>();
        workoutAreasImageViews.add(view.findViewById(R.id.bicepsIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.chestIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.tricepsIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.coreIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.forearmsIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.backIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.upperlegsIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.glutesIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.calvesIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.shoulderIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.cardioIcon));
        workoutAreasImageViews.add(view.findViewById(R.id.allIcon));
        return  workoutAreasImageViews;
    }

}