package com.tamk.moviet;

import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * MovieDatabase is an abstract class that extends the RoomDatabase.
 * Class uses the DAO to issue queries to its database.
 *
 * @author      Laura Kanerva
 * @version     %I%, %G%
 */
@Database(entities = {Event.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    private static volatile MovieDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Creates the database the first time it's accessed
     * using Room's database builder, and names it.
     * Returns a singleton to prevent having multiple instances of the same
     * database opened at the same time.
     *
     * @param context   the application context
     * @return          singleton that prevents having multiple instances of the database opened at the same time
     */
    static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "movie_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Possibility to delete all content and repopulate the database whenever the app is started.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            /*
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                EventDao dao = INSTANCE.eventDao();
                dao.deleteAll();

                Event movie = new Event("Testi pimeässä", "Test in the Darkness", "60", "2020-09-20T00:00:00", "Draama, kauhu", "Testing gone really well or really really bad.", "http://www.finnkino.fi/", "http://media.finnkino.fi/1012/Event_12647/portrait_small/B19-20_07_Jewels_1080.jpg");
                dao.insert(movie);
                movie = new Event("Toinen mahdollisuus", "Second Chance", "180", "2020-02-20T00:00:00", "Komedia, jännitys", "There's always a second chance in life. But not in programming", "http://www.dropbox.com/", "http://media.finnkino.fi/1012/Event_12888/portrait_small/Elvis-ThatsTheWayItIs_1080.jpg");
                dao.insert(movie);

            });
            */
        }
    };
}
