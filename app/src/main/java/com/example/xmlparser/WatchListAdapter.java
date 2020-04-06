package com.example.xmlparser;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MovieViewHolder> {
    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitle;
        private final TextView originalTitle;
        private Button moveButton;
        private Button removeButton;
        private Button clearButton;
        private LinearLayout extra;
        private EditText withInput;
        private EditText whereInput;
        private EditText whenInput;
        private Button saveButton;
        private RatingBar ratingBar;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.watchlist_title);
            originalTitle = itemView.findViewById(R.id.watchlist_original);
            moveButton = itemView.findViewById(R.id.move_button);
            removeButton = itemView.findViewById(R.id.remove_button);
            clearButton = itemView.findViewById(R.id.clear_date_button);
            extra = itemView.findViewById(R.id.extra);
            withInput = itemView.findViewById(R.id.with_input);
            whereInput = itemView.findViewById(R.id.where_input);
            whenInput = itemView.findViewById(R.id.when_input);
            saveButton = itemView.findViewById(R.id.save_seen);
            ratingBar = itemView.findViewById(R.id.rating_bar);
        }
    }

    private final LayoutInflater inflater;
    private List<Event> events; // Cached copy of events
    private int expandedPosition = -1;
    private int previouslyExpanded = -1;
    private MovieViewModel movieViewModel;
    private String fragment;
    private Calendar currentTime = Calendar.getInstance();
    private Context context;

    WatchListAdapter(Context context, String fragment) {
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.context = context;
        movieViewModel = ViewModelProviders.of((PersonalActivity) context).get(MovieViewModel.class);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (fragment.equals("seenFragment")) {
            Log.d("Fragment", "was seen");
            itemView = inflater.inflate(R.layout.seen_item, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.watchlist_item, parent, false);
        }
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                currentTime.set(Calendar.YEAR, year);
                currentTime.set(Calendar.MONTH, monthOfYear);
                currentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(holder);
            }
        };

        final boolean isExpanded = position==expandedPosition;
        // what I wanna show
        holder.removeButton.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.moveButton.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        if (fragment.equals("seenFragment")) {
            holder.extra.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        }
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previouslyExpanded = position;

        holder.movieTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previouslyExpanded);
                notifyItemChanged(position);
            }
        });

        if (events != null) {
            Event current = events.get(position);
            holder.movieTitle.setText(current.getTitle());
            holder.originalTitle.setText(current.getOriginalTitle());
            if(fragment.equals("seenFragment")) {
                holder.withInput.setText(current.getWith());
                holder.whereInput.setText(current.getWhere());
                holder.ratingBar.setRating(current.getRating());
                holder.whenInput.setText(current.getDate());
                holder.whenInput.setOnClickListener((View v) -> {
                    new DatePickerDialog(context, dateListener, currentTime
                            .get(Calendar.YEAR), currentTime.get(Calendar.MONTH),
                            currentTime.get(Calendar.DAY_OF_MONTH)).show();
                });
                // updateDate(holder);
                holder.clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.whenInput.setText("");
                        currentTime = Calendar.getInstance();
                    }
                });

                holder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        current.setWith(holder.withInput.getText().toString());
                        current.setWhere(holder.whereInput.getText().toString());
                        current.setDate(holder.whenInput.getText().toString());
                        current.setRating(holder.ratingBar.getRating());
                        Log.d("Date is ", "" + holder.whenInput.getText().toString());
                        movieViewModel.update(events);
                        // TOAST
                        Toast toast = Toast.makeText(context,
                                "Saved successfully",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            }
            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieViewModel.remove(current);
                    expandedPosition = isExpanded ? -1:position;
                }
            });
            holder.moveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment.equals("seenFragment")) {
                        current.setSeen(false);
                    } else {
                        current.setSeen(true);
                    }
                    Log.d("Seen?", "" + current.isSeen());
                    movieViewModel.update(events);
                    expandedPosition = isExpanded ? -1:position;
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.movieTitle.setText("No title found");
        }
    }

    /**
     * Formats time and sets it to a ViewHolder item.
     *
     * @param holder    the ViewHolder which contains the item changed
     */
    public void updateDate(MovieViewHolder holder) {
        String myFormat = "dd/MM/yyyy";
        DateFormat sdf = new SimpleDateFormat(myFormat);
        String seenDate = sdf.format(currentTime.getTime());

        holder.whenInput.setText(seenDate);
    }

    /**
     * Set events to the recyclerview and notify it about the changes.
     *
     * @param movies    list of events.
     */
    void setEvents(List<Event> movies){
        events = movies;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // events has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (events != null)
            return events.size();
        else return 0;
    }
}
