package com.example.MAD_A2A;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    void insert(Contact... contact);

    @Update
    void update(Contact... contact);

    @Delete
    void delete(Contact... contact);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();



    @Query("SELECT * FROM contacts WHERE id = :contactId")
    Contact getContactsByID(long contactId);
}



