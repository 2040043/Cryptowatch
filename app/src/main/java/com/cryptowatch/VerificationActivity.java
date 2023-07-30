package com.cryptowatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends AppCompatActivity {

    private EditText etVerificationCode;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        etVerificationCode = findViewById(R.id.etVerificationCode);
        btnVerify = findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationCode = etVerificationCode.getText().toString().trim();
                // Perform code verification
                // Add your code verification logic here

                // Example: Display a toast message
                Toast.makeText(VerificationActivity.this, "Verification code: " + verificationCode, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void verifyCode() {
        String verificationCode = etVerificationCode.getText().toString().trim();

        // Perform code verification
        // Add your code verification logic here

        // Example: Display a toast message
        Toast.makeText(VerificationActivity.this, "Verification code: " + verificationCode, Toast.LENGTH_SHORT).show();
    }
}
