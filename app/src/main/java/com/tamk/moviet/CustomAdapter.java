package com.tamk.moviet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * CustomAdapter class extends the RecyclerView.Adapter and
 * provides a binding from data set to a view displayed within a RecyclerView.
 * Adapter is used in MovieActivity and it's related fragments.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Event> events;
    private List<Event> fullEvents;

    /**
     * Class constructor.
     *
     * @param context   application context
     * @param events    list of all events
     */
    public CustomAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
        fullEvents = new ArrayList<>(events);
    }

    /**
     * Returns a new ViewHolder and adds a specified View to it.
     * Represents an item in the RecyclerView.
     *
     * @param viewGroup The ViewGroup into which the new View will be added
     * @param i         Type of the new View
     * @return          A new ViewHolder that holds a View
     */
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * Displays data at the given position and updates
     * the contens of the itemview.
     *
     * @param viewHolder    the holder that represents the contents of an item
     * @param i             the position of an item within adapter's data set
     */
    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(events.get(i).getTitle());
        Picasso.with(context).load(events.get(i).getPhoto()).fit().error(R.drawable.placeholder).into(viewHolder.picture);

        viewHolder.itemView.setOnClickListener((View v) -> {
            ((MovieActivity)context).showInfoDialog(events.get(i));
        });
    }

    /**
     * Returns the size of a list in an integer value.
     *
     * @return the size of specified list
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Returns a Filter object that filters list values
     * based on the given CharSequence.
     *
     * @return the specified Filter object
     */
    @Override
    public Filter getFilter() {
        return eventFilter;
    }

    /**
     * ViewHolder class extends the RecyclerView.ViewHolder and
     * describes an item view and metadata about its place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView picture;
        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.listItem);
            picture = view.findViewById(R.id.picture);
        }
    }

    /**
     * A filter that constraints data with a filtering pattern.
     * Operations are performed asynchronously.
     * Filters events based on a search term given to performFiltering().
     */
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
    };
}
