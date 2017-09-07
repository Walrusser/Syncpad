package com.nordstar.afehr.syncpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class mainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private String uid;
    private String title;
    private ArrayList<String> contentIds = new ArrayList<String>();

    private ArrayList<note> dataset = new ArrayList<note>();

    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.main_activity);

        //TODO Add toolbar with logout and settings

        //Get the uid
        uid = auth.getCurrentUser().getUid();

        listView = (ListView) findViewById(R.id.listView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Syncpad");
        setSupportActionBar(toolbar);



        //Handlers
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO Make multiple list types available to choose

                final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                builder.setTitle("Set Title:");

                final EditText inputText = new EditText(mainActivity.this);
                inputText.setInputType(InputType.TYPE_CLASS_TEXT);

                builder.setView(inputText);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        title = inputText.getText().toString();
                        String unID = UUID.randomUUID().toString();

                        mDatabase.child("content").child(unID).child("title").setValue(title); //Create the content
                        mDatabase.child("content").child(unID).child("content").setValue(""); //Create a blank content child
                        mDatabase.child("users").child(uid).child("content").child(unID).setValue(true); //Add the content to the user and set owner to true
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
                listView.invalidateViews();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = dataset.get(i).getId();
                String title = dataset.get(i).getTitle();

                Intent intent = new Intent(mainActivity.this, editorActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("id", id);

                startActivity(intent);
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contentIds.clear();
                dataset.clear();

                for(DataSnapshot ds : dataSnapshot.child("users").child(uid).child("content").getChildren()) {
                    contentIds.add(ds.getKey());
                }

                for(String id : contentIds){
                    String title = dataSnapshot.child("content").child(id).child("title").getValue().toString();
                    dataset.add(new note(title, "Test", id));
                }
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mNoteAdapter noteAdapter = new mNoteAdapter(dataset, this);
        listView.setAdapter(noteAdapter);

    }

}
