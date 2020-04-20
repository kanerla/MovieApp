package com.tamk.moviet;

import android.app.Application;
import java.util.List;
import androidx.lifecycle.LiveData;

/**
 * MovieRepository class manages queries and uses data cached in the local database.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
public class MovieRepository {
    private EventDao eventDao;
    private LiveData<List<Event>> allEvents;
    private LiveData<List<Event>> allSeen;

    /**
     * Class constructor.
     *
     * @param application   the application running the repository and database
     */
    MovieRepository(Application application) {
        MovieDatabase mdb = MovieDatabase.getDatabase(application);
        eventDao = mdb.eventDao();
        allEvents = eventDao.getAll();
        allSeen = eventDao.getAllSeen();
    }

    /**
     * Returns all events in the database.
     *
     * @return  wide list of Event objects
     */
    LiveData<List<Event>> getAllEvents() {
        return allEvents;
    }

    /**
     * Returns all events in the database marked as "seen".
     *
     * @return  filtered list of Event objects
     */
    LiveData<List<Event>> getAllSeenEvents() {
        return allSeen;
    }

    /**
     * Inserts the given event to the database.
     * Must be called on a non-UI thread.
     *
     * @param event     event to be inserted
     */
    void insert(Event event) {
        MovieDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.insert(event);
        });
    }

    /**
     * Asks the DAO to delete the specified event.
     *
     * @param event     event to be removed
     */
    void remove(Event event) {
        MovieDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.delete(event);
        });
    }

    /**
     * Sends an updated list of Event objects to the DAO to be updated in the database.
     *
     * @param events    updated list of events
     */
    void update(List<Event> events) {
        MovieDatabase.databaseWriteExecutor.execute(() -> {
            eventDao.update(events);
        });
    }
}
