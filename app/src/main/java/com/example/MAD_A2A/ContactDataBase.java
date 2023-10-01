package com.example.MAD_A2A;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 4)
public abstract class ContactDataBase extends RoomDatabase {
    public abstract ContactDAO contactDAO();
}

