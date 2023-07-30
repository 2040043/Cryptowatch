package com.cryptowatch;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class CurrencyConverterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextAmount;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private Button buttonConvert;
    private TextView textViewResult;

    private double[] conversionRates = {0.91, 1.09, 1.00}; // Conversion rates: USD to EUR, EUR to USD, and EUR to EUR  //July 11, 2023

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyconverter);

        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTo.setOnItemSelectedListener(this);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        convertCurrency();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    private void convertCurrency() {
        String amountString = editTextAmount.getText().toString();
        if (amountString.isEmpty()) {
            textViewResult.setText("");
            return;
        }

        double amount = Double.parseDouble(amountString);
        double rateFrom = conversionRates[spinnerFrom.getSelectedItemPosition()];
        double rateTo = conversionRates[spinnerTo.getSelectedItemPosition()];

        double convertedAmount = (amount / rateFrom) * rateTo;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String result = decimalFormat.format(convertedAmount);

        textViewResult.setText(result);
    }
}
