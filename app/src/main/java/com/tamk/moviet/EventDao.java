package com.tamk.moviet;

import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * DAO interface validates the used SQL at compile-time and associates it with a method.
 * Annotations are used to represent the database operations.
 *
 * @author Laura Kanerva
 * @version     %I%, %G%
 */
@Dao
public interface EventDao {

    /**
     * Selects all objects from the specified table, where "seen" attribute is equal to false.
     *
     * @return list of Event objects
     */
    @Query("SELECT * FROM movie_table WHERE seen=0")
    LiveData<List<Event>> getAll();

    /**
     * Selects all objects from the specified table, where "seen" attribute is equal to true.
     *
     * @return list of Event objects
     */
    @Query("SELECT * FROM movie_table WHERE seen=1")
    LiveData<List<Event>> getAllSeen();

    /**
     * Deletes everything from specified table.
     */
    @Query("DELETE FROM movie_table")
    void deleteAll();

    /**
     * Inserts all given Event objects to the database.
     *
     * @param events    event(s) to be inserted to database
     */
    @Insert
    void insertAll(Event... events);

    /**
     * Adds a new Event object to the database.
     *
     * @param event     event to be added to database
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    /**
     * Removes a specified Event object from the database.
     *
     * @param event     event to be removed from database
     */
    @Delete
    void delete(Event event);

    /**
     * Updates all Event objects in the database with given list.
     *
     * @param events    changed list of events
     */
    @Update
    public void update(List<Event> events);
}
