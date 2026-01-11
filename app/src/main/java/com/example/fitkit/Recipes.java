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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Recipes extends BaseActivity {

    EditText editSearch;
    Button btnSearch;
    TextView txtRecipeResult;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);

        // 1. Initialize Views
        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        txtRecipeResult = findViewById(R.id.txtRecipeResult); // Make sure XML ID matches this
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

            if (TextUtils.isEmpty(foodName)) {
                editSearch.setError("Enter a food item");
                return;
            }

            // Show Loading
            progressBar.setVisibility(View.VISIBLE);
            txtRecipeResult.setText("Chef is cooking up a recipe...");
            btnSearch.setEnabled(false);

            // 3. Create the Recipe Prompt
            // We specifically ask for NO stars (*) to keep the design clean
            String prompt = "Create a healthy recipe for " + foodName + ". " +
                    "Include: \n1. Ingredients list \n2. Step-by-step Instructions. " +
                    "Do NOT use asterisks (*) or markdown. Keep the format clean and simple.";

            // 4. Call Gemini API
            GeminiResp.getResponse(prompt, response -> {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnSearch.setEnabled(true);

                    // Clean up any stray stars just in case
                    String cleanResponse = response.replace("*", "").replace("#", "").trim();

                    txtRecipeResult.setText(cleanResponse);
                });
            });
        });
    }
}