package com.example.xmlparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MovieViewHolder> {
    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitle;
        private final TextView originalTitle;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.watchlist_title);
            originalTitle = itemView.findViewById(R.id.watchlist_original);
        }
    }

    private final LayoutInflater inflater;
    private List<Event> events; // Cached copy of events

    WatchListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.watchlist_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (events != null) {
            Event current = events.get(position);
            holder.movieTitle.setText(current.getTitle());
            holder.originalTitle.setText(current.getOriginalTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.movieTitle.setText("No title found");
        }
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
