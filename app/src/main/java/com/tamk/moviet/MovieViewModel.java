package com.tamk.moviet;

import android.app.Application;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * MovieViewModel class extends the AndroidViewModel.
 * MovieViewModel is responsible for preparing and managing data
 * and handling the communication of Activities/Fragments and the database.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<Event>> allSeen;

    /**
     * Class constructor.
     *
     * @param application   the application to which lifecycle viewmodel is attached to
     */
    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allEvents = repository.getAllEvents();
        allSeen = repository.getAllSeenEvents();
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
     * Returns a list of all events in the repository marked as "seen"
     *
     * @return  list of seen events
     */
    LiveData<List<Event>> getAllSeenEvents() {
        return allSeen;
    }

    /**
     * Inserts new event to the repository.
     *
     * @param event event to be added to the repository
     */
    public void insert(Event event) {
        repository.insert(event);
    }

    /**
     * Removes the specified event from the repository.
     *
     * @param event event to be removed from the repository
     */
    public void remove(Event event) {
        repository.remove(event);
    }

    /**
     * Updates the events in the repository.
     *
     * @param events    list of events to be updated
     */
    public void update(List<Event> events) {
        repository.update(events);
    }
}
