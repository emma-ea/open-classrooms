package com.oddlycoder.ocr.views.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.views.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class UpcomingTimeAdapter extends RecyclerView.Adapter<UpcomingTimeAdapter.ViewHolder> {

    private final List<String> times;
    private final HomeFragment context;

    public UpcomingTimeAdapter(List<String> times, HomeFragment context) {
        this.times = times;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        filterCallback = (FilterCallback) context;
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
        fadeAnimation(holder.getItemView());
        holder.getParent().setOnClickListener((view) -> {
            selectedRow = position;
            notifyDataSetChanged();
        });

        if (selectedRow == position) {
            holder.getParent().setBackgroundResource(R.drawable.upcoming_time_item_selected);
            filterCallback.selectedUpcoming(selectedRow);
        } else {
            holder.getParent().setBackgroundResource(R.drawable.upcoming_time_item);
        }

    }

    public int getSelectedItem() {
        return selectedRow;
    }

    private void fadeAnimation(View itemView) {
        AlphaAnimation fadeAnim = new AlphaAnimation(0.0f, 1.0f);
        fadeAnim.setDuration(1000L);
        itemView.startAnimation(fadeAnim);
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTime;
        private final ConstraintLayout mParent;

        private final View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTime = itemView.findViewById(R.id.upcoming_time_);
            mParent = itemView.findViewById(R.id.upcoming_item_parent);
        }

        public View getItemView() {
            return itemView;
        }

        public void bind(String time) {
            mTime.setText(time);
        }

        public ConstraintLayout getParent() { return mParent; }

    }

    public FilterCallback filterCallback;

    public interface FilterCallback {
        int selectedUpcoming(int s);
    }

}
