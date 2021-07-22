package com.oddlycoder.ocr.views;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom;

import java.util.Arrays;

public class ClassroomDialog extends DialogFragment {

    public static final String DIALOG_TAG = "classroom";
    private TextView classroomName, classroomDes;
    private FrameLayout parent;

    public static ClassroomDialog newInstance(Classroom classroom) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DIALOG_TAG, classroom);
        ClassroomDialog dialog = new ClassroomDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classroom_item_selected_dialog, container, false);
        classroomName = view.findViewById(R.id.dialog_classroom_name);
        classroomDes = view.findViewById(R.id.classroom_des);
        parent = view.findViewById(R.id.dialog_parent);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Classroom classroom = (Classroom) getArguments().getSerializable(DIALOG_TAG);
            classroomName.setText(classroom.getClassroom());
            String week = Arrays.toString(classroom.getWeek().toArray());
            String hoursAll = classroom.getWeek().get(0).getTtables().toString();
            String res = week + "\n" + hoursAll;
            classroomDes.setText(res);
        }
    }
}
