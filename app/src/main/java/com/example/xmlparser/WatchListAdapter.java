package com.example.xmlparser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MovieViewHolder> {
    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView movieTitle;
        private final TextView originalTitle;
        private Button moveButton;
        private Button removeButton;
        private LinearLayout extra;
        private EditText withInput;
        private EditText whereInput;
        private Button saveButton;

        private MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.watchlist_title);
            originalTitle = itemView.findViewById(R.id.watchlist_original);
            moveButton = itemView.findViewById(R.id.move_button);
            removeButton = itemView.findViewById(R.id.remove_button);
            extra = itemView.findViewById(R.id.extra);
            withInput = itemView.findViewById(R.id.with_input);
            whereInput = itemView.findViewById(R.id.where_input);
            saveButton = itemView.findViewById(R.id.save_seen);
        }
    }

    private final LayoutInflater inflater;
    private List<Event> events; // Cached copy of events
    private int expandedPosition = -1;
    private int previouslyExpanded = -1;
    private MovieViewModel movieViewModel;
    private String fragment;

    WatchListAdapter(Context context, String fragment) {
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
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
                holder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        current.setWith(holder.withInput.getText().toString());
                        current.setWhere(holder.whereInput.getText().toString());
                        movieViewModel.update(events);
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
