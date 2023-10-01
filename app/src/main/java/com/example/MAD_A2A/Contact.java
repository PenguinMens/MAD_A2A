package com.example.MAD_A2A;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts", indices = {@Index(value ={"contact_phone","contact_email"}, unique = true)})
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "contact_first_name")
    private String firstName;


    @ColumnInfo(name = "contact_last_name")
    private String lastName;

    @ColumnInfo(name = "contact_email")
    private String email;
    @ColumnInfo(name = "contact_phone")
    private String contactPhone;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
