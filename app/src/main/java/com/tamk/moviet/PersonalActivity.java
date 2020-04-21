package com.tamk.moviet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

/**
 * PersonalActivity is the activity where user can switch between
 * Watchlist- and SeenFragments and view their personal information.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class PersonalActivity extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TabLayout tabLayout;
    private MovieViewModel movieViewModel;

    /**
     * Called when activity starts.
     * Initializes attributes, fragments and tablayout.
     * Calls setContentView to inflate activity's UI.
     *
     * @param savedInstanceState    null or the data that activity most recently supplied in onSaveInstanceState()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

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
                // Do nothing because method not needed.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing because method not needed.
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
        ArrayList<Integer> ratings = new ArrayList<>();
        movieViewModel.getAllSeenEvents().observe(this, movies -> {
            for (Event m : movies) {
                int rating = (int) m.getRating();
                ratings.add(rating);
            }
        });
        Bundle ratingBundle = new Bundle();
        ratingBundle.putIntegerArrayList("ratings", ratings);

        int id = item.getItemId();
        if (id == R.id.stats) {
            Intent i = new Intent(this, StatisticsActivity.class);
            i.putExtras(ratingBundle);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
