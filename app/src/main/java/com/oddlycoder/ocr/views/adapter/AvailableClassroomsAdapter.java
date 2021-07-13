package com.oddlycoder.ocr.views.adapter;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AvailableClassroomsAdapter extends RecyclerView.Adapter<AvailableClassroomsAdapter.ViewHolder> {

    private final ArrayList<String> classrooms;

    public AvailableClassroomsAdapter(ArrayList<String> classrooms) {
        this.classrooms = classrooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.available_class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableClassroomsAdapter.ViewHolder holder, int position) {
        holder.viewDecorator(position);
        holder.bind(classrooms.get(position)); // TODO pass arguments
    }

    @Override
    public int getItemCount() {
        return classrooms.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mClassroom;
        private TextView mViewCount;

        private final ConstraintLayout mAvailableItemParent;
        private final ImageView mAvailableIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mClassroom = itemView.findViewById(R.id.classroom_id);
            mViewCount = itemView.findViewById(R.id.classroom_view_count);
            mAvailableItemParent = itemView.findViewById(R.id.available_background);
            mAvailableIcon = itemView.findViewById(R.id.ic_class_item);
        }

        public void viewDecorator(int pos) {
            switch ((pos + 1) % 3) {
                case 0:
                    // 2
                    setDecorator(R.drawable.available_class_item_background_pl, R.drawable.ic_assessment_pl);
                    break;
                case 1:
                    // 0
                    setDecorator(R.drawable.available_class_item_background, R.drawable.ic_assessment);
                    break;
                case 2:
                    // 1
                    setDecorator(R.drawable.available_class_item_background_gr, R.drawable.ic_assessment_gr);
                    break;
            }
        }

        private void setDecorator(@DrawableRes int bgColor,@DrawableRes int imgColor) {
            //mAvailableItemParent.setBackgroundTintList(ColorStateList.valueOf(bgColor));
            //mAvailableIcon.setBackgroundTintList(ColorStateList.valueOf(imgColor));
            mAvailableItemParent.setBackground(itemView.getContext().getDrawable(bgColor));
            mAvailableIcon.setImageDrawable(itemView.getContext().getDrawable(imgColor));
        }

        public void bind(String classroom) {
            mClassroom.setText(classroom);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(itemView.getContext(), mClassroom.getText(), Toast.LENGTH_SHORT).show();
            Dialog detailDialog = new AlertDialog.Builder(itemView.getContext())
                    .setView(R.layout.classroom_item_selected_dialog)
                    .show();
        }
    }

}
