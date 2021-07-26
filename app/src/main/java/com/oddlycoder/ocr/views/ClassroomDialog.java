package com.oddlycoder.ocr.views;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.utils.DateUtil;
import com.oddlycoder.ocr.views.adapter.UpcomingTimeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ClassroomDialog extends DialogFragment {

    public static final String TAG = "ClassroomDialog";

    public static final String DIALOG_TAG = "classroom";
    private TextView classroomName, classroomTime;
    private TextView ttlClassroom; // time to live
    private Button dialogClose;
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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.classroom_item_selected_dialog, container, false);
        classroomName = view.findViewById(R.id.dialog_classroom_name);
        classroomTime = view.findViewById(R.id.classroom_time_);
        dialogClose = view.findViewById(R.id.dialog_close);
        ttlClassroom = view.findViewById(R.id.classroom_time_current_time);
        parent = view.findViewById(R.id.dialog_parent);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogClose.setOnClickListener((listener) -> dismiss());

        if (getArguments() != null) {
            Classroom classroom = (Classroom) getArguments().getSerializable(DIALOG_TAG);
            classroomName.setText(classroom.getClassroom());
            for (Day day : classroom.getWeek()) {
                if (day.getDay().equalsIgnoreCase(DateUtil.getDayOrTime(DateUtil.DAY))) {
                    String[] times = getResources().getStringArray(R.array.upcoming_time);
                    String time = String.format("Current time: %s", DateUtil.getDayOrTime(DateUtil.TIME));
                    String upcoming = times[UpcomingTimeAdapter.getSelectedItem()];

                    String rTime;

                    try {
                        rTime = timeDifference(time, upcoming);
                    } catch (ParseException pe) {
                        Log.e(TAG, "onViewCreated: ", pe);
                    }

                    classroomTime.setText(upcoming);
                    ttlClassroom.setText(time);

                    if (day.getClassHours().containsValue("")) {
                    }

                }
            }

        }
    }

    private String timeDifference(String currentTime, String upcoming) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date c = simpleDateFormat.parse(currentTime);
        String beginningTime = splitUpcoming(upcoming);
        return beginningTime;
    }

    private String splitUpcoming(String upcoming) {
        String[] result = upcoming.split("_");
        return result[0].replace('t', ' ').trim();
    }
}


// if current time is more than selected time.
// you missed this period [red text formatting]
// else show: minutes to live [ green text formatting]