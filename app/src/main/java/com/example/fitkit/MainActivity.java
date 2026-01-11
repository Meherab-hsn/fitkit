package com.example.fitkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // <--- NEW: Changed from Button to ImageView
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

// <--- NEW: Import Firebase classes
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // <--- NEW: Declare Firebase Auth
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // <--- NEW: Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // <--- NEW: Check if user is logged in. If not, kick them to Login screen
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }

        // <--- NEW: Logout Button Logic
        ImageView btnLogout = findViewById(R.id.btnLogout); // Find the icon
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish(); // Close MainActivity so they can't go back
            }
        });

        // --- Your Existing Card Click Listeners Below ---

        LinearLayout cardMetabolic = findViewById(R.id.cardMetabolic);
        cardMetabolic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Metabolic.class);
                startActivity(intent);
            }
        });

        LinearLayout cardBodyFat = findViewById(R.id.cardBodyFat);
        cardBodyFat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BodyFat.class);
                startActivity(intent);
            }
        });

        LinearLayout cardRecipe = findViewById(R.id.cardRecipe);
        cardRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recipes.class);
                startActivity(intent);
            }
        });

        LinearLayout cardCalorie = findViewById(R.id.cardCalorie);
        cardCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nutrition.class);
                startActivity(intent);
            }
        });

        LinearLayout cardExercise = findViewById(R.id.cardExercise);
        cardExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Exercise.class);
                startActivity(intent);
            }
        });

        LinearLayout cardWater = findViewById(R.id.cardWater);
        cardWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Water.class);
                startActivity(intent);
            }
        });


    }
}