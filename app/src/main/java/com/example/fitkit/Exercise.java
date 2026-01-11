package com.example.fitkit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Exercise extends BaseActivity {

    Spinner spinnerLevel, spinnerGoal, spinnerEquipment;
    EditText editTime;
    Button btnGenerate;
    TextView txtResult;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise);

        // 1. Initialize Views
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerGoal = findViewById(R.id.spinnerGoal);
        spinnerEquipment = findViewById(R.id.spinnerEquipment);
        editTime = findViewById(R.id.editTime);
        btnGenerate = findViewById(R.id.btnGenerate);
        txtResult = findViewById(R.id.txtResult);
        progressBar = findViewById(R.id.progressBar);

        // Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Button Logic
        btnGenerate.setOnClickListener(v -> {
            String time = editTime.getText().toString().trim();

            if (TextUtils.isEmpty(time)) {
                editTime.setError("Required");
                return;
            }

            // Get selected values directly from the Spinners
            String level = spinnerLevel.getSelectedItem().toString();
            String goal = spinnerGoal.getSelectedItem().toString();
            String equipment = spinnerEquipment.getSelectedItem().toString();

            // UI Update
            progressBar.setVisibility(View.VISIBLE);
            txtResult.setText("Designing your workout...");
            btnGenerate.setEnabled(false);

            // 3. Create Prompt for Gemini
            String prompt = "Create a " + time + " minute " + level + " workout routine focused on " + goal + ". " +
                    "Equipment available: " + equipment + ". " +
                    "List exercises with Sets and Reps. " +
                    "Do NOT use asterisks (*) or markdown. Just a clean list.";

            // 4. API Call
            GeminiResp.getResponse(prompt, response -> {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);

                    String cleanResponse = response.replace("*", "").replace("#", "").trim();
                    txtResult.setText(cleanResponse);
                });
            });
        });
    }
}