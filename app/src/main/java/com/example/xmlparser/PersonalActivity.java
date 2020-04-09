package com.example.xmlparser;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PersonalActivity extends AppCompatActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Fragment fragment = new SeenFragment();
        transaction.add(R.id.personal_fragment_container, fragment);
        transaction.commit();

        tabLayout = findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    // Seen
                    switchToSeen();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    // Watchlist
                    switchToWatchList();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    /**
     * Executes the onBackPressed() method.
     * Called when user chooses to navigate up within application's activity hierarchy
     * from the action bar.
     *
     * @return  true if up navigation is completed successfully, false otherwise
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Replace previous fragment in the fragment container with another.
     * Disable view that triggered the action and enable other view(s).
     *
     */
    public void switchToSeen() {
        manager = getSupportFragmentManager();
        Fragment fragment = new SeenFragment();

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Replace previous fragment in the fragment container with another.
     * Disable view that triggered the action and enable other view(s).
     *
     */
    public void switchToWatchList() {
        manager = getSupportFragmentManager();
        Fragment fragment = new WatchlistFragment();

        transaction = manager.beginTransaction();

        transaction.replace(R.id.personal_fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Specifies the options menu for the activity.
     * Inflates the menu resource into the menu provided in the callback.
     * Basically, adds items to the action bar if it is present.
     *
     * @param menu  menu to be inflated
     * @return      true when completed successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    /**
     * Called whenever an item in options menu is selected.
     * Handles action bar item clicks.
     * Handles Home/Up button clicks automatically if parent activity is specifies in AndroidManifest.xml.
     * MenuItem parameter value must never be null.
     *
     * @param item  the selected menu item
     * @return      false to allow normal menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.stats) {
            Log.d("PersonalActivity", "stats was clicked");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
