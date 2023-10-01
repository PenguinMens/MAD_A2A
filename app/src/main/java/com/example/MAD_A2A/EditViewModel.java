package com.example.MAD_A2A;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditViewModel extends ViewModel {
    private MutableLiveData<String> contact_first_name;
    private MutableLiveData<String> contact_last_name;
    private MutableLiveData<String> contact_email;
    private MutableLiveData<Integer> contact_phone_number;
    private MutableLiveData<Integer> value_clicked;
    private MutableLiveData<Contact> contact;
    private MutableLiveData<Integer> newProfile;
    public EditViewModel() {
        contact_first_name = new MutableLiveData<>();
        contact_last_name = new MutableLiveData<>();
        contact_email = new MutableLiveData<>();
        contact_phone_number = new MutableLiveData<>();
        value_clicked = new MutableLiveData<>();
        contact = new MutableLiveData<>();
        newProfile = new MutableLiveData<>();
        value_clicked.setValue(-1);
        newProfile.setValue(1);
    }

    public  void setContact(Contact contact)
    {
        this.contact.setValue(contact);
    }
    public Contact getContact()
    {
        return this.contact.getValue();
    }

    public void setContactFirstName(String val)
    {
        contact_first_name.setValue(val);
    }
    public void setContactLastName(String val)
    {
        contact_last_name.setValue(val);
    }
    public void setContactPhoneNumber(int val)
    {
        contact_phone_number.setValue(val);
    }
    public void setContactEmail(String val)
    {
        contact_email.setValue(val);
    }

    public String getContactFirstName()
    {
        return  contact_first_name.getValue();
    }
    public String getContactLastName()
    {
        return  contact_last_name.getValue();
    }
    public int getContactPhoneNumber()
    {
        return  contact_phone_number.getValue();
    }
    public String getContactEmail()
    {
        return  contact_email.getValue();
    }
    public MutableLiveData<Integer> getValueClicked()
    {
        return value_clicked;
    }
    public void setValueClicked(int val)
    {
        value_clicked.setValue(val);
    }
    public int getNewProfileFlag()
    {
        return value_clicked.getValue();
    }
    public void setNewProfileFlag(int val)
    {
        value_clicked.setValue(val);
    }
    public void setToBlank()
    {
        contact.setValue(null);

    }

}
