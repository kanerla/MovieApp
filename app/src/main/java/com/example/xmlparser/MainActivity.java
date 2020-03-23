package com.example.xmlparser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Start new MovieActivity.
     *
     * @param v view that triggers this action.
     */
    public void loadPage(View v) {
        Intent i = new Intent(this, MovieActivity.class);
        startActivity(i);
    }
}
