package com.example.bhattaadmin.Application;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.bhattaadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class add_user_keyActivity extends AppCompatActivity {
    private TextView userKey;
    private AppCompatButton copyBtn;
    private AppCompatButton shareBtn;
    private CircleImageView userProfile;


    private String userAuthid;
    private String getMyImage;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_key);
        getSupportActionBar().hide();

        getMyImage = getIntent().getStringExtra("myImage");

        userKey = findViewById(R.id.userKeyTextview);
        userKey.setSelected(true);

        copyBtn = findViewById(R.id.copyButton);
        shareBtn = findViewById(R.id.shareButton);
        userProfile = findViewById(R.id.hiddenProfile);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        userAuthid = FirebaseAuth.getInstance().getUid();
        userKey.setText(userAuthid);

        Picasso.get().load(getMyImage).into(userProfile);

//        Button click events
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //copy work in android studio
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); //sabase pahale usaki serviece li
                ClipData clipData = ClipData.newPlainText("Key copied", userKey.getText().toString()); //clipdata mai dal di
                clipboardManager.setPrimaryClip(clipData); //yaha save kar li
                Toast.makeText(add_user_keyActivity.this, "Key copied", Toast.LENGTH_SHORT).show();
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareText = "Hello brother " + "\n" + "i have send user key " + "\n\n " + userKey.getText().toString() + " \n\nDon't share this key any one" + "\n" + " and you will share this key only perticular person " + "\n\nThanks you\n\n" + "\ncopy this text\n\n\n" + userKey.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plane");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(intent);
            }
        });
        
    }

    public void RESET_ALL_USER_DATA(View view) {
        Toast.makeText(this, "fake = Your all date deleted ", Toast.LENGTH_SHORT).show();
    }
}