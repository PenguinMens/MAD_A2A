package com.example.MAD_A2A;

import android.content.Context;

import androidx.room.Room;

public class ContactDBInstance {
    private static ContactDataBase database;

    public static ContactDataBase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context,
                            ContactDataBase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}


