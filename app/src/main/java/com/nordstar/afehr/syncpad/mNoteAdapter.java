package com.nordstar.afehr.syncpad;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander Fehr on 2017-08-06.
 */

public class mNoteAdapter extends ArrayAdapter<note> {

    private ArrayList<note> dataset;
    private Context context;

    public mNoteAdapter(ArrayList<note> _dataset, Context _context) {
        super(_context, R.layout.row_layout, _dataset);

        dataset = _dataset;
        context = _context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        note _note = dataset.get(position);
        View v = convertView;

        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.row_layout, null);
        }

        TextView title = (TextView) v.findViewById(R.id.titleTextView);
        title.setText(_note.getTitle());

        TextView subTitle = (TextView) v.findViewById(R.id.subtitleTextView);
        subTitle.setText(_note.getSubtitle());

        TextView timestamp = (TextView) v.findViewById(R.id.timestampTextView);
        timestamp.setText(_note.getId());

        return v;
    }
}
