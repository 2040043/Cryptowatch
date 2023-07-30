package com.cryptowatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnFrankfurter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFrankfurter = findViewById(R.id.btnFrankfurter);
        btnFrankfurter.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnFrankfurter:
                intent = new Intent(this, FragmentContainerActivity.class);
                intent.putExtra("apiType", "FrankfurterAPI");
                startActivity(intent);
                break;

        }
    }
}