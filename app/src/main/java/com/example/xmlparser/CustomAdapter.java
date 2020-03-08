package com.example.xmlparser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Event> events;
    List<Event> fullEvents;

    public CustomAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        fullEvents = new ArrayList<>(events);
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(events.get(i).getTitle());
        Log.d("CustomAdapter", "Photo address: " + events.get(i).getPhoto());
        Picasso.with(context).load(events.get(i).getPhoto()).fit().error(R.drawable.placeholder).into(viewHolder.picture);
        // Picasso.with(context).load(R.drawable.ahiddenlife).fit().error(R.drawable.placeholder).into(viewHolder.picture);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public Filter getFilter() {
        return eventFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView picture;
        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.listItem);
            picture = view.findViewById(R.id.picture);

            title.setOnClickListener((View v) -> {
                // ((MovieActivity)context).replaceFragment(fragment); where fragment should probably be the name of this
                Log.d("CustomAdapter", title.getText() + " was clicked");
                ((MovieActivity)context).showInfoDialog(events.get(getAdapterPosition()));
            });
        }
    }

    private Filter eventFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullEvents);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Event e : fullEvents) {
                    if (e.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(e);
                    }
                }
            }
            // sort by release date
            // sortList(filteredList);
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            events.clear();
            events.addAll((List)results.values);
            notifyDataSetChanged();
        }

        // sort list by release date (default = a -> z)
        protected void sortList(List<Event> filteredList) {
            Collections.sort(filteredList, new Comparator() {
                @Override
                public int compare(Object one, Object two) {
                    //use instanceof to verify the references are indeed of the type in question
                    return ((Event)one).getReleaseDate()
                            .compareTo(((Event)two).getReleaseDate());
                }
            });
        }
    };
}
