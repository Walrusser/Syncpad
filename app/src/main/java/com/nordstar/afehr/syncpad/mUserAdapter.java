package com.nordstar.afehr.syncpad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alexander Fehr on 2017-09-09.
 */

public class mUserAdapter extends ArrayAdapter<user> {

    private ArrayList<user> dataset;
    private Context context;

    public mUserAdapter(ArrayList<user> _dataset, Context _context){
        super(_context, R.layout.people_row_layout, _dataset);

        dataset = _dataset;
        context = _context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        user _user = dataset.get(position);
        View v = convertView;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.people_row_layout, null);
        }

        TextView displayNameTextView = (TextView) v.findViewById(R.id.displayNameTextView);
        displayNameTextView.setText(_user.getDisplayName());

        TextView emailTextView = (TextView) v.findViewById(R.id.emailTextView);
        emailTextView.setText(_user.getEmail());

        return v;
    }

}
