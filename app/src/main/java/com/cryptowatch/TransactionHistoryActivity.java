package com.cryptowatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryActivity extends Activity {

    private ListView transactionListView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_history);

        transactionListView = findViewById(R.id.transactionListView);
        backButton = findViewById(R.id.backButton);

        // Sample transaction data
        List<String> transactions = new ArrayList<>();
        transactions.add("Transaction 1");
        transactions.add("Transaction 2");
        transactions.add("Transaction 3");
        transactions.add("Transaction 4");
        transactions.add("Transaction 5");

        // Create an ArrayAdapter to populate the ListView with transaction data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                transactions
        );

        // Set the adapter to the ListView
        transactionListView.setAdapter(adapter);

        // Set click listener for the ListView items
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTransaction = transactions.get(position);
                Toast.makeText(TransactionHistoryActivity.this, selectedTransaction, Toast.LENGTH_SHORT).show();
            }
        });

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
