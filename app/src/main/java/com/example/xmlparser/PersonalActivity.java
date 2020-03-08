package com.example.xmlparser;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PersonalActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new WatchlistFragment();
        transaction.add(R.id.personal_fragment_container, fragment);
        transaction.commit();

        /*
        Button back = findViewById(R.id.finnkino);
        back.setOnClickListener((View v) -> {
            finish();
        });
        */
    }
}
