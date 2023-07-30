package com.cryptowatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private static final int RC_IMAGE_PICK = 123;
    private static final String TAG = "EditProfile";

    private ImageView profileImageView;
    private Button changeImageButton;
    private Button changePasswordButton;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        profileImageView = findViewById(R.id.ProfileImage);
        changeImageButton = findViewById(R.id.selectImageButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        // Load current profile image if available
        loadProfileImage();

        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLogin();
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String emailAddress = user.getEmail();

            assert emailAddress != null;
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfileActivity.this,
                                        "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditProfileActivity.this,
                                        "Failed to send password reset email",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(EditProfileActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage() {
        // Load the profile image from Firebase Storage using Picasso (or any other image loading library)
        storageRef.child("profile_images")
                .child(getCurrentUserId())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                        Toast.makeText(EditProfileActivity.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RC_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                uploadProfileImage(imageUri);
            }
        }
    }

    private void uploadProfileImage(Uri imageUri) {
        StorageReference profileImageRef = storageRef.child("profile_images").child(getCurrentUserId());

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload success
                        Toast.makeText(EditProfileActivity.this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                        // Refresh the profile image view
                        loadProfileImage();

                        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                        editor.putString("profileImageUrl", imageUri.toString());
                        editor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        Toast.makeText(EditProfileActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                        // Display default image or placeholder
                        Picasso.get()
                                .load(R.drawable.profileicon) // Use a default image or placeholder
                                .into(profileImageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        // Image loaded successfully
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        // Handle error loading image
                                        Log.e(TAG, "Error loading image: " + e.getMessage());
                                        e.printStackTrace();
                                    }
                                });
                    }
                });
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

    private void navigateToLogin() {
        Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
