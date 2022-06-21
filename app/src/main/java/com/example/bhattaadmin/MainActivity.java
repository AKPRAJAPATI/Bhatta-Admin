package com.example.bhattaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhattaadmin.Adapter.mainAdapter;
import com.example.bhattaadmin.Application.SettingsActivity;
import com.example.bhattaadmin.Application.add_user_keyActivity;
import com.example.bhattaadmin.Models.registerModel;
import com.example.bhattaadmin.authentication.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    /////////////add your layout item data /////////////
    private RecyclerView recyclerView;
    private CircleImageView profileImage;
    private TextView username;
    private TextView userNumber;
    private mainAdapter adapter;
    private String profileimagee;

    private ConstraintLayout userNotFoundLayout;
    private CardView cardView;
    private ImageView hiddenActivity;
    private ArrayList<registerModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hide action bar
        getSupportActionBar().hide();
        //check our users

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        checkUser(firebaseUser); //check our users

        userNotFoundLayout = findViewById(R.id.constraintNotUserFoundLayout);
        cardView = findViewById(R.id.refreshbtn);
        hiddenActivity = findViewById(R.id.addUserBtn);
        hiddenActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), add_user_keyActivity.class);
                intent.putExtra("myImage", profileimagee);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        username = findViewById(R.id.userName);
        userNumber = findViewById(R.id.userNumber);
        profileImage = findViewById(R.id.userProfile);
        //yaha par mai apani profile pic pr click karake bhi setting activity mai jana chahunga or data change karana bhi chahunga
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });


        recyclerView = findViewById(R.id.recyclerviewUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        databaseReference.child("Muneem").child(auth.getUid()).child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    userNotFoundLayout.setVisibility(View.GONE);
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        registerModel model = dataSnapshot.getValue(registerModel.class);
                        arrayList.add(model);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    userNotFoundLayout.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //this is offline database
        databaseReference.keepSynced(true);
        //this is offline database

        adapter = new mainAdapter(getApplicationContext(), arrayList);
        recyclerView.setAdapter(adapter);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });

        //////////////ALL METHODS IS HERE///////////////////////////
        GET_OUR_PROFILE_DATA();

        ///////////////////////////////////////////////////////////////
    }


    private void GET_OUR_PROFILE_DATA() {
        databaseReference.child("Muneem").child(auth.getUid()).child("information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name, number;
                    name = snapshot.child("name").getValue(String.class);
                    number = snapshot.child("phone").getValue(String.class);
                    profileimagee = snapshot.child("imageUrl").getValue(String.class);

                    username.setText(name);
                    userNumber.setText(number);
                    Picasso.get().load(profileimagee).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error is " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }

}