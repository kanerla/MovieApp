package com.example.xmlparser;

import android.content.Context;
import android.util.Log;
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
    public void onBindViewHolder(final CustomAdapter.ViewHolder viewHolder, int i) {
        final int index = i;
        viewHolder.title.setText(events.get(i).getTitle());
        Log.d("CustomAdapter", "Photo address: " + events.get(i).getPhoto());
        Picasso.with(context).load(events.get(i).getPhoto()).fit().error(R.drawable.placeholder).into(viewHolder.picture);
        // Picasso.with(context).load(R.drawable.ahiddenlife).fit().error(R.drawable.placeholder).into(viewHolder.picture);

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((MovieActivity)context).replaceFragment(fragment); where fragment should probably be the name of this
                Log.d("CustomAdapter", viewHolder.title.getText() + " was clicked");
                ((MovieActivity)context).showInfoDialog(index);
            }
        });
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
