package com.example.bhattaadmin.Application;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.example.bhattaadmin.Models.add_user_ents_cuts;
import com.example.bhattaadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class addEntsActivity extends AppCompatActivity {

    private TextView userAddName;
    private TextView bharaiRate;
    private TextView cutsOfPachhisa;
    private TextView totalEnts;
    private TextView katiGayiEnts;
    private TextView money;

    private CardView addUpateCardBtn;
    private Button updateBtn;
    private CardView addCardEnts;
    private Button addEntss;

    private EditText addEntsEditText;
    private CircleImageView userProfile;

    private String mera_name;
    private String meri_profile;
    private String userid;
    private Dialog dialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    int mny;
    int totalents;
    int katiEnts;
    int myEnts;
    int rate;
    int cuts_of_pachhisa;
    int katane_wali_ents;
    int total_ents;
    int moneyy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ents);
        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        userid = getIntent().getStringExtra("authid");

        userAddName = findViewById(R.id.userAddName);
        userAddName.setSelected(true);

        bharaiRate = findViewById(R.id.addRateOfBharai);
        cutsOfPachhisa = findViewById(R.id.addCutsOfPchhissa);

        totalEnts = findViewById(R.id.addTotalEnts);
        totalEnts.setSelected(true);

        katiGayiEnts = findViewById(R.id.addKatiGayiEnts);
        katiGayiEnts.setSelected(true);

        money = findViewById(R.id.addMoney);
        money.setSelected(true);
        addEntsEditText = findViewById(R.id.addEntsEditText);

        //this is my add ents button
        addEntss = findViewById(R.id.addEntsButton);
        ///////////////////////////////////////////////////////////////////////
        updateBtn = findViewById(R.id.addEntsUpdateButton);
        addUpateCardBtn = findViewById(R.id.cardUpdateButton);
        addCardEnts = findViewById(R.id.addCardEnts);
        //////////////////////////////////////////////////////////////////
        userProfile = findViewById(R.id.adduserprofile);

        mera_name = getIntent().getStringExtra("mera_name");
        meri_profile = getIntent().getStringExtra("meri_profile");
        userAddName.setText(mera_name);
        Picasso.get().load(meri_profile).into(userProfile);

        GET_OUR_DATA();

        /////////////////BUTTON CLICKED////////////////////////////
        addEntss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addEntsEditText.getText().toString().isEmpty()) {
                    Toast.makeText(addEntsActivity.this, "Ents is empty", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("ents_data", MODE_PRIVATE);
                    String BHARAI_RATE = sharedPreferences.getString("bharai_rate", "");
                    String CUTS_OF_PACHHISA = sharedPreferences.getString("cuts_of_pachisa", "");

                    myEnts = Integer.parseInt(addEntsEditText.getText().toString());

                    rate = Integer.parseInt(BHARAI_RATE); //bharai rate
                    cuts_of_pachhisa = Integer.parseInt(CUTS_OF_PACHHISA); //kati gayi ents

                    katane_wali_ents = myEnts * cuts_of_pachhisa / 1000; //isase hamri katane wali ents aayegi 4000 par 100 ents katengi
                    total_ents = myEnts - katane_wali_ents; // yaha par hamari total ents aa gayi // 3900
                    moneyy = total_ents * rate / 1000;

                    add_user_ents_cuts addEnts = new add_user_ents_cuts();
                    addEnts.setCuts_of_pachhisa(cuts_of_pachhisa);
                    addEnts.setBharai_rate(rate);
                    addEnts.setTotal_ents(total_ents);
                    addEnts.setKati_gayi_ents(katane_wali_ents);
                    addEnts.setMoney(moneyy);
                    addEnts.setShowbtn(true);

                    databaseReference.child("Muneem").child(auth.getUid()).child("users").child(userid).child("Store_data").setValue(addEnts).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(addEntsActivity.this, "added", Toast.LENGTH_SHORT).show();
                                databaseReference.child("Muneem").child(auth.getUid()).child("users").child(userid).child("Store_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            boolean getvalue = dataSnapshot.child("showbtn").getValue(Boolean.class);
                                            if (getvalue == true) {

                                                addUpateCardBtn.setVisibility(View.VISIBLE);
                                                updateBtn.setVisibility(View.VISIBLE);
                                                //not show add ents
                                                addCardEnts.setVisibility(View.GONE);
                                                addEntss.setVisibility(View.GONE);

                                            } else {
                                                addUpateCardBtn.setVisibility(View.GONE);
                                                updateBtn.setVisibility(View.GONE);
                                                //not show add ents
                                                addCardEnts.setVisibility(View.VISIBLE);
                                                addEntss.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            }
                        }
                    });
                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addEntsEditText.getText().toString().isEmpty()) {
                    Toast.makeText(addEntsActivity.this, "Ents is empty", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("ents_data", MODE_PRIVATE);
                    String BHARAI_RATE = sharedPreferences.getString("bharai_rate", "");
                    String CUTS_OF_PACHHISA = sharedPreferences.getString("cuts_of_pachisa", "");

                    myEnts = Integer.parseInt(addEntsEditText.getText().toString());

                    rate = Integer.parseInt(BHARAI_RATE); //bharai rate
                    cuts_of_pachhisa = Integer.parseInt(CUTS_OF_PACHHISA); //kati gayi ents

                    katane_wali_ents = myEnts * cuts_of_pachhisa / 1000; //isase hamri katane wali ents aayegi 4000 par 100 ents katengi
                    total_ents = myEnts - katane_wali_ents; // yaha par hamari total ents aa gayi // 3900
                    moneyy = total_ents * rate / 1000;

                    add_user_ents_cuts model = new add_user_ents_cuts();

                    model.setBharai_rate(rate);
                    model.setCuts_of_pachhisa(cuts_of_pachhisa);
                    model.setShowbtn(true);
                    model.setMoney(moneyy + mny);
                    model.setTotal_ents(total_ents + totalents);
                    model.setKati_gayi_ents(katane_wali_ents + katiEnts);

                    databaseReference.child("Muneem").child(auth.getUid()).child("users").child(userid).child("Store_data").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(addEntsActivity.this, "Ents added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addEntsActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void GET_OUR_DATA() {


        databaseReference.child("Muneem").child(auth.getUid()).child("users").child(userid).child("Store_data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    boolean getvalue = dataSnapshot.child("showbtn").getValue(Boolean.class);
                    mny = dataSnapshot.child("money").getValue(Integer.class);
                    totalents = dataSnapshot.child("total_ents").getValue(Integer.class);
                    katiEnts = dataSnapshot.child("kati_gayi_ents").getValue(Integer.class);

                    money.setText(mny + "");
                    totalEnts.setText(totalents + "");
                    katiGayiEnts.setText(katiEnts + "");

                    if (getvalue == true) {

                        addUpateCardBtn.setVisibility(View.VISIBLE);
                        updateBtn.setVisibility(View.VISIBLE);
                        //not show add ents
                        addCardEnts.setVisibility(View.GONE);
                        addEntss.setVisibility(View.GONE);

                    } else {
                        addUpateCardBtn.setVisibility(View.GONE);
                        updateBtn.setVisibility(View.GONE);
                        //not show add ents
                        addCardEnts.setVisibility(View.VISIBLE);
                        addEntss.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(addEntsActivity.this, "Error is " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void RESET_ALL_USER_DATA(View view) {

        //pahale tum ye wala method run karo ki you want to delete all data in your users

        //////////////////////////ye kam hoga bad mai jab data added hoga
        dialog = new Dialog(addEntsActivity.this);
        dialog.setContentView(R.layout.caustom_dailog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); //this is get the hight and width
        dialog.setCancelable(false);

        CircleImageView userProfileImageDailog = dialog.findViewById(R.id.userProfileDailog);
        TextView userTitlename = dialog.findViewById(R.id.userNameDailog);
        AppCompatButton yes = dialog.findViewById(R.id.yesBtnDailog);
        AppCompatButton no = dialog.findViewById(R.id.noBtnDailog);

        ///GET USER IMAGE AND NAME
        userTitlename.setText(mera_name);
        Picasso.get().load(meri_profile).into(userProfileImageDailog);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_user_ents_cuts addEnts = new add_user_ents_cuts();
                addEnts.setCuts_of_pachhisa(cuts_of_pachhisa);
                addEnts.setBharai_rate(rate);
                addEnts.setTotal_ents(0);
                addEnts.setKati_gayi_ents(0);
                addEnts.setMoney(0);
                addEnts.setShowbtn(false);

                databaseReference.child("Muneem").child(auth.getUid()).child("users").child(userid).child("Store_data").setValue(addEnts).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(addEntsActivity.this, "Data reseted....", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(addEntsActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(addEntsActivity.this, "Thanks a Lot :)", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}