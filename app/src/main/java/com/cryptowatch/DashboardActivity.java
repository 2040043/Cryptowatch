package com.cryptowatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cryptowatch.app.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvAccountBalance;
    private final double accountBalance = 500.00; // Initial account balance

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "AeFFdxJH-JPJna1tVvdT1tGC5m7bVC2sVQavGLxp44hY352wAoH94RlJIV2bIaedaJWJsG-aYgCqT0qr"; // Replace with your PayPal Client ID

    private static PayPalConfiguration config;
    private static final int REQUEST_CODE_EDIT_PROFILE = 2;

    private DatabaseReference userRef;
    private EditText editTextDepositAmount;
    private EditText editTextWithdrawalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase database reference
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        tvAccountBalance = findViewById(R.id.tvAccountBalance);
        editTextDepositAmount = findViewById(R.id.editTextDepositAmount);
        editTextWithdrawalAmount = findViewById(R.id.editTextWithdrawalAmount);
        Button btnDeposit = findViewById(R.id.btnDeposit);
        Button btnWithdraw = findViewById(R.id.btnWithdraw);
        Button btnTransactions = findViewById(R.id.btnTransactions);

        updateAccountBalance();

        // Set up PayPal configuration
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID);

        Intent paypalServiceIntent = new Intent(this, PayPalService.class);
        paypalServiceIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(paypalServiceIntent);

        btnDeposit.setOnClickListener(view -> {
            // Get the deposit amount from user input
            double depositAmount = Double.parseDouble(editTextDepositAmount.getText().toString());

            // Check if the account balance is less than or equal to ₱1000
            PayPalPayment payment = new PayPalPayment(new BigDecimal(depositAmount), "PHP", "Deposit", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent paymentIntent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
            paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            paymentIntent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(paymentIntent, REQUEST_CODE_PAYMENT);
        });

        btnWithdraw.setOnClickListener(view -> {
            // Get the withdrawal amount from user input
            double withdrawalAmount = Double.parseDouble(editTextWithdrawalAmount.getText().toString());

            // Check if the account balance is greater than or equal to ₱1000
            if (accountBalance >= withdrawalAmount) {
                PayPalPayment payment = new PayPalPayment(new BigDecimal(withdrawalAmount), "PHP", "Withdrawal", PayPalPayment.PAYMENT_INTENT_SALE);
                Intent paymentIntent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
                paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                paymentIntent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(paymentIntent, REQUEST_CODE_PAYMENT);
            } else {
                Toast.makeText(this, "Withdrawal limit exceeded", Toast.LENGTH_SHORT).show();
            }
        });

        btnTransactions.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, TransactionHistoryActivity.class);
            startActivity(intent);
        });

        // ImageButton click listeners
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        ImageButton imageButton3 = findViewById(R.id.imageButton3);

        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        ImageButton profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(view -> {
            // Handle the click event for the profile image
            Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE);
        });

        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
        });

        retrieveProfileImage();
    }

    private void updateAccountBalance() {
        tvAccountBalance.setText("Account Balance: ₱" + accountBalance);
    }

    private void viewTransactions() {
        Toast.makeText(this, "View Transactions", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                // Handle successful payment
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                // You may want to update the account balance here based on the successful payment
                updateAccountBalance();
            } else if (resultCode == RESULT_CANCELED) {
                // Handle canceled payment
                Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                // Invalid payment or configuration
                Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_EDIT_PROFILE) {
            if (resultCode == RESULT_OK) {
                // Retrieve the profile image URL from the data intent
                String profileImageUrl = data.getStringExtra("profileImageUrl");

                // Update the profile image with the retrieved URL
                if (profileImageUrl != null) {
                    ImageButton profileImage = findViewById(R.id.profileImage);
                    // Use an image loading library like Picasso or Glide to load the image into the ImageView
                    // For example, using Picasso:
                    Picasso.get().load(profileImageUrl).into(profileImage);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Profile edit canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void retrieveProfileImage() {
        // Get the current user's ID
        String currentUserId = getCurrentUserId();

        if (currentUserId != null) {
            // Retrieve the profile image URL from Firebase Database
            userRef.child(currentUserId).child("profileImageUrl")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String imageUrl = dataSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                updateProfileIcon(imageUrl);
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("profileImageUrl", imageUrl);
                                editor.apply(); // Use apply() instead of commit()
                            }
                            Log.d("ImageUrl", "Retrieved image URL: " + imageUrl);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur during data retrieval
                            Toast.makeText(DashboardActivity.this, "Failed to retrieve profile image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            // User is not logged in or authentication is not set up
            // Handle this case according to your app's requirements
            return null;
        }
    }

    private void updateProfileIcon(String imageUrl) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUrl", imageUrl);
        editor.apply(); // Add the missing closing parenthesis for apply()
    }
}
