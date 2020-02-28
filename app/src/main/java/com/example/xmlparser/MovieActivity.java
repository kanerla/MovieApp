package com.example.xmlparser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieActivity extends AppCompatActivity {
    private static final String URL = "https://www.finnkino.fi/xml/Events/";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Event> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadPage();
    }

    // Uses AsyncTask to download the XML feed from the URL.
    public void loadPage() {
        new DownloadXmlTask().execute(URL);
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
            myAdapter = new CustomAdapter(getApplicationContext(), entries);
            recyclerView.setAdapter(myAdapter);

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
            XmlParser stackOverflowXmlParser = new XmlParser();
            entries = null;
            String title = null;
            String url = null;
            String summary = null;
            String photo = null;

            // Checks whether the user set the preference to include summary text
            // SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            StringBuilder htmlString = new StringBuilder();
            Log.d("MovieActivity", "we got a stringbuilder here");

            try {
                stream = downloadUrl(urlString);
                entries = stackOverflowXmlParser.parse(stream);
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

            /*
            for (Event entry : entries) {
                Log.d("MovieActivity", "we got an entry");
                // htmlString.append(entry.link);
                // htmlString.append(" " + entry.title);
                Log.d("MovieActivity", "title was " + entry.title);
            } */
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
