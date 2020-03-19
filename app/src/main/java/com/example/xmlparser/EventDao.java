package com.example.xmlparser;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface EventDao {

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Insert
    void insertAll(Event... events);

    @Delete
    void delete(Event event);
}
