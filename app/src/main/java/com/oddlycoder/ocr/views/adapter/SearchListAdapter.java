package com.oddlycoder.ocr.views.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.oddlycoder.ocr.model.Classroom;

import java.util.List;

public class SearchListAdapter extends ArrayAdapter<Classroom> {

    private static final String TAG = "SearchListAdapter"; 
    
    private List<Classroom> classrooms;

    public SearchListAdapter(@NonNull Context context, int resource, @NonNull List<Classroom> objects) {
        super(context, resource, objects);
        classrooms = objects;
    }
}
