package com.example.xmlparser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SeenFragment extends Fragment {
    private RecyclerView recyclerView;
    private WatchListAdapter adapter;
    private MovieViewModel movieViewModel;
    private TextView welcome;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.seen_fragment, container, false);

        movieViewModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        welcome = view.findViewById(R.id.welcome);

        recyclerView = view.findViewById(R.id.recyclerview);
        // recyclerView.setHasFixedSize(true);
        adapter = new WatchListAdapter(getContext(), "seenFragment");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        movieViewModel.getAllSeenEvents().observe(this, movies -> {
            // Update the cached copy of the events in the adapter.
            if(movies.isEmpty()) {
                welcome.setVisibility(View.VISIBLE);
            } else {
                //clumsy
                welcome.setVisibility(View.GONE);
            }
            adapter.setEvents(movies);
        });

        return view;
    }
}
