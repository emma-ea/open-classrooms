package com.oddlycoder.ocr.views.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;

import java.util.ArrayList;

public class UpcomingTimeAdapter extends RecyclerView.Adapter<UpcomingTimeAdapter.ViewHolder> {

    private final ArrayList<String> times;

    public UpcomingTimeAdapter(ArrayList<String> times) {
        this.times = times;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.upcoming_time_item, parent, false);
        return new ViewHolder(view);
    }

    int selectedRow = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull UpcomingTimeAdapter.ViewHolder holder, int position) {
        holder.bind(times.get(position));
        holder.getParent().setOnClickListener((view) -> {
            selectedRow = position;
            notifyDataSetChanged();
        });

        if (selectedRow == position) {
            holder.getParent().setBackgroundResource(R.drawable.upcoming_time_item_selected);
        } else {
            holder.getParent().setBackgroundResource(R.drawable.upcoming_time_item);
        }

    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTime;
        private final ConstraintLayout mParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.upcoming_time_);
            mParent = itemView.findViewById(R.id.upcoming_item_parent);
        }

        public void bind(String time) {
            mTime.setText(time);
        }

        public ConstraintLayout getParent() { return mParent; }

    }

}
