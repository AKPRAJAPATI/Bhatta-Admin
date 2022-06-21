package com.example.bhattaadmin.Application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bhattaadmin.Models.entsDetail;
import com.example.bhattaadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private EditText rate_of_bharai;
    private EditText cuts_of_pachhisa;
    private Button updateButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        rate_of_bharai = findViewById(R.id.rate_bharai);
        cuts_of_pachhisa = findViewById(R.id.cutsOfPachhisa);
        updateButton = findViewById(R.id.updateEnts);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (TextUtils.isEmpty(rate_of_bharai.getText().toString())){
                  rate_of_bharai.setError("Enter your bharai rate");
                  rate_of_bharai.requestFocus();
              }else if (TextUtils.isEmpty(cuts_of_pachhisa.getText().toString())){
                  cuts_of_pachhisa.setError("1000 par katane wali ents");
                  cuts_of_pachhisa.requestFocus();
              }else{
                  entsDetail entsDetail = new entsDetail();

                  int bharaiRate;
                  int kataneWaliEnts;
                  bharaiRate = Integer.parseInt(rate_of_bharai.getText().toString());
                  kataneWaliEnts = Integer.parseInt(cuts_of_pachhisa.getText().toString());

                  entsDetail.setCuts_of_pachhisa(kataneWaliEnts);
                  entsDetail.setRate_of_bharai(bharaiRate);

                  databaseReference.child("Muneem").child(auth.getUid()).child("entes_detail").setValue(entsDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              SharedPreferences sharedPreferences = getSharedPreferences("ents_data", MODE_PRIVATE);
                              SharedPreferences.Editor editor = sharedPreferences.edit();

                              editor.putString("bharai_rate", rate_of_bharai.getText().toString());
                              editor.putString("cuts_of_pachisa", cuts_of_pachhisa.getText().toString());
                              editor.apply();

                              Toast.makeText(SettingsActivity.this, "Your rate Fix", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
              }
            }
        });

        GET_MY_DATA();

    }

    private void GET_MY_DATA() {
        SharedPreferences sharedPreferences = getSharedPreferences("ents_data", MODE_PRIVATE);
        String BHARAI_RATE = sharedPreferences.getString("bharai_rate","");
        String CUTS_OF_PACHHISA = sharedPreferences.getString("cuts_of_pachisa","");
        rate_of_bharai.setText(BHARAI_RATE);
        cuts_of_pachhisa.setText(CUTS_OF_PACHHISA);
    }


}