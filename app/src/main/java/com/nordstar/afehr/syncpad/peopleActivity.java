package com.nordstar.afehr.syncpad;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class peopleActivity extends AppCompatActivity {

    private String id;
    private String uid;
    private Toolbar toolbar;
    private ListView listView;

    private DatabaseReference mDataBase;
    private FirebaseAuth auth;

    private FloatingActionButton floatingActionButton;
    private EditText emailEditText;

    private ArrayList<user> userArrayList = new ArrayList<user>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);

        id = getIntent().getStringExtra("id");

        //TODO Create a list with all the users

        mDataBase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        uid = Base64.encodeToString(auth.getCurrentUser().getEmail().getBytes(), Base64.DEFAULT).replace("\n", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("People");

        floatingActionButton = (FloatingActionButton) findViewById(R.id.peopleFloatActionButton);
        listView = (ListView) findViewById(R.id.peopleListView);
        emailEditText = (EditText) findViewById(R.id.targetEmailEditText);

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String targetUserId = Base64.encodeToString(emailEditText.getText().toString().getBytes(), Base64.DEFAULT).replaceAll("\n", "");

                //TODO Check if user exists before adding

                mDataBase.child("users").child(targetUserId).child("content").child(id).setValue(false);
                mDataBase.child("users").child(uid).child("content").child(id).child("users").child(targetUserId).setValue(false);

            }
        });

        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userArrayList.clear();

                for(DataSnapshot ds : dataSnapshot.child("users").child(uid).child("content").child(id).child("users").getChildren()){
                    String decodedEmail = new String(Base64.decode(ds.getKey(), Base64.DEFAULT), StandardCharsets.UTF_8);

                    userArrayList.add(new user("Test", decodedEmail));
                }

                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserAdapter userAdapter = new mUserAdapter(userArrayList, this);
        listView.setAdapter(userAdapter);
    }
}
