package com.nordstar.afehr.syncpad;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class peopleActivity extends AppCompatActivity {

    private String id;
    private Toolbar toolbar;

    private DatabaseReference mDataBase;

    private FloatingActionButton floatingActionButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);

        id = getIntent().getStringExtra("id");

        //TODO Create a list with all the users

        mDataBase = FirebaseDatabase.getInstance().getReference();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("People");

        floatingActionButton = (FloatingActionButton) findViewById(R.id.peopleFloatActionButton);
        emailEditText = (EditText) findViewById(R.id.targetEmailEditText);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetUserId = Base64.encodeToString(emailEditText.getText().toString().getBytes(), Base64.DEFAULT).replaceAll("\n", "");

                mDataBase.child("users").child(targetUserId).child("content").child(id).setValue(false);
                //TODO Make sure that the target user exists
            }
        });
    }
}
