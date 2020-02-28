package com.example.xmlparser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Context context;
    List<Event> events;

    public CustomAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(events.get(i).getTitle());
        Picasso.with(context).load(events.get(i).getPhoto()).resize(120, 120).into(viewHolder.picture);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView picture;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.listItem);
            picture = (ImageView) view.findViewById(R.id.picture);
        }
    }
}
