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

        Fragment fragment = new SeenFragment();
        transaction.add(R.id.personal_fragment_container, fragment);
        transaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void switchToSeen(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new SeenFragment();
        // fragment.setArguments(comingbundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();
    }

    public void switchToWatchList(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new WatchlistFragment();
        // fragment.setArguments(comingbundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();
    }
}
