package com.example.gymtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.Color;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView exercisesIcon;
    private ImageView liveIcon;
    private ImageView profileIcon;
    private TextView exercisesIconText;
    private TextView liveIconText;
    private TextView profileIconText;

    private void initialize()
    {
        exercisesIcon = findViewById(R.id.exercisesIcon);
        liveIcon = findViewById(R.id.liveIcon);
        profileIcon = findViewById(R.id.profileIcon);
        exercisesIconText = findViewById(R.id.exercisesIconText);
        liveIconText = findViewById(R.id.liveIconText);
        profileIconText = findViewById(R.id.profileIconText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize all views
        initialize();

        // on click exerciseIcon
        exercisesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveIconColor(exercisesIcon, exercisesIconText,
                                    new ImageView[]{liveIcon, profileIcon},
                                    new TextView[]{liveIconText, profileIconText});
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragmentArea, new ExerciseFragment());
                ft.commit();
            }
        });

        // on click liveIcon
        liveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveIconColor(liveIcon, liveIconText,
                        new ImageView[]{exercisesIcon, profileIcon},
                        new TextView[]{exercisesIconText, profileIconText});
                //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            }
        });

        // on click profileIcon
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveIconColor(profileIcon, profileIconText,
                        new ImageView[]{exercisesIcon, liveIcon},
                        new TextView[]{exercisesIconText, liveIconText});
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFragmentArea, new profileFragment());
                ft.commit();
            }
        });
    }

    private void setActiveIconColor(ImageView iconToChange, TextView textToChange,
                                    ImageView[] iconNotToChange,
                                    TextView[] textNotToChange)
    {
        textToChange.setTextColor(Color.parseColor("#2B547E"));
        iconToChange.setColorFilter(Color.parseColor("#2B547E"));

        for (ImageView icon : iconNotToChange) {icon.setColorFilter(Color.parseColor("#808080"));}
        for (TextView text : textNotToChange) {text.setTextColor(Color.parseColor("#808080"));}
    }

}