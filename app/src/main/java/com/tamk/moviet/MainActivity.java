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

public class MainActivity extends AppCompatActivity {
    boolean connected = false;
    ConnectivityManager connectivityManager;
    NetworkInfo activeNetwork;
    private static final String TAG = "MainActivity";

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
