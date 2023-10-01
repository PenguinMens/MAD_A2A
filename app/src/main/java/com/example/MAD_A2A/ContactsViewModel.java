package com.example.MAD_A2A;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactsViewModel extends ViewModel {
    private MutableLiveData<Integer> valueClicked;
    private MutableLiveData<Long> profileClicked;
//


    public ContactsViewModel(){
        valueClicked = new MediatorLiveData<Integer>();
        profileClicked = new MediatorLiveData<Long>();
        valueClicked.setValue(-1);
    }

    public LiveData<Integer> getValueClicked() {
        return valueClicked;
    }
    public LiveData<Long> getProfileClicked() {
        return profileClicked;
    }
    public void setProfileClicked(Long value) {
        profileClicked.setValue(value);
    }
    public void setValueClicked(int value) {
        valueClicked.setValue(value);
    }
}
