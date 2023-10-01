package com.example.MAD_A2A;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();

        ContactsViewModel contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        EditViewModel editViewModel = new ViewModelProvider(this).get(EditViewModel.class);
        ContactDAO contactDAO = ContactDBInstance.getDatabase(getApplicationContext()).contactDAO();


        fm.beginTransaction()
                .replace(R.id.main_frame_layout, ContactsFragment.class,null, "CONTACTS")
                .commit();
        contactsViewModel.getValueClicked().observe(this, val ->{
            if(val == 1){
                editViewModel.setToBlank();
                editViewModel.setNewProfileFlag(1);
                fm.beginTransaction()
                        .replace(R.id.main_frame_layout,EditContactFragment.class,null, "EDITS")
                        .addToBackStack("edit")
                        .commit();
                val = -1;
            }
            else if (val ==2)
            {
                fm.beginTransaction()
                        .replace(R.id.main_frame_layout, ContactsFragment.class,null, "CONTACTS")
                        .commit();
                val = -1;
            }
        });
        contactsViewModel.getProfileClicked().observe(this,val ->{
            if(val != -1)
            {
                Contact contact = contactDAO.getContactsByID(val);
                editViewModel.setContact(contact);
                editViewModel.setNewProfileFlag(0);
                fm.beginTransaction()
                        .replace(R.id.main_frame_layout,EditContactFragment.class,null, "EDITS")
                        .addToBackStack("edit")
                        .commit();
                val = -1l;
            }
        });
        editViewModel.getValueClicked().observe(this, val ->{
            if(val!=-1)
            {
                fm.popBackStack();
                val = -1;
            }
        });

        fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                Log.v("FragXX9", f.toString());
                for (Fragment fragment : fm.getFragments()) {
                    System.out.println(fragment);
                }
            }

        },true);
    }
}