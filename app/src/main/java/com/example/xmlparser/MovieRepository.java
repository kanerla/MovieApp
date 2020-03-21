package com.example.xmlparser;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;

    MovieRepository(Application application) {
        MovieDatabase mdb = MovieDatabase.getDatabase(application);
        eventDao = mdb.eventDao();
        allEvents = eventDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Event event) {
        MovieDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }
}
