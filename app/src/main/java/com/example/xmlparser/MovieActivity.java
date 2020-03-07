package com.example.xmlparser;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MovieActivity extends AppCompatActivity {
    private static final String URL = "https://www.finnkino.fi/xml/Events/";
    List<Event> entries;
    FragmentManager manager;
    FragmentTransaction transaction;
    NowInTheatresFragment nit_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        loadPage();
    }

    // Uses AsyncTask to download the XML feed from the URL.
    public void loadPage() {
        new DownloadXmlTask().execute(URL);
    }

    public void showInfoDialog(Event e) {
        Bundle bundle = new Bundle();
        bundle.putString("title", e.getTitle());
        bundle.putString("synopsis", e.getSummary());
        bundle.putString("original", e.getOriginal());
        bundle.putString("release", e.getReleaseDate());
        Log.d("MovieActivity", "Release date: " + e.getReleaseDate());
        bundle.putString("length", e.getLength());
        bundle.putString("link", e.getLink());
        bundle.putString("genres", e.getGenres());
        bundle.putString("photo", e.getPhoto());

        FragmentManager fragmentManager = getSupportFragmentManager();
        MovieInfoDialogFragment movieinfo = new MovieInfoDialogFragment();
        movieinfo.setArguments(bundle);
        movieinfo.show(fragmentManager, "MovieInfo");
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
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("eventsArray", array);

            nit_fragment = new NowInTheatresFragment();
            nit_fragment.setArguments(bundle);

            transaction.add(R.id.fragment_container, nit_fragment);
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
}
