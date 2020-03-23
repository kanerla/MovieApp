package com.example.xmlparser;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MovieActivity extends AppCompatActivity {
    private static final String URL = "https://www.finnkino.fi/xml/Events/";
    private static final String comingURL = "https://www.finnkino.fi/xml/Events/?listType=ComingSoon";
    List<Event> entries;
    List<Event> coming;
    Button nitButton;
    Button csButton;
    FragmentManager manager;
    FragmentTransaction transaction;
    NowInTheatresFragment nitFragment;
    Bundle bundle;
    Bundle comingBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        /*
        // show item on the left side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_home);
        // remove/show title of the app
        getSupportActionBar().setDisplayShowTitleEnabled(true); */

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        nitButton = findViewById(R.id.nit_button);
        nitButton.setEnabled(false);
        csButton = findViewById(R.id.cs_button);

        loadPage();
        loadComingPage();
    }

    // Uses AsyncTask to download the XML feed from the URL.
    public void loadPage() {
        new DownloadXmlTask().execute(URL);
    }

    // Uses AsyncTask to download the XML feed from the URL.
    public void loadComingPage() {
        new DownloadComingXmlTask().execute(comingURL);
    }

    public void showInfoDialog(Event e) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", e);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieInfoDialogFragment movieInfo = new MovieInfoDialogFragment();
        movieInfo.setArguments(bundle);
        movieInfo.show(fragmentManager, "MovieInfo");
    }

    public void switchToNowInTheatres(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new NowInTheatresFragment();
        fragment.setArguments(bundle);

        /* CREATES ONE COPY OF FRAGMENT TO BACKSTACK --> back button opens previous fragment
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(backStateName);
            transaction.commit();
        } */

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        // transaction.addToBackStack(null);
        transaction.commit();

        v.setEnabled(false);
        csButton.setEnabled(true);
    }

    public void switchToComingSoon(View v) {
        manager = getSupportFragmentManager();
        Fragment fragment = new ComingSoonFragment();
        fragment.setArguments(comingBundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        v.setEnabled(false);
        nitButton.setEnabled(true);
    }

    /*
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
            return;
        }

        super.onBackPressed();
    } */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
                // User chose the "Search" item...
                Log.d("MovieActivity", "search was clicked");
                return true;

            case R.id.mymovies:
                // User chose the "Account" action...
                Log.d("MovieActivity", "mymovies was clicked");
                goToPersonalPage();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void goToPersonalPage() {
        Intent i = new Intent(this, PersonalActivity.class);
        startActivity(i);
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("MovieActivity", "we trying to load");
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Connection error";
            } catch (XmlPullParserException e) {
                return "Error parsing";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // listassa on tavaraa
            Log.d("Entries: ", "" + entries.size());

            ArrayList<Event> array = new ArrayList<>(entries);
            bundle = new Bundle();
            bundle.putParcelableArrayList("eventsArray", array);

            nitFragment = new NowInTheatresFragment();
            nitFragment.setArguments(bundle);

            transaction.add(R.id.fragment_container, nitFragment);
            transaction.commit();

            Log.d("transaction", "started");

            /*
            setContentView(R.layout.main);
            // Displays the HTML string in the UI via a WebView
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadData(result, "text/html", null); */
            Log.d("MovieActivity", result);
        }

        // Uploads XML from the URL and parses it. Returns string.
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            XmlParser xmlParser = new XmlParser();
            entries = null;

            // Checks whether the user set the preference to include summary text
            // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            StringBuilder htmlString = new StringBuilder();
            Log.d("MovieActivity", "we got a stringbuilder here");

            try {
                stream = downloadUrl(urlString);
                entries = xmlParser.parse(stream);
                Log.d("Entries 1: ", "" + entries.size());
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                Log.d("MovieActivity", "Streamed and parsed");
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d("MovieActivity", "Closed the stream");
                }
            }

            // XmlParser returns a List (called "events") of Event objects.
            // Each Event object represents a single movie in the XML feed.
            // This section processes the events list to combine each event with HTML markup.
            // Each event is displayed in the UI as a link that optionally includes
            // a text summary.

            return htmlString.toString();
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("MovieActivity", "all good in the hood");
            // Starts the query
            conn.connect();
            Log.d("MovieActivity", "conn.connect says: No adapter attached; skipping layout");
            return conn.getInputStream();
        }
    }

    private class DownloadComingXmlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Connection error";
            } catch (XmlPullParserException e) {
                return "Error parsing";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // listassa on tavaraa
            Log.d("Coming Soon: ", "" + coming.size());

            ArrayList<Event> array = new ArrayList<>(coming);
            comingBundle = new Bundle();
            comingBundle.putParcelableArrayList("comingArray", array);
        }

        // Uploads XML from the URL and parses it. Returns string.
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            XmlParser xmlParser = new XmlParser();
            coming = null;

            // Checks whether the user set the preference to include summary text
            // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            StringBuilder htmlString = new StringBuilder();

            try {
                stream = downloadUrl(urlString);
                coming = xmlParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                Log.d("MovieActivity", "Streamed and parsed");
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d("MovieActivity", "Closed the stream");
                }
            }

            // XmlParser returns a List (called "events") of Event objects.
            // Each Event object represents a single movie in the XML feed.
            // This section processes the events list to combine each event with HTML markup.
            // Each event is displayed in the UI as a link that optionally includes
            // a text summary.

            return htmlString.toString();
        }

        // Given a string representation of a URL, sets up a connection and gets
        // an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("MovieActivity", "all good in the hood");
            // Starts the query
            conn.connect();
            Log.d("MovieActivity", "conn.connect says: No adapter attached; skipping layout");
            return conn.getInputStream();
        }
    }
}
