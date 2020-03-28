package com.example.xmlparser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface EventDao {

    @Query("SELECT * FROM movie_table WHERE seen=0")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM movie_table WHERE seen=1")
    LiveData<List<Event>> getAllSeen();

    @Query("DELETE FROM movie_table")
    void deleteAll();

    @Insert
    void insertAll(Event... events);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Delete
    void delete(Event event);

    @Update
    public void update(List<Event> events);
}
