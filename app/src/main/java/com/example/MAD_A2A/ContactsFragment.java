package com.example.MAD_A2A;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profiles#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ContactsFragment extends Fragment implements ContactViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Contact> contactsData;
    private List<Contact> rendredListData;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button addContactButton;
    private Button importContactButton;
    private  ContactsViewModel contactsViewModel;
    private static final int REQUEST_READ_CONTACT_PERMISSION = 3;
    private Contact contact;
    private String firstName;
    private String lastName;
    private String number;
    private String email;
    private byte[] img;
    private ActivityResultLauncher<Intent> pickContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    processPickContactResult(data);
                }
            });
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    System.out.printf("test treue%n");
                    // PERMISSION GRANTED
                } else {
                    System.out.printf("test fail%n");
                    // PERMISSION NOT GRANTED
                }
            }
    );

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profiles.
     */
    // TODO: Rename and change types and number of parameters

    public static ContactsFragment newInstance(String param1, String param2) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public ContactsFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.printf("does this run");
        ContactDAO contactDAO = ContactDBInstance.
                getDatabase(getContext().getApplicationContext()).contactDAO();
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsViewModel = new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
        contactsData  = contactDAO.getAllContacts();
        addContactButton = rootView.findViewById(R.id.button1);
        importContactButton = rootView.findViewById(R.id.button2);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsViewModel.setValueClicked(1);
            }
        });
        importContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                        android.Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
                }
                else
                {
                    contact = null;
                    firstName = null;
                    lastName = null;
                    number = null;
                    email = null;
                    img = null;
                    pickContactButtonClicked();
                }


            }
        });
        contactsViewModel.getValueClicked().observe(getViewLifecycleOwner(), val ->{
            if(val== 3)
            {

                contact = new Contact();
                contact.setImage(img);
                contact.setFirstName(firstName);
                contact.setEmail(email);
                contact.setLastName(lastName);
                contact.setContactPhone(number);
                try {
                    contactDAO.insert(contact);
                }
                catch (SQLiteConstraintException e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                contactsViewModel.setValueClicked(2);
            }
        });
//        for (ProfileData profilesDatum : profilesData) {
//            System.out.printf("Profile data %d%n", profilesDatum.getId());
//        }
//        for (ProfileData renderedProfile : renderedProfiles) {
//            System.out.printf("rendered: %d%n",renderedProfile.getId());
//        }
//        for (ProfileData profilesToExclude2 : profilesToExclude) {
//            System.out.printf("exclude: %d%n",profilesToExclude2.getId());
//        }
//        Log.d("profileData",String.valueOf(profilesData.size()));
        RecyclerView recyclerView = rootView.findViewById(R.id.profile_recycler_view);
        ContactViewAdapter adapter = new ContactViewAdapter(contactsData,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    public void onItemCLick(int pos, View view) {
        contactsViewModel.setProfileClicked(contactsData.get(pos).getId());
        Log.d("Profile Selected",String.valueOf(pos));
    }
    private void pickContactButtonClicked(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(ContactsContract.Contacts.CONTENT_URI);
        pickContactLauncher.launch(intent);
    }
    private void processPickContactResult(Intent data){

        int id = -1;
        Uri contactUri = data.getData();
        String[] queryFields = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,


        };

        Cursor c = getContext().getContentResolver().query(
                contactUri, queryFields, null, null, null);
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                System.out.print(c.getInt(0));// ID first
                id = c.getInt(0);
                firstName = c.getString(1); // Name second

            }

        }
        catch (android.database.CursorIndexOutOfBoundsException e)
        {

        }
        finally {
            c.close();
        }
        if(id != -1)
            getRest(id);
            contactsViewModel.setValueClicked(3);
    }
    private void getPhoneNumber(int id){
        String result="";
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(id)
        };
        Cursor c = getContext().getContentResolver().query(
                phoneUri, queryFields, whereClause,whereValues, null);
        try{
            c.moveToFirst();
            do{
                // ther ecould be multiple number formats and multiple numbers
                // only using first one

//                String phoneNumber = c.getString(0);
//                result = result+phoneNumber+" ";
                result =  c.getString(0);
            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }
        System.out.println(result);
        this.number = result;
//        phone.setText(result);
//        phone.setVisibility(View.VISIBLE);
    }
    private void getPhoto(int id){
        img = null;
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                id);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContext().getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        try{
            cursor.moveToFirst();
            do{
                System.out.println("test");
                img = cursor.getBlob(0);
                System.out.println(img);
            }
            while (cursor.moveToNext());

        }
        catch (android.database.CursorIndexOutOfBoundsException e)
        {

        }
        finally {
            cursor.close();
        }


//        phone.setText(result);
//        phone.setVisibility(View.VISIBLE);
    }
    private void getName(int id){
        String result="";
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
        };

        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(id)
        };
        Cursor c = getContext().getContentResolver().query(
                phoneUri , queryFields, whereClause,whereValues, null);
        System.out.println(c);
        try{
            c.moveToFirst();
            do{
                lastName= c.getString(0);

            }
            while (c.moveToNext());

        }
        finally {
            c.close();
        }

//        phone.setText(result);
//        phone.setVisibility(View.VISIBLE);
    }
    private void getRest(int id){
        String result="";
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] queryFields = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS
        };

        String whereClause = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
        String [] whereValues = new String[]{
                String.valueOf(id)
        };
        Cursor c = getContext().getContentResolver().query(
                emailUri, queryFields, whereClause,whereValues, null);

        try{
            c.moveToFirst();
            do{
                String emailAddress = c.getString(0);
                result = result+emailAddress+" ";
            }
            while (c.moveToNext());

        }
        catch (android.database.CursorIndexOutOfBoundsException e)
        {

        }
        finally {
            c.close();
        }
        this.email = result;
        getName(id);
        getPhoneNumber(id);
//        getPhoto(id);
    }

}