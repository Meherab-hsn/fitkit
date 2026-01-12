package com.example.fitkit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Water extends BaseActivity {

    double additionalWater = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_water);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editWeight = findViewById(R.id.editWeight);
        Spinner spinnerActivity = findViewById(R.id.spinnerActivity);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        TextView txtWaterIntake = findViewById(R.id.txtWaterIntake);
        TextView txtGlasses = findViewById(R.id.txtGlasses);

        // --- 1. USE BUILT-IN ANDROID LAYOUT (No new file needed) ---
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                this,
                android.R.layout.simple_spinner_item, // Use default Android layout
                getResources().getStringArray(R.array.exercise_options)
        ) {
            // --- 2. CUSTOMIZE THE DESIGN IN JAVA ---
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;

                // Set Design Manually
                tv.setPadding(40, 30, 40, 30); // Add Padding (Left, Top, Right, Bottom)
                tv.setTextSize(16); // Set Font Size
                tv.setBackgroundColor(Color.WHITE); // White Background

                // Logic: Hint is Gray, Options are Black
                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#A0AEC0"));
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;

                // Simple design for the closed box
                tv.setPadding(10, 0, 0, 0);
                tv.setTextSize(15);

                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#A0AEC0"));
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivity.setAdapter(adapter);


        // --- 3. SELECTION LOGIC ---
        spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: additionalWater = 0.0; break;
                    case 1: additionalWater = 0.0; break;
                    case 2: additionalWater = 0.35; break;
                    case 3: additionalWater = 0.70; break;
                    case 4: additionalWater = 1.05; break;
                    case 5: additionalWater = 1.40; break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Calculate Logic
        btnCalculate.setOnClickListener(v -> {
            String weightStr = editWeight.getText().toString();

            if (weightStr.isEmpty()) {
                editWeight.setError("Enter Weight");
                return;
            }
            if (spinnerActivity.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Select Activity Level", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(weightStr);
            double baseWater = weight * 0.033;
            double totalWaterLiters = baseWater + additionalWater;
            int glasses = (int) Math.ceil((totalWaterLiters * 1000) / 250);

            txtWaterIntake.setText(String.format("%.1f L", totalWaterLiters));
            txtGlasses.setText("≈ " + glasses + " glasses (250ml each)");
        });
    }
}