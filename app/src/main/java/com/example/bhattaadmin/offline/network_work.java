package com.example.bhattaadmin.offline;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class network_work extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
