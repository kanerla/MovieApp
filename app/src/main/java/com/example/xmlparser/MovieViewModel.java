package com.example.xmlparser;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Event>> allEvents;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allEvents = repository.getAllEvents();
    }

    /**
     * Returns list of events in the repository.
     *
     * @return list of events in the repository.
     */
    LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    /**
     * Inserts new event to the repository.
     *
     * @param event event to be added to the repository.
     */
    public void insert(Event event) {
        repository.insert(event);
    }
}
