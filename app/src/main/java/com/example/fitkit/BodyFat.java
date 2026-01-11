package com.example.fitkit;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BodyFat extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_body_fat);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edtheight, edtneck, edtwaist;
        TextView txtresult;
        Button calculate;

        edtheight = findViewById(R.id.editHeight);
        edtneck = findViewById(R.id.editNeck);
        edtwaist = findViewById(R.id.editWaist);
        txtresult = findViewById(R.id.txtCategory);
        calculate = findViewById(R.id.btnCalculate);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ht=  Integer.parseInt( edtheight.getText().toString() );
                int ne=  Integer.parseInt(   edtneck.getText().toString());
                int wa = Integer.parseInt(   edtwaist.getText().toString() );

                double bodyFat = 495 / (1.0324 - 0.19077 * Math.log10(wa - ne) + 0.15456 * Math.log10(ht)) - 450;

                TextView txtBodyFat = findViewById(R.id.txtBodyFat);
                txtBodyFat.setText(String.format("%.1f%%", bodyFat));

                // Determine category
                String category;

                if (bodyFat < 6) {
                    category = "Essential Fat";
                } else if (bodyFat < 14) {
                    category = "Athletes";
                } else if (bodyFat < 18) {
                    category = "Fitness";
                } else if (bodyFat < 25) {
                    category = "Average";
                } else {
                    category = "Obese";
                }

                // Show category in txtresult
                txtresult.setText(category);
            }
        });


    }

}
