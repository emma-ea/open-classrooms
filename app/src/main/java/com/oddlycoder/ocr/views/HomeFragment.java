package com.oddlycoder.ocr.views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;
import com.oddlycoder.ocr.viewmodel.HomeViewModel;
import com.oddlycoder.ocr.views.adapter.AvailableClassroomsAdapter;
import com.oddlycoder.ocr.views.adapter.UpcomingTimeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeFragment extends Fragment implements UpcomingTimeAdapter.FilterCallback {

    public static final String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    private ConstraintLayout mParent;
    private RecyclerView mUpcomingTimes, mAvailableClassrooms;
    private TextView mHomeOCRText;
    private ProgressBar mRecyclerLoading;
    private LinearLayoutCompat userMsgWeekends;
    private TextView userMsgText;

    private SwipeRefreshLayout refreshLayout;

    private AvailableClassroomsAdapter adapter;
    private UpcomingTimeAdapter upcomingAdapter;

    private List<Classroom> classroomsl = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         adapter = new AvailableClassroomsAdapter(Collections.emptyList(), getActivity());
         upcomingAdapter = new UpcomingTimeAdapter(Collections.emptyList(), this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mParent = view.findViewById(R.id.fragHomeParent);
        mAvailableClassrooms = view.findViewById(R.id.available_classrooms_recyclerview);
        mUpcomingTimes = view.findViewById(R.id.upcoming_times_recyclerview);
        mHomeOCRText = view.findViewById(R.id.booked_header);
        mRecyclerLoading = view.findViewById(R.id.recycler_progress);
        userMsgWeekends = view.findViewById(R.id.home_frag_weekend_msg);
        userMsgText = view.findViewById(R.id.msg_text_weekends);
        userMsgWeekends.setVisibility(View.GONE);
        refreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        view.findViewById(R.id.close_app).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startAuth(); // auth activity
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mRecyclerLoading.setVisibility(View.VISIBLE);


        getDayOfWeek();
        initUpcoming();

        classroomData();

        setRefreshLayout();

    }

    private void setRefreshLayout() {
        refreshLayout.setOnRefreshListener(() -> {
            classroomData();
            refreshLayout.setRefreshing(false);
        });

    }

    private void setUserMsgWeekends() {
        if (date.trim().equalsIgnoreCase("saturday") || date.equalsIgnoreCase("sunday")) {
            userMsgWeekends.setVisibility(View.VISIBLE);
            mAvailableClassrooms.setVisibility(View.GONE);
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            String userName = "";
            if (fUser != null) {
                userName = fUser.getDisplayName();
            }
            String msg = String.format(
                    "Hello %s, Classrooms are not available on %ss",
                    userName,
                    date);
            userMsgText.setText(msg);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAvailable(List<Classroom> classrooms) {
        adapter = new AvailableClassroomsAdapter(classrooms, getActivity());
        mAvailableClassrooms.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAvailableClassrooms.setAdapter(adapter);
        mAvailableClassrooms.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyDataSetChanged();
    }

    private void initUpcoming() {
        String[] times = getResources().getStringArray(R.array.upcoming_time);
        upcomingAdapter = new UpcomingTimeAdapter(new ArrayList<>(Arrays.asList(times)), this);
        mUpcomingTimes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mUpcomingTimes.setAdapter(upcomingAdapter);
        mUpcomingTimes.setItemAnimator(new DefaultItemAnimator());
    }

    private void classroomData() {
        homeViewModel.getClassrooms().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                classroomsl = classrooms;
                initAvailable(filterList(classrooms, filterId));

                if (!classrooms.isEmpty())
                    mRecyclerLoading.setVisibility(View.GONE);

//                if (classrooms.size() == 0) { }

                if (mRecyclerLoading.getVisibility() == View.GONE) {
                    setUserMsgWeekends();
                }

            }
        });
    }

    private List<Classroom> filterList(List<Classroom> classrooms, int filterId) {
        List<Classroom> newClassroomList = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            for (Day day : classroom.getWeek()) {
                if (day.getDay().trim().equalsIgnoreCase(date.trim())) {
                    TTable tTable = day.getTtables();
                    switch (filterId) {
                        case 1: {
                            if (tTable.getT8_9() == null || tTable.getT8_9().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 2: {
                            if (tTable.getT9_10() == null || tTable.getT9_10().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 3: {
                            if (tTable.getT1030_1130() == null || tTable.getT1030_1130().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 4: {
                            if (tTable.getT1130_1230() == null || tTable.getT1130_1230().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 5: {
                            if (tTable.getT13_14() == null || tTable.getT13_14().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 6: {
                            if (tTable.getT14_15() == null || tTable.getT14_15().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 7: {
                            if (tTable.getT15_16() == null || tTable.getT15_16().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 8: {
                            if (tTable.getT16_17() == null || tTable.getT16_17().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        case 9: {
                            if (tTable.getT17_18() == null || tTable.getT17_18().equals("")) {
                                newClassroomList.add(classroom);
                            }
                        }
                        break;
                        default: {
                            newClassroomList = classrooms;
                        }
                    }
                }

                String dday = day.getDay().trim();
                if (dday.equalsIgnoreCase("saturday") || dday.equalsIgnoreCase("sunday")) {
                    //TODO: alert user with dialog, classrooms not available on weekends
                }
            }
        }
        return newClassroomList;
    }

    private String date;

    private void getDayOfWeek() {
        // use local date if web fails
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH); // i just want the day
        date = sdf.format(c.getTime());

        mHomeOCRText.setText(String.format("Hello, it's %s", date));

        homeViewModel.getClock().observe(getViewLifecycleOwner(), timeObserver -> {
            mHomeOCRText.setText(String.format("Hello, it's %s", timeObserver.getDayOfTheWeek()));
        });
    }

    private void logout() {
        callback.logout();  // todo: handle response from logout and run startAuthActivity
    }

    private void startAuth() {
        callback.startAuthActivity();
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParent, message, Snackbar.LENGTH_LONG).show();
    }

    private int filterId = 0;

    @Override
    public int selectedUpcoming(int s) {
        Log.d(TAG, "selectedUpcoming: i: " + s);
        initAvailable(filterList(classroomsl, s));
        filterId = s;
        return s;
    }

    interface HomeCallbacks {
        void logout();
        void startAuthActivity();
    }

    private HomeCallbacks callback = null;

    private int exitCount = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (HomeCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
