package com.example.xmlparser;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

public class StatisticsActivity extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;
    int[] bluecolors;
    int[] greencolors;
    ArrayList ratings;
    private float totalInSeen;
    private float noRating;
    private float ratedOne;
    private float ratedTwo;
    private float ratedThree;
    private float ratedFour;
    private float ratedFive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ratings = getIntent().getExtras().getIntegerArrayList("ratings");
        totalInSeen = ratings.size();

        Log.d("Bundle", "" + ratings.size());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bluecolors = new int[] {R.color.BabyBlue, R.color.BlueberryBlue, R.color.CookieMonsterBlue, R.color.FindingDoryBlue, R.color.OceanBlue, R.color.SquirtleBlue};
        greencolors = new int[] {R.color.AlienGreen, R.color.ShamrockGreen, R.color.LimeGreen, R.color.MikeWazowskiGreen, R.color.MediumSeaGreen, R.color.StarbucksGreen};

        sortRatings();
        createStarChart();
    }

    private void createStarChart() {
        pieChart = findViewById(R.id.starChart);
        getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value % 1 == 0) {
                    return ((int) Math.floor(value)) + "%";
                }
                return value + "%";
            }
        });
        // pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieDataSet.setColors(greencolors, getApplicationContext());
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        // donut, no donut
        pieChart.setDrawHoleEnabled(false);
        pieChart.setUsePercentValues(true);
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();

        if (noRating != 0) {
            float percent = (noRating / totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "No rating"));
        }
        if (ratedOne != 0) {
            float percent = (ratedOne / totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "⭐"));
        }
        if (ratedTwo != 0) {
            float percent = (ratedTwo / totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "⭐⭐"));
        }
        if (ratedThree != 0) {
            float percent = (ratedThree / totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "⭐⭐⭐"));
        }
        if (ratedFour != 0) {
            float percent = (ratedFour/totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "⭐⭐⭐⭐"));
        }
        if (ratedFive != 0) {
            float percent = (ratedFive / totalInSeen) * 100;
            pieEntries.add(new PieEntry(percent, "⭐⭐⭐⭐⭐"));
        }
    }

    private void sortRatings() {
        for (Object rating : ratings) {
            int intRating = (int) rating;
            Log.d("Movies", "" + rating); // 0 - 5
            switch (intRating) {
                case 0:
                    noRating ++;
                    break;
                case 1:
                    ratedOne ++;
                    break;
                case 2:
                    ratedTwo ++;
                    break;
                case 3:
                    ratedThree ++;
                    break;
                case 4:
                    ratedFour ++;
                    break;
                case 5:
                    ratedFive ++;
                    break;
            }
        }
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
}
