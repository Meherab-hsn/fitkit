package com.example.fitkit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nutrition extends BaseActivity {

    EditText editSearch;
    Button btnSearch;
    TextView txtResult;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nutrition);

        // 1. Initialize Views
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        txtResult = findViewById(R.id.txtResult);
        progressBar = findViewById(R.id.progressBar);

        // Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Button Click Logic
        btnSearch.setOnClickListener(v -> {
            String foodName = editSearch.getText().toString().trim();

            // Validation
            if (TextUtils.isEmpty(foodName)) {
                editSearch.setError("Please enter a food name");
                return;
            }

            // Show Loading UI
            progressBar.setVisibility(View.VISIBLE);
            txtResult.setText("Analyzing...");
            btnSearch.setEnabled(false); // Stop double clicking

            // 3. IMPROVED PROMPT: Strictly ask for no stars
            String prompt = "Analyze the nutrition for " + foodName + ". " +
                    "Provide a clean list with: Calories, Protein, Carbs, and Fat. " +
                    "Do NOT use asterisks (*), bolding, or markdown. Just plain text.";

            // 4. Call the API
            GeminiResp.getResponse(prompt, response -> {
                // Ensure this runs on the main UI thread to prevent crashes
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnSearch.setEnabled(true);

                    // 5. CLEANUP: Remove any stars manually if Gemini forgets
                    String cleanResponse = response.replace("*", "").replace("#", "").trim();

                    txtResult.setText(cleanResponse);
                });
            });
        });
    }
}