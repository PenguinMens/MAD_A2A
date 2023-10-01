//package com.example.databaseroom;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class NextActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_next);
//        ContactDAO contactDAO = ContactDBInstance.
//                getDatabase(getApplicationContext()).studentDAO();
//        LinearLayout rootLinearLayout = findViewById(R.id.rootLayout);
//        List<Contact> contactList = contactDAO.getAllStudents();
//        for ( Contact contact : contactList) {
//            LinearLayout newLinearLayout = new LinearLayout(this);
//            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            LinearLayout.LayoutParams params = new LinearLayout.
//                    LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(20,20,20,20);
//
//            TextView textView = new TextView(this);
//            textView.setLayoutParams(params);
//            textView.setText(String.valueOf(contact.getId()));
//            newLinearLayout.addView(textView);
//            TextView textView1 = new TextView(this);
//            textView1.setLayoutParams(params);
//            textView1.setText(contact.getName());
//            newLinearLayout.addView(textView1);
//
//            rootLinearLayout.addView(newLinearLayout);
//        }
//        Button prev = findViewById(R.id.prev);
//        prev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(NextActivity.this,
//                        MainActivity.class);
//                startActivity(i);
//            }
//        });
//    }
//}