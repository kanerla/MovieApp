package com.example.xmlparser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PersonalActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    Button seenButton;
    Button watchlistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new SeenFragment();
        transaction.add(R.id.personal_fragment_container, fragment);
        transaction.commit();

        seenButton = findViewById(R.id.seen_button);
        seenButton.setEnabled(false);
        watchlistButton = findViewById(R.id.watchlist_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Replace previous fragment in the fragment container with another.
     * Disable view that triggered the action and enable other view(s).
     *
     * @param v view that triggers this action.
     */
    public void switchToSeen(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new SeenFragment();
        // fragment.setArguments(comingbundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();

        v.setEnabled(false);
        watchlistButton.setEnabled(true);
    }

    /**
     * Replace previous fragment in the fragment container with another.
     * Disable view that triggered the action and enable other view(s).
     *
     * @param v view that triggers this action.
     */
    public void switchToWatchList(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new WatchlistFragment();
        // fragment.setArguments(comingbundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();

        v.setEnabled(false);
        seenButton.setEnabled(true);
    }
}
