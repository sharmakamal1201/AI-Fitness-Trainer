package com.example.gymtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IndividualWorkoutDetailsActivity extends AppCompatActivity {

    boolean isStepsDropdownIconExpanded;
    boolean isCautionsDropdownIconExpanded;
    boolean isTagsDropdownIconExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_workout_details);

        ImageView backButton = findViewById(R.id.back_to_exercises_list);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String workoutName = intent.getStringExtra("name");
        String workoutsDetailsJsonString = intent.getStringExtra("workoutsDetailsJsonString");
        showWorkoutDetails(workoutName, workoutsDetailsJsonString);
    }

    public void showWorkoutDetails(String workoutName, String workoutsDetailsJsonString)
    {
        isCautionsDropdownIconExpanded = false;
        isStepsDropdownIconExpanded = false;
        isTagsDropdownIconExpanded = false;


        try {
            JSONArray allWorkoutDetails = new JSONArray(workoutsDetailsJsonString);
            for (int i = 0; i < allWorkoutDetails.length(); i++)
            {
                JSONObject individualWorkoutDetails = (JSONObject) (allWorkoutDetails.get(i));
                if (individualWorkoutDetails.get("name").toString().equals(workoutName)) {
                    String GIFUrl = individualWorkoutDetails.get("GIFUrl").toString();
                    String steps = individualWorkoutDetails.get("steps").toString();
                    String cautions = individualWorkoutDetails.get("cautions").toString();
                    String targetArea = individualWorkoutDetails.get("workoutArea").toString();
                    String difficultyLevel = individualWorkoutDetails.get("difficultyLevel").toString();
                    String EquipmentRequired = individualWorkoutDetails.get("equipments").toString();
                    String tags = "Target Area(s): " + targetArea + "\nDifficulty Level: " + difficultyLevel +
                            "\nEquipment(s) Required: " + EquipmentRequired;
                    showWorkoutDetailsUtils(workoutName, GIFUrl, steps, cautions, tags);
                    break;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showWorkoutDetailsUtils(String workoutName, String GIFUrl, String steps, String cautions, String tags)
    {
        ImageView imageView = (ImageView) findViewById(R.id.image_individual_workout);
        Glide.with(getApplicationContext()).load(GIFUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.all)
                .into(imageView);

        TextView textWorkoutArea = (TextView) findViewById(R.id.workoutName);
        textWorkoutArea.setText(workoutName);


        TextView text = (TextView) findViewById(R.id.steps_individual_workout);
        ImageButton stepsDropdown = (ImageButton) findViewById(R.id.expandBtnSteps);
        stepsDropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        text.setText("Steps");
        TextView stepsText = text;
        stepsDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStepsDropdownIconExpanded = !isStepsDropdownIconExpanded;
                stepsDropdown.setImageResource(isStepsDropdownIconExpanded
                        ? R.drawable.ic_baseline_arrow_drop_up_24
                        : R.drawable.ic_baseline_arrow_drop_down_24);
                stepsText.setText(isStepsDropdownIconExpanded ? "Steps\n\n"+steps : "Steps");
            }
        });

        text = (TextView) findViewById(R.id.cautions_individual_workout);
        ImageButton cautionsDropdown = (ImageButton) findViewById(R.id.expandBtnCautions);
        cautionsDropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        text.setText("Cautions");
        TextView cautionsText = text;
        cautionsDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCautionsDropdownIconExpanded = !isCautionsDropdownIconExpanded;
                cautionsDropdown.setImageResource(isCautionsDropdownIconExpanded
                        ? R.drawable.ic_baseline_arrow_drop_up_24
                        : R.drawable.ic_baseline_arrow_drop_down_24);
                cautionsText.setText(isCautionsDropdownIconExpanded ? "Cautions\n\n"+cautions : "Cautions");
            }
        });

        text = (TextView) findViewById(R.id.tags_individual_workout);
        ImageButton tagsDropdown = (ImageButton) findViewById(R.id.expandBtnTags);
        tagsDropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        text.setText("Tags");
        TextView tagsText = text;
        tagsDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTagsDropdownIconExpanded = !isTagsDropdownIconExpanded;
                tagsDropdown.setImageResource(isTagsDropdownIconExpanded
                        ? R.drawable.ic_baseline_arrow_drop_up_24
                        : R.drawable.ic_baseline_arrow_drop_down_24);
                tagsText.setText(isTagsDropdownIconExpanded ? "Tags\n\n"+tags : "Tags");
            }
        });
    }
}