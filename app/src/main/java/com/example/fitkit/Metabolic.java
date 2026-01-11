package com.example.fitkit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Metabolic extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metabolic);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editAge, editWeight, editHeight;
        RadioGroup radioGroupGender;
        RadioButton radioMale;
        TextView txtBMI, txtBMR, txtMetabolicAge, txtBMICategory;
        Button btnCalculate;

        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        editHeight = findViewById(R.id.editHeight);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        txtBMI = findViewById(R.id.txtBMI);
        txtBMR = findViewById(R.id.txtBMR);
        txtMetabolicAge = findViewById(R.id.txtMetabolicAge);
        txtBMICategory = findViewById(R.id.txtBMICategory);
        btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int age = Integer.parseInt(editAge.getText().toString());
                double weight = Double.parseDouble(editWeight.getText().toString());
                double height = Double.parseDouble(editHeight.getText().toString());

                boolean isMale = radioMale.isChecked();

                // Calculate BMI
                double heightInMeters = height / 100;
                double bmi = weight / (heightInMeters * heightInMeters);
                txtBMI.setText(String.format("%.1f", bmi));

                // BMI Category
                String bmiCategory;
                if (bmi < 18.5) {
                    bmiCategory = "Underweight";
                } else if (bmi < 25) {
                    bmiCategory = "Normal";
                } else if (bmi < 30) {
                    bmiCategory = "Overweight";
                } else {
                    bmiCategory = "Obese";
                }
                txtBMICategory.setText(bmiCategory);

                // Calculate BMR (Mifflin-St Jeor Formula)
                double bmr;
                if (isMale) {
                    bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
                } else {
                    bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
                }
                txtBMR.setText(String.format("%.0f", bmr));

                // Calculate Metabolic Age
                double avgBMR;
                if (isMale) {
                    avgBMR = 1662;
                } else {
                    avgBMR = 1493;
                }

                int metabolicAge = (int) (30 + ((avgBMR - bmr) / 10));
                txtMetabolicAge.setText(String.valueOf(metabolicAge));
            }
        });
    }
}