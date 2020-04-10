package com.example.xmlparser;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;
    int[] bluecolors;
    int[] greencolors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bluecolors = new int[] {R.color.BabyBlue, R.color.BlueberryBlue, R.color.CookieMonsterBlue, R.color.FindingDoryBlue, R.color.OceanBlue, R.color.SquirtleBlue};
        greencolors = new int[] {R.color.AlienGreen, R.color.ShamrockGreen, R.color.LimeGreen, R.color.MikeWazowskiGreen, R.color.MediumSeaGreen, R.color.StarbucksGreen};

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
        pieEntries.add(new PieEntry((float) 4.5, "⭐"));
        pieEntries.add(new PieEntry(5, "⭐⭐"));
        pieEntries.add(new PieEntry((float) 30.5, "⭐⭐⭐"));
        pieEntries.add(new PieEntry(10, "⭐⭐⭐⭐"));
        pieEntries.add(new PieEntry(45, "⭐⭐⭐⭐⭐"));
        pieEntries.add(new PieEntry(5, "No rating"));
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
