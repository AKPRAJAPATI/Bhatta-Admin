package com.example.bhattaadmin.authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bhattaadmin.Application.SettingsActivity;
import com.example.bhattaadmin.MainActivity;
import com.example.bhattaadmin.Models.registerModel;
import com.example.bhattaadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    private CircleImageView profileImage;
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private EditText userNumber;
    private Button registerButton;

    private Uri IMAGE_URI;
    private String image_uri;
    private String name, email, password, number;
    private int Image_request_code = 100;
    private int checkSelfPermission_RequestCode = 101;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        //database work reference
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        //////////////METHOD ALL IS HERE/////////////////
        ID_IS_FINDING();
        GET_DATA_FORM_EDITTEXT();
        ////////////////////////////////////////////////////


    }

    private void ID_IS_FINDING() {
        profileImage = findViewById(R.id.userRegisterImage);
        userName = findViewById(R.id.registerName);
        userEmail = findViewById(R.id.registerEmail);
        userPassword = findViewById(R.id.registerPassword);
        userNumber = findViewById(R.id.registerPhoneNumber);
        registerButton = findViewById(R.id.registerButton);

        //button Click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IMAGE_URI == null) {
                    Toast.makeText(getApplicationContext(), "Please Select Your Image", Toast.LENGTH_SHORT).show();
                    openGallery();
                } else if (userName.getText().toString().isEmpty()) {
                    userName.requestFocus();
                    userName.setError("Enter Your name");
                } else if (userEmail.getText().toString().isEmpty()) {
                    userEmail.requestFocus();
                    userEmail.setError("Enter Your Email");
                } else if (!userEmail.getText().toString().contains("@gmail.com")) {
                    userEmail.setError("Add email with @gmail.com");
                } else if (userPassword.getText().toString().isEmpty()) {
                    userPassword.requestFocus();
                    userPassword.setError("Password is empty");
                } else if (userPassword.getText().toString().length() < 8) {
                    userPassword.setError("password like 14kab?kah");
                    userPassword.requestFocus();
                } else if (userNumber.getText().toString().isEmpty()) {
                    userNumber.requestFocus();
                    userNumber.setError("Number is empty");
                } else {
                    showProgressDialog();
                    save_Data_in_Database();
                }
            }
        });
        //Profile Image Click Event
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

    }

    private void requestPermission() {
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //open gallery
                openGallery();


            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(RegisterActivity.this, "Please give me CAMERA permission", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Image_request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_request_code || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
            profileImage.setImageURI(IMAGE_URI);
        }
    }

    private void GET_DATA_FORM_EDITTEXT() {
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        number = userNumber.getText().toString();
    }

    public void GotoLoginActivity(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void save_Data_in_Database() {
        auth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User Registered Success", Toast.LENGTH_SHORT).show();
                    //save image in storage
                    storageReference.child("Profile").child(auth.getUid()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> imageUriLink = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            imageUriLink.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri.toString();
                                    //save our all data in database reference
                                    registerModel registerModel = new registerModel();
                                    registerModel.setImageUrl(image_uri);
                                    registerModel.setName(userName.getText().toString());
                                    registerModel.setEmail(userEmail.getText().toString());
                                    registerModel.setPassword(userPassword.getText().toString());
                                    registerModel.setPhone(userNumber.getText().toString());
                                    registerModel.setUserId(auth.getUid());

                                    databaseReference.child("Muneem").child(auth.getUid()).child("information").setValue(registerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //this is functionality for data
                                                databaseReference.child("Muneem").child(auth.getUid()).child("entes_detail").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (!dataSnapshot.exists()) {
                                                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                                                        } else {
                                                            progressDialog.dismiss();
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(RegisterActivity.this, "Error is "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Image lint is not geted.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "User Registered Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this, R.style.CustomDialog);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Logging in. Please wait.");
        progressDialog.show();
    }
}