package com.nordstar.afehr.syncpad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        //TODO Make the ui an encoded version of the email
        String email = auth.getCurrentUser().getEmail();

        String base64Email = Base64.encodeToString(email.getBytes(), Base64.DEFAULT);
        uid = base64Email.replaceAll("\n", "");


        listView = (ListView) findViewById(R.id.peopleListView);
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
                        Log.d("Test", "test");
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

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //TODO Create toast
                return false;
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

        //TODO Split up the valueEventListener to use less download
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.settings:
                startActivity(new Intent(mainActivity.this, settingsActivity.class));
                return true;

            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(mainActivity.this, loginActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

}
