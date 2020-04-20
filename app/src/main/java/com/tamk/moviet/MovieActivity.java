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

public class MovieActivity extends AppCompatActivity {
    private static final String URL = "https://www.finnkino.fi/xml/Events/";
    private static final String comingURL = "https://www.finnkino.fi/xml/Events/?listType=ComingSoon";
    List<Event> entries;
    List<Event> coming;
    TabLayout tabLayout;
    FragmentManager manager;
    FragmentTransaction transaction;
    NowInTheatresFragment nitFragment;
    Bundle bundle;
    Bundle nowBundle;
    Bundle comingBundle;
    private static final String TAG = "MovieActivity";

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

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

    // Uses AsyncTask to download the XML feed from the URL.
    public void loadComingPage() {
        new DownloadComingXmlTask().execute(comingURL);
    }

    public void showInfoDialog(Event e) {
        bundle = new Bundle();
        bundle.putParcelable("movie", e);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieInfoDialogFragment movieInfo = new MovieInfoDialogFragment();
        movieInfo.setArguments(bundle);
        movieInfo.show(fragmentManager, "MovieInfo");
    }

    public void switchToNowInTheatres() {
        manager = getSupportFragmentManager();
        Fragment fragment = new NowInTheatresFragment();
        fragment.setArguments(nowBundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void switchToComingSoon() {
        manager = getSupportFragmentManager();
        Fragment fragment = new ComingSoonFragment();
        fragment.setArguments(comingBundle);

        transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:
                // User chose the "Search" item...
                Log.d(TAG, "search was clicked");
                return true;

            case R.id.mymovies:
                // User chose the "Account" action...
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

    private class DownloadXmlTask extends AsyncTask<String, Void, String> {
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

        // Uploads XML from the URL and parses it. Returns string.
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
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                Log.d(TAG, "Streamed and parsed");
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d(TAG, "Closed the stream");
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
            // Starts the query
            conn.connect();
            Log.d(TAG, "conn.connect says: No adapter attached; skipping layout");
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
            Log.d(TAG, "Coming soon: " + coming.size());

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

            StringBuilder htmlString = new StringBuilder();

            try {
                stream = downloadUrl(urlString);
                coming = xmlParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
                Log.d(TAG, "Streamed and parsed");
            } finally {
                if (stream != null) {
                    stream.close();
                    Log.d(TAG, "Closed the stream");
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
            // Starts the query
            conn.connect();
            Log.d(TAG, "conn.connect says: No adapter attached; skipping layout");
            return conn.getInputStream();
        }
    }
}
