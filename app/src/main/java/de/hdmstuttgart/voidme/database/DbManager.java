package de.hdmstuttgart.voidme.database;

import android.content.Context;

import androidx.room.Room;


public class DbManager {
    public static AppDatabase voidLocation;

    public static void initDb(Context context) {
        voidLocation = Room.databaseBuilder(context, AppDatabase.class, "voidLocationDB")
                .allowMainThreadQueries()
                /*.fallbackToDestructiveMigration()*/
                .build();
    }
}
