package com.oddlycoder.ocr.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Day;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder> {

    private final List<Day> week;

    public SearchResultAdapter(List<Day> week) {
        this.week = week;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_layout_recycler_item, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ResultViewHolder holder, int position) {
        holder.bind(week.get(position));
    }

    @Override
    public int getItemCount() { return week.size(); }

    protected final static class ResultViewHolder extends RecyclerView.ViewHolder {

        private TextView dayTv;
        private TextView periods;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.day_of_week);
            periods = itemView.findViewById(R.id.times_periods_textview);
        }

        public void bind(Day day) {
            dayTv.setText(day.getDay());
            periods.setText(day.getTtables().toString());
        }
    }
}
