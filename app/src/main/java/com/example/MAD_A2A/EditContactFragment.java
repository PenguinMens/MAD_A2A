package com.example.MAD_A2A;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private ImageView contactPic;
    private Button saveButton;
    private Button deleteButton;
    private Contact contact;
    private Bitmap contactImage;
    public EditContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditContactFragment newInstance(String param1, String param2) {
        EditContactFragment fragment = new EditContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_edit_contant, container, false);
        System.out.println(view);
        EditViewModel editViewModel = new ViewModelProvider(getActivity()).get(EditViewModel.class);
        deleteButton = view.findViewById(R.id.delete_button);
        saveButton = view.findViewById(R.id.save_button);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone_number);
        contactPic = view.findViewById(R.id.contact_pic);
        contact = editViewModel.getContact();
        contactImage = null;
        if(contact != null)
        {
            System.out.printf("i guess this doesnt trigger ;9?%n");
            firstName.setText(contact.getFirstName());
            lastName.setText(contact.getLastName());
            email.setText(contact.getEmail());
            phone.setText(String.valueOf(contact.getContactPhone()));
            deleteButton.setVisibility(View.VISIBLE);
            byte[] img = contact.getImage();
            if(img!= null)
            {
                contactImage = BitmapFactory.decodeByteArray(img,0,img.length);
                contactPic.setImageBitmap(contactImage);
            }
        }
        contactPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureLauncher.launch(takePictureIntent);
//                dispatchTakePictureIntent();
            }
        });
        ContactDAO contactDAO = ContactDBInstance.getDatabase(getContext().getApplicationContext()).contactDAO();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactDAO.delete(contact);
                editViewModel.setValueClicked(1);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    contact = createContact(view);
                    int newProfileCreating = editViewModel.getNewProfileFlag();
                    if(newProfileCreating == 1)
                        contactDAO.insert(contact);
                    else
                        contactDAO.update(contact);
                    editViewModel.setValueClicked(1);
                    Toast.makeText(getContext(), "created :3", Toast.LENGTH_SHORT).show();
                }
                catch (FieldNotEnteredException e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (SQLiteConstraintException e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    public Contact createContact(View view) throws FieldNotEnteredException
    {
        String firstNameText =firstName.getText().toString();
        System.out.printf("firstNameText: %s%n", firstNameText);
        if(firstNameText.equals(""))
            throw new FieldNotEnteredException("First Name not Entered");
        String phoneNumber = phone.getText().toString();
        String emailText = email.getText().toString();
        System.out.printf("PhoneNumber: %s%n", phoneNumber);
        if(!isValidMobile(phoneNumber))
            throw new FieldNotEnteredException("Phone Number is not valid");
        if(!isValidMail(emailText) && !emailText.equals(""))
            throw new FieldNotEnteredException("Email is not valid");
//        if(!isValidMobile(phoneNumber) && !isValidMail(emailText))
//            throw new FieldNotEnteredException("Phone Number or Email is not valid");

//        if(!isValidMail(emailText) && isValidMobile(phoneNumber) )
//            throw new FieldNotEnteredException("Email is not valid");

        if(contact == null)
            contact = new Contact();

        if(contactImage != null)
        {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            contactImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            contact.setImage(byteArray);

        }
        contact.setContactPhone(phoneNumber);
        contact.setEmail(emailText);
        contact.setFirstName(firstNameText);
        contact.setLastName(lastName.getText().toString());

        return contact;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivity(takePictureIntent);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    ActivityResultLauncher<Intent> pictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
               if(result.getResultCode() == RESULT_OK){
                   Intent data = result.getData();
                   Bitmap image = (Bitmap) data.getExtras().get("data");
                   if(image!=null)
                   {
                       contactPic.setImageBitmap(image);
                       contactImage = image;
                   }
               }

            });

    // methods from
    // https://stackoverflow.com/questions/22505336/email-and-phone-number-validation-in-android
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
