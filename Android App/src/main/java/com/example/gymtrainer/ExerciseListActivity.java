package com.example.gymtrainer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {

    private String workoutArea;
    private String difficultyLevel;
    private String equipment;
    private String workoutsDetailsJsonString;

    ArrayList<String> ImgUrl = new ArrayList<>();
    ArrayList<String> workoutNameText = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager Manager;
    workoutItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        // add custom back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // take intent
        Intent intent = getIntent();

        // set workoutArea in spinner
        workoutArea = intent.getStringExtra("workoutArea");
        difficultyLevel = "Beginner";
        equipment = "All";
        workoutsDetailsJsonString = intent.getStringExtra("workoutsDetailsJsonString");

        Spinner workoutAreasSpinner = findViewById(R.id.workoutAreaSpinner);
        for(int i= 0; i < workoutAreasSpinner.getAdapter().getCount(); i++)
        {
            String s = workoutAreasSpinner.getAdapter().getItem(i).toString();
            if(s.equals(workoutArea))
            {
                workoutAreasSpinner.setSelection(i);
                try {
                    fetchWorkouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        // on selecting workout area from spinner
        workoutAreasSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                workoutArea = workoutAreasSpinner.getSelectedItem().toString();
                try {
                    fetchWorkouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // on selecting difficulty level from spinner
        Spinner difficultyLevelSpinner =findViewById(R.id.difficultyLevelSpinner);
        difficultyLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                difficultyLevel = difficultyLevelSpinner.getSelectedItem().toString();
                try {
                    fetchWorkouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // on selecting equipment from spinner
        Spinner equipmentSpinner =findViewById(R.id.equipmentSpinner);
        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
                equipment = equipmentSpinner.getSelectedItem().toString();
                try {
                    fetchWorkouts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void fetchWorkouts() throws JSONException {
        this.recyclerView = findViewById(R.id.recyclerViewExerciseListActivity);
        Manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(Manager);
        ImgUrl.clear();
        workoutNameText.clear();
        UpdateItems(workoutArea, difficultyLevel, equipment);
        adapter = new workoutItemAdapter(ImgUrl, workoutNameText, workoutsDetailsJsonString,this);
        recyclerView.setAdapter(adapter);
    }

    public void UpdateItems(String workoutArea, String difficultyLevel, String equipment) throws JSONException {
        JSONArray workoutsDetails = new JSONArray(workoutsDetailsJsonString);
        for (int i = 0; i < workoutsDetails.length(); i++)
        {
            JSONObject individualWorkoutDetails = (JSONObject)(workoutsDetails.get(i));

            // check difficulty level
            if(individualWorkoutDetails.get("difficultyLevel").toString().equals(difficultyLevel)
                || difficultyLevel.equals("All"))
            {
                JSONArray equipmentsUsed = (JSONArray) individualWorkoutDetails.get("equipments");
                JSONArray muscleGroup = (JSONArray) individualWorkoutDetails.get("workoutArea");

                boolean isEquip = false;
                boolean isMuscle = false;

                // check equipment
                if(equipment.equals("All"))
                    isEquip = true;
                else {
                    for (int j = 0; j < equipmentsUsed.length(); j++) {
                        if (equipmentsUsed.get(j).toString().equals(equipment))
                            isEquip = true;
                    }
                }

                // check muscle group or workoutArea
                if(workoutArea.equals("All"))
                    isMuscle = true;
                else {
                    for (int j = 0; j < muscleGroup.length(); j++) {
                        if (muscleGroup.get(j).toString().equals(workoutArea))
                            isMuscle = true;
                    }
                }

                if(isEquip && isMuscle)
                {
                    ImgUrl.add(individualWorkoutDetails.get("imageUrl").toString());
                    workoutNameText.add(individualWorkoutDetails.get("name").toString());
                }
            }
        }
    }

}