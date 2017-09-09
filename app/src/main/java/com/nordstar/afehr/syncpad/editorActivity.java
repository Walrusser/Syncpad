package com.nordstar.afehr.syncpad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editorActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Toolbar toolbar;
    private EditText mainEditText;

    private String title;
    private String id;

    private boolean editTextInUse = false;


    private ValueEventListener vel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainEditText = (EditText) findViewById(R.id.mainEditText);

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        vel = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child("content").child(id).child("content").getValue().toString().equals(mainEditText.getText().toString())){
                try{

                    //while (!editTextInUse);
                    mainEditText.setText(dataSnapshot.child("content").child(id).child("content").getValue().toString());
                    //TODO make so the text cursor dosen't jump on setText
                }
                catch (Exception e){
                    return;
                }}}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        //Handlers
        mDatabase.addValueEventListener(vel);


        mainEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editTextInUse = true;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDatabase.child("content").child(id).child("content").setValue(mainEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextInUse = false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                //TODO Switch to the settings
                return true;

            case R.id.people:
                Intent intent = new Intent(editorActivity.this, peopleActivity.class);

                intent.putExtra("id", id);

                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.editor_toolbar_menu, menu);

        return true;
    }
}
