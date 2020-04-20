package com.tamk.moviet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * SeenFragment extends Fragment class, and shows
 * all items added to user's personal "seen" list.
 * SeenFragment is shown in PersonalActivity.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class SeenFragment extends Fragment {
    private RecyclerView recyclerView;
    private PersonalAdapter adapter;
    private MovieViewModel movieViewModel;
    private TextView welcome;

    /**
     * Called to have the fragment intantiate its user interface view.
     * Returns the view to inflate.
     *
     * @param inflater              the layoutinflater used to inflate views in the fragment
     * @param container             the parent view that the fragment is attached to
     * @param savedInstanceState    if non-null, the previous state fragment is being re-constructed from
     * @return                      the view to inflate
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.seen_fragment, container, false);

        movieViewModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        welcome = view.findViewById(R.id.welcome);

        recyclerView = view.findViewById(R.id.recyclerview);
        // recyclerView.setHasFixedSize(true);
        adapter = new PersonalAdapter(getContext(), "seenFragment");
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
