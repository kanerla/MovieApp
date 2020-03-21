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
        private final TextView movieItemView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieItemView = itemView.findViewById(R.id.listItem);
        }
    }

    private final LayoutInflater inflater;
    private List<Event> events; // Cached copy of events

    WatchListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (events != null) {
            Event current = events.get(position);
            holder.movieItemView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.movieItemView.setText("No title found");
        }
    }

    void setEvents(List<Event> movies){
        events = movies;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (events != null)
            return events.size();
        else return 0;
    }
}
