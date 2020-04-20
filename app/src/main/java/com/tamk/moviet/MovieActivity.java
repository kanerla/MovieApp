package com.tamk.moviet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.tabs.TabLayout;
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

/**
 * MovieActivity is the activity where user can switch between
 * NowInTheatres- and ComingSoonFragments.
 * Activity shows events that are listed on Finnkino's website and allows user
 * to view information of said events and add them to their personal watchlist or mark them as "seen".
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class MovieActivity extends AppCompatActivity {
    private static final String URL = "https://www.finnkino.fi/xml/Events/";
    private static final String comingURL = "https://www.finnkino.fi/xml/Events/?listType=ComingSoon";
    List<Event> entries;
    List<Event> coming;
    private TabLayout tabLayout;
    FragmentManager manager;
    FragmentTransaction transaction;
    NowInTheatresFragment nitFragment;
    Bundle bundle;
    Bundle nowBundle;
    Bundle comingBundle;
    private static final String TAG = "MovieActivity";

    /**
     * Called when activity starts.
     * Initializes the tablayout.
     * Calls loadPage() and loadComingPage() to access needed XML feed.
     * Calls setContentView to inflate activity's UI.
     *
     * @param savedInstanceState    null or the data that activity most recently supplied in onSaveInstanceState()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        tabLayout = findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    // Now in theatres
                    switchToNowInTheatres();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    // Coming soon
                    switchToComingSoon();
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

        loadPage();
        loadComingPage();
    }

    /**
     * Downloads the XML feed from the URL using AsyncTask.
     *
     */
    public void loadPage() {
        new DownloadXmlTask().execute(URL);
    }

    /**
     * Uses AsyncTask and downloads the XML feed from the URL.
     * Used specifically for movies that are "coming soon".
     */
    public void loadComingPage() {
        new DownloadComingXmlTask().execute(comingURL);
    }

    /**
     * Creates a DialogFragment and shows information about the chosen event.
     *
     * @param e     the chosen event
     */
    public void showInfoDialog(Event e) {
        bundle = new Bundle();
        bundle.putParcelable("movie", e);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieInfoDialogFragment movieInfo = new MovieInfoDialogFragment();
        movieInfo.setArguments(bundle);
        movieInfo.show(fragmentManager, "MovieInfo");
    }

    /**
     * Switch the fragment shown in Activity to NowInTheatresFragment.
     */
    public void switchToNowInTheatres() {
        manager = getSupportFragmentManager();
        Fragment fragment = new NowInTheatresFragment();
        fragment.setArguments(nowBundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Switch the fragment shown in Activity to ComingSoonFragment.
     */
    public void switchToComingSoon() {
        manager = getSupportFragmentManager();
        Fragment fragment = new ComingSoonFragment();
        fragment.setArguments(comingBundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Called whenever an item in options menu is selected.
     * Handles action bar item clicks.
     * MenuItem parameter value must never be null.
     *
     * @param item      the selected menu item
     * @return          false to allow normal menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
                Log.d(TAG, "search was clicked");
                return true;

            case R.id.mymovies:
                Log.d(TAG, "mymovies was clicked");
                goToPersonalPage();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Start new PersonalActivity.
     */
    public void goToPersonalPage() {
        Intent i = new Intent(this, PersonalActivity.class);
        startActivity(i);
    }

    /**
     * DownloadXmlTask class extends AsyncTask,
     * and loads XML from specified URL to use in the application.
     */
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
        /**
         * Performs a computation on a background thread.
         * Loads XML from given URL.
         *
         * @param urls  string representation of the url(s) to load
         * @return      the parsed string
         */
        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d(TAG, "trying to load");
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Connection error";
            } catch (XmlPullParserException e) {
                return "Error parsing";
            }
        }

        /**
         * Creates an event array and
         * assigns a bundle to it after executing the AsyncTask.
         * Sets up the original fragment.
         *
         * @param result    execution result
         */
        @Override
        protected void onPostExecute(String result) {
            ArrayList<Event> array = new ArrayList<>(entries);
            nowBundle = new Bundle();
            nowBundle.putParcelableArrayList("eventsArray", array);

            nitFragment = new NowInTheatresFragment();
            nitFragment.setArguments(nowBundle);

            transaction.add(R.id.fragment_container, nitFragment);
            transaction.commit();

            Log.d(TAG, result);
        }

        /**
         * Uploads XML from the given URL and parses it.
         * Returns string.
         *
         * @param urlString                 the url to connect to
         * @return                          xml data in string form
         * @throws XmlPullParserException   if parser exception occurred
         * @throws IOException              if an input or output exception occurred
         */
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            XmlParser xmlParser = new XmlParser();
            entries = null;

            StringBuilder htmlString = new StringBuilder();
            Log.d(TAG, "we got a stringbuilder here");

            try {
                stream = downloadUrl(urlString);
                entries = xmlParser.parse(stream);
                Log.d(TAG, "Entries: " + entries.size());
                Log.d(TAG, "Streamed and parsed");
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d(TAG, "Closed the stream");
                }
            }

            return htmlString.toString();
        }

        /**
         * Method gets a string presentation of a URL,
         * sets up a connection and gets an input stream.
         *
         * @param urlString     the url to connect to
         * @return              access to the content after connection is made
         * @throws IOException  if an input or output exception occurred
         */
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            Log.d(TAG, "conn.connect says: No adapter attached; skipping layout");
            return conn.getInputStream();
        }
    }

    /**
     * DownloadComingXmlTask class extends AsyncTask,
     * and loads XML from specified URL to use in the application.
     */
    private class DownloadComingXmlTask extends AsyncTask<String, Void, String> {
        /**
         * Performs a computation on a background thread.
         * Loads XML from given URL.
         *
         * @param urls  string representation of the url(s) to load
         * @return      the parsed string
         */
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

        /**
         * Creates an event array and
         * assigns a bundle to it after executing the AsyncTask.
         *
         * @param result    execution result
         */
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Coming soon: " + coming.size());

            ArrayList<Event> array = new ArrayList<>(coming);
            comingBundle = new Bundle();
            comingBundle.putParcelableArrayList("comingArray", array);
        }

        /**
         * Uploads XML from the given URL and parses it.
         * Returns string.
         *
         * @param urlString                 the url to connect to
         * @return                          xml data in string form
         * @throws XmlPullParserException   if parser exception occurred
         * @throws IOException              if an input or output exception occurred
         */
        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            XmlParser xmlParser = new XmlParser();
            coming = null;

            StringBuilder htmlString = new StringBuilder();

            try {
                stream = downloadUrl(urlString);
                coming = xmlParser.parse(stream);
                Log.d(TAG, "Streamed and parsed");
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d(TAG, "Closed the stream");
                }
            }

            return htmlString.toString();
        }

        /**
         * Method gets a string presentation of a URL,
         * sets up a connection and gets an input stream.
         *
         * @param urlString     the url to connect to
         * @return              access to the content after connection is made
         * @throws IOException  if an input or output exception occurred
         */
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            Log.d(TAG, "conn.connect says: No adapter attached; skipping layout");
            return conn.getInputStream();
        }
    }
}
