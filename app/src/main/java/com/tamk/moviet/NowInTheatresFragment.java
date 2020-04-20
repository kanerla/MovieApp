package com.tamk.moviet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * NowInTheatresFragment extends Fragment class, and shows
 * all events in Finnkino theatres at the moment.
 * NowInTheatresFragment is shown in MovieActivity.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class NowInTheatresFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Event> entries;

    /**
     * Class constructor.
     */
    public NowInTheatresFragment() {
        entries = new ArrayList<>();
    }

    /**
     * Called to have the fragment intantiate its user interface view.
     * Initiates most attributes.
     * Returns the view to inflate.
     *
     * @param inflater              the layoutinflater used to inflate views in the fragment
     * @param container             the parent view that the fragment is attached to
     * @param savedInstanceState    if non-null, the previous state fragment is being re-constructed from
     * @return                      the view to inflate
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        assert getArguments() != null;
        entries = getArguments().getParcelableArrayList("eventsArray");

        myAdapter = new CustomAdapter(getContext(), entries);
        recyclerView.setAdapter(myAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Specifies the options menu.
     * Inflates the menu resource into the menu provided in the callback.
     * Initializes the SearchView and its preferred action.
     *
     * @param menu          menu to be inflated
     * @param mInflater     MenuInflater to use
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater mInflater) {
        mInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
