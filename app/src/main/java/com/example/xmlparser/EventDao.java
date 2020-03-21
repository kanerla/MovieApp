package com.example.xmlparser;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface EventDao {

    @Query("SELECT * FROM movie_table")
    LiveData<List<Event>> getAll();

    @Insert
    void insertAll(Event... events);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Event event);

    @Delete
    void delete(Event event);
}
