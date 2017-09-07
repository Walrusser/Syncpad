package com.nordstar.afehr.syncpad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class peopleActivity extends AppCompatActivity {

    private String id;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_activity);

        id = getIntent().getStringExtra("id");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add People");
    }
}
