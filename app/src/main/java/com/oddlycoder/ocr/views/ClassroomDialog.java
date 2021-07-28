package com.oddlycoder.ocr.views;

import android.content.res.Resources;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.utils.DateUtil;
import com.oddlycoder.ocr.viewmodel.ClassroomDialogViewModel;
import com.oddlycoder.ocr.views.adapter.UpcomingTimeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class ClassroomDialog extends DialogFragment {

    public static final String TAG = "ClassroomDialog";
    public static final String EARLY = "Period will be available in";
    public static final String IN = "Period already started in";
    public static final String MISSED = "You missed this period by";

    public static final String DIALOG_TAG = "classroom";
    private TextView classroomName, classroomTime;
    private TextView ttlClassroom; // time to live
    private Button dialogClose, sendMessageBtn;
    private ImageView missedSessionImg;
    private TextView missedSessionTxt;
    private FrameLayout parent;

    private ClassroomDialogViewModel viewModel;

    private int remainingHrs, remainingMins;

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
        missedSessionImg = view.findViewById(R.id.missed_session_img);
        missedSessionTxt = view.findViewById(R.id.missed_session_text);
        sendMessageBtn = view.findViewById(R.id.send_message_header);

        missedSessionImg.setVisibility(View.GONE);
        missedSessionTxt.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(this).get(ClassroomDialogViewModel.class);

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
                    String[] time24hr = getResources().getStringArray(R.array.upcoming_time_class_dialog);
                    String[] times = getResources().getStringArray(R.array.upcoming_time);

                    String time = DateUtil.getDayOrTime(DateUtil.TIME);
                    String ftime = String.format("Current time: %s", DateUtil.getDayOrTime(DateUtil.TIME));

                    String upcoming = times[UpcomingTimeAdapter.getSelectedItem()];
                    String upcoming24hr = time24hr[UpcomingTimeAdapter.getSelectedItem()];

                    String rTime = ""; // time difference

                    if (!upcoming.equalsIgnoreCase("view all")) {
                        try {
                            switch (timeDifference(time, upcoming24hr)) {
                                case EARLY:
                                    Log.d(TAG, "onViewCreated: early with " + remainingHrs + " " + remainingMins);
                                    rTime = String.format(Locale.ENGLISH, "%s %dhrs %dmins", EARLY, remainingHrs, remainingMins);
                                    break;
                                case IN:
                                    Log.d(TAG, "onViewCreated: in with " + remainingHrs + " " + remainingMins);
                                    rTime = String.format(Locale.ENGLISH,"%s %dhrs %dmins", IN, remainingHrs, remainingMins);
                                    break;
                                case MISSED:
                                    Log.d(TAG, "onViewCreated: missed  with " + remainingHrs + " " + remainingMins);
                                    rTime = String.format(Locale.ENGLISH,"%s %dhrs %dmins", MISSED, remainingHrs, remainingMins);
                                    missedSessionImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_missed));
                                    //missedSessionImg.setImageDrawable(ResourcesCompat.getDrawable(Resources., R.drawable.ic_missed));
                                    break;
                                default:
                                    Log.d(TAG, "onViewCreated: something went wrong");
                            }

                        } catch (ParseException pe) {
                            Log.e(TAG, "onViewCreated: ", pe);
                        }
                    }

                    if (!rTime.trim().isEmpty()) {
                        missedSessionImg.setVisibility(View.VISIBLE);
                        missedSessionTxt.setVisibility(View.VISIBLE);
                        missedSessionTxt.setText(rTime);
                    }

                    classroomTime.setText(upcoming);
                    ttlClassroom.setText(ftime);

                    if (day.getClassHours().containsValue("")) {
                    }

                }
            }

        }
        sendMessage();
    }

    private void sendMessage() {
        // TODO: send message
        sendMessageBtn.setOnClickListener(listener -> {
            BookedClassroom bookedClassroom =
                    new BookedClassroom();
            // TODO to view model
        });
    }

    private Session timeDifference(String currentTime, String upcoming) throws ParseException, NullPointerException {

        Session session;
        // TODO: time difference
        // add 1 hour to current time
        // if more than last of upcoming
        // user missed the session
        // if its less than the beginning, user has oppor
        // if its more than beginning and less than last, user is within session.
        String beginningTime = splitUpcoming(upcoming, SplitTimeBy.BEGINNING);
        String endingTime = splitUpcoming(upcoming, SplitTimeBy.ENDING);

        Date cTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(currentTime.trim());
        Date bTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(beginningTime.trim());
        Date eTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(endingTime.trim());

        long currentBeginningDiff = cTime.getTime() - bTime.getTime();
        long currentEndingDiff = cTime.getTime() - eTime.getTime();

        int currentBeginningResHrs = (int) ((currentBeginningDiff/(1000*60*60) % 24));
        int currentBeginningResMins = (int) ((currentBeginningDiff/(1000*60) % 60));
        int currentEndingResHrs = (int) ((currentEndingDiff/(1000*60*60) % 24));
        int currentEndingResMins = (int) ((currentEndingDiff/(1000*60) % 60));

        // negative means.. student is can use session // session will start in res hours
        // 0 means.. session time is active // session may end soon
        // positive means.. student missed session // missed session by res hours
        // 2. positive means.. student in session.. res mins into session

        // if cBTime is negative.. student is res hrs & mins before session
        // if cBTime is positive.. add to bTime.. if its less than eTime.. student
        // in session, if its more than eTime, student missed session.
        int begTimeHrs = (int) ((bTime.getTime() / (1000 * 60 * 60)) % 24);
        int begTimeMins = (int) ((bTime.getTime() / (1000 * 60) % 60));
        int eTimeHrs = (int) ((eTime.getTime() / (1000 * 60 * 60)) % 24);
        int eTimeMins = (int) ((eTime.getTime() / (1000 * 60) % 60));

        if (currentBeginningResHrs <= 0 && currentBeginningResMins <= 0) {
            session = Session.EARLY;
            remainingHrs = Math.abs(currentBeginningResHrs);
            remainingMins = Math.abs(currentBeginningResMins);
        } else if ((currentBeginningResHrs >= 0 && currentBeginningResMins <= 0)
                && ((currentBeginningResHrs + begTimeHrs) < eTimeHrs)) {
            session = Session.IN;
            remainingHrs = Math.abs(currentBeginningResHrs + begTimeHrs);
            remainingMins = Math.abs(currentBeginningResMins + begTimeMins);
            /*if ((currentBeginningResHrs + begTimeHrs) < eTimeHrs) {
                session = Session.IN;
            }*/
        } else {
            session = Session.MISSED;
            remainingHrs = Math.abs(currentBeginningResHrs);
            remainingMins = Math.abs(currentBeginningResMins);
        }

        Log.d(TAG, "timeDifference: currentBeginningDiff: Hrs: " + currentBeginningResHrs + " Mins: " + currentBeginningResMins );
        Log.d(TAG, "timeDifference: currentEndingDiff: Hrs" + currentEndingResHrs + " Mins: " + currentEndingResMins);

        try {
            Log.d(TAG, "timeDifference: beginning: " + bTime.toString() + " ending: " + eTime.toString() + " current: " + cTime.toString() );
            Log.d(TAG, "timeDifference: beginning: " + beginningTime + " ending: " + endingTime + " current: " + currentTime);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        return session;
    }

    private String splitUpcoming(String upcoming, SplitTimeBy mode) {
        String[] result = upcoming.split("-");
        if (mode == SplitTimeBy.BEGINNING)
            return result[0].replaceAll("(am|pm)", "");
        return result[1].replaceAll("(am|pm)", "");
    }

    enum SplitTimeBy { BEGINNING, ENDING }

    enum Session { EARLY, IN, MISSED}
}


// if current time is more than selected time.
// you missed this period [red text formatting]
// else show: minutes to live [ green text formatting]