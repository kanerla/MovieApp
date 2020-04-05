package com.example.xmlparser;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.stats) {
            Log.d("PersonalActivity", "stats was clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
