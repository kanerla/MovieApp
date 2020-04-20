package com.tamk.moviet;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
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
    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ratings = getIntent().getExtras().getIntegerArrayList("ratings");
        totalInSeen = ratings.size();

        Log.d(TAG, "Bundle: " + ratings.size());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bluecolors = new int[] {R.color.BabyBlue, R.color.OceanBlue, R.color.CookieMonsterBlue, R.color.FindingDoryBlue, R.color.BlueberryBlue, R.color.SquirtleBlue};
        greencolors = new int[] {R.color.AlienGreen, R.color.ShamrockGreen, R.color.LimeGreen, R.color.MikeWazowskiGreen, R.color.MediumSeaGreen, R.color.StarbucksGreen};

        sortRatings();
        createStarChart();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieDataSet.setColors(bluecolors, getApplicationContext());
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
            Log.d(TAG, "Rating: " + rating); // 0 - 5
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
                default:
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

    /**
     * Specifies the options menu for the activity.
     * Inflates the menu resource into the menu provided in the callback.
     * Basically, adds items to the action bar if it is present.
     *
     * @param menu  menu to be inflated
     * @return      true when completed successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info) {
            Log.d(TAG, "info was clicked");

            new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(R.drawable.ic_info)
                    //set title
                    .setTitle("MPAndroidChart License")
                    //set message
                    .setMessage(readLicense())
                    //set positive button
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            dialogInterface.cancel();
                        }
                    })
                    .show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String readLicense() {
        String license = "";

        try{
            AssetManager am = getApplicationContext().getAssets();
            InputStream is = am.open("license.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                if(receiveString.equals("")){
                    stringBuilder.append(System.getProperty("line.separator"));
                }else{
                    stringBuilder.append(receiveString);
                }
            }
            is.close();
            license = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return license;
    }
}
