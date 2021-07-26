package com.oddlycoder.ocr.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.utils.ClassroomFilter;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private final List<Classroom> classroomList;
    private ClassroomFilter filter;

    public SearchAdapter(List<Classroom> classrooms) {
        classroomList = classrooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.bind(classroomList.get(position));
    }

    @Override
    public int getItemCount() { return classroomList.size(); }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ClassroomFilter(classroomList, this);
        return filter;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView searchedClassroom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchedClassroom = itemView.findViewById(R.id.search_item_);
        }

        public void bind(Classroom classroom) {
            searchedClassroom.setText(classroom.getClassroom());
        }
    }
}
