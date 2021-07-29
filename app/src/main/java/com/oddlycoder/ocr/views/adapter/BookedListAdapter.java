package com.oddlycoder.ocr.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.BookedClassroom;

import java.util.List;

public class BookedListAdapter extends RecyclerView.Adapter<BookedListAdapter.ViewHolder> {

    private final List<BookedClassroom> bookedClassroomList;

    public BookedListAdapter(final List<BookedClassroom> bookedClassrooms) {
        this.bookedClassroomList = bookedClassrooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.booked_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedListAdapter.ViewHolder holder, int position) {
        holder.bind(bookedClassroomList.get(position));
    }

    @Override
    public int getItemCount() { return bookedClassroomList.size(); }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView classroomName, message, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classroomName = itemView.findViewById(R.id.booked_classroom);
            message = itemView.findViewById(R.id.booked_message);
            time = itemView.findViewById(R.id.booked_time);
        }

        public void bind(BookedClassroom classroom) {
            classroomName.setText(classroom.getClassroom());
            message.setText(classroom.getMessage());
            time.setText(classroom.getTime());
        }
    }

}
