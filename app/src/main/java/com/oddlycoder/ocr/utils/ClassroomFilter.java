package com.oddlycoder.ocr.utils;

import android.widget.Filter;

import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.views.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassroomFilter extends Filter {

    private List<Classroom> classrooms;
    private RecyclerView.Adapter adapter;

    public ClassroomFilter(List<Classroom> classrooms, RecyclerView.Adapter adapter) {
        this.classrooms = classrooms;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        List<Classroom> filteredResults = new ArrayList<>();
        if (constraint != null && constraint.length() > 0) {
            for (Classroom classroom : classrooms) {
                if (classroom.getClassroom().toLowerCase().matches(constraint.toString())) {
                    filteredResults.add(classroom);
                }
            }
            results.values = filteredResults;
            results.count = filteredResults.size();
        } else {
            results.values = classrooms;
            results.count = classrooms.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        classrooms = (List<Classroom>) results.values;
        adapter.notifyDataSetChanged();
    }

}
