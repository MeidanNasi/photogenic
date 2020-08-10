package com.colman.photogenic.sql;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.colman.photogenic.model.Photo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Photo.class}, version = 1, exportSchema = false)
public abstract class PhotoRoomDatabase extends RoomDatabase {

    public static Date RoomLastUpdate;
    private static Query mQuery;
    private static DocumentReference ref;
    public abstract PhotoDao photoDao();
    private static volatile PhotoRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static PhotoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PhotoRoomDatabase.class, "photos_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                PhotoDao dao = INSTANCE.photoDao();


                //dao.deleteAll();


            });
        }
    };



}
