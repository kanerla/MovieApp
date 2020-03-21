package com.example.xmlparser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WatchlistFragment extends Fragment {
    private RecyclerView recyclerView;
    private WatchListAdapter adapter;
    private MovieViewModel mWordViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);

        mWordViewModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        adapter = new WatchListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mWordViewModel.getAllEvents().observe(this, words -> {
            // Update the cached copy of the words in the adapter.
            adapter.setEvents(words);
        });

        return view;
    }

}
