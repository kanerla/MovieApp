package com.example.xmlparser;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
}
