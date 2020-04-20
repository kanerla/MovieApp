package com.tamk.moviet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import java.util.Locale;

/**
 * MainActivity is the activity that starts the application.
 * In MainActivity, user is asked to secure internet connection
 * and urged to continue to view the application content.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 *
 */
public class MainActivity extends AppCompatActivity {
    private boolean connected = false;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetwork;
    private static final String TAG = "MainActivity";

    /**
     * Called when activity starts.
     * Initializes the used Locale and ConnectivityManager.
     * Calls setContentView to inflate activity's UI.
     *
     * @param savedInstanceState    null or the data that activity most recently supplied in onSaveInstanceState()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        Locale primaryLocale = getApplicationContext().getResources().getConfiguration().getLocales().get(0);
        String locale = primaryLocale.getDisplayName();
        Log.d(TAG, locale);
    }

    /**
     * Start new MovieActivity.
     * Makes sure user is connected to the internet
     * before continuing.
     *
     * @param v view that triggers this action.
     */
    public void loadPage(View v) {
        activeNetwork = connectivityManager.getActiveNetworkInfo();
        connected = activeNetwork != null;

        if (connected) {
            Intent i = new Intent(this, MovieActivity.class);
            startActivity(i);
        } else {
            Log.d(TAG, "Not connected to the internet");
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.connect,
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}
