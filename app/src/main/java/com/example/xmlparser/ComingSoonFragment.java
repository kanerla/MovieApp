package com.example.xmlparser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ComingSoonFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomAdapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Event> entries;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        entries = getArguments().getParcelableArrayList("comingArray");
        /*
        entries = new ArrayList<>();
        Log.d("we got", "this far");
        List<Event> allEntries = getArguments().getParcelableArrayList("eventsArray");
        moviesComingSoon(allEntries);
        */

        myAdapter = new CustomAdapter(getContext(), entries);
        recyclerView.setAdapter(myAdapter);

        setHasOptionsMenu(true);

        return view;
    }

    /*
    public void moviesComingSoon(List<Event> events) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        for (Event e : events) {
            String release = e.getRelease();
            Date releaseDate = new Date();
            try {
                releaseDate = sdf.parse(release);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (releaseDate.getTime() - now.getTime() > 0) {
                Log.d("Movies", e.getTitle());
                entries.add(e);
            }
        }
    }

     */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater mInflater) {
        MenuInflater inflater = mInflater;
        inflater.inflate(R.menu.search_menu, menu);

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
