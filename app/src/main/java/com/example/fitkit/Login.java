package com.example.fitkit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity; // Changed to AppCompatActivity for simplicity
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends BaseActivity { // Ensure this matches your BaseActivity if needed

    EditText editEmail, editPassword;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth == null) mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        // --- 1. Login Logic ---
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Verify email first", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                } else {
                    Toast.makeText(Login.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // --- 2. Sign Up Logic ---
        findViewById(R.id.txtSignUp).setOnClickListener(v ->
                startActivity(new Intent(Login.this, Signup.class))
        );

        // --- 3. FORGOT PASSWORD (SHORT VERSION) ---
        findViewById(R.id.txtForgotPassword).setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                // If box is empty, tell them to fill it
                editEmail.setError("Enter your email here first!");
                Toast.makeText(Login.this, "Enter email in the box above first", Toast.LENGTH_LONG).show();
                return;
            }

            // Send Reset Link
            mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Login.this, "Reset link sent to your email!", Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }
}