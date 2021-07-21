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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom;
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
public class HomeFragment extends Fragment {

    public static final String TAG = "co.oddlycoder.ocr";

    private HomeViewModel homeViewModel;

    private ConstraintLayout mParent;
    private RecyclerView mUpcomingTimes, mAvailableClassrooms;
    private TextView mHomeOCRText;
    private ProgressBar mRecyclerLoading;

    private AvailableClassroomsAdapter adapter = new AvailableClassroomsAdapter(Collections.emptyList());
    private UpcomingTimeAdapter upcomingAdapter = new UpcomingTimeAdapter(Collections.emptyList());

    private List<Classroom> classrooms = new ArrayList<>() ;

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
        mHomeOCRText = view.findViewById(R.id.home_ocr_text);
        mRecyclerLoading = view.findViewById(R.id.recycler_progress);

        view.findViewById(R.id.close_app).setOnClickListener(v -> {
            logout();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mRecyclerLoading.setVisibility(View.VISIBLE);

        initUpcoming();
        getDayOfWeek();

        // tables livedata
        classroomData();

        getUpcomingSelected();  // todo: filter primer

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initAvailable(List<Classroom> classrooms) {
         adapter = new AvailableClassroomsAdapter(classrooms);
        mAvailableClassrooms.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAvailableClassrooms.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initUpcoming() {
        String[] times = getResources().getStringArray(R.array.upcoming_time);
        upcomingAdapter = new UpcomingTimeAdapter(new ArrayList<>(Arrays.asList(times)));
        mUpcomingTimes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mUpcomingTimes.setAdapter(upcomingAdapter);
    }

    private void getUpcomingSelected() {
        // TODO: filter available list
        int s = upcomingAdapter.getSelectedItem();
        Log.d(TAG, "getUpcomingSelected: selected: " + s);
    }

    private void filterClassrooms(int byTime) {

    }

    private void classroomData() {
       /* homeViewModel.getClassroomData().observe(getViewLifecycleOwner(), (list) -> {
            initAvailable(list); // classrooms
            adapter.notifyDataSetChanged();
            for (Classroom c : list) {
                Log.d(TAG, "classroomData: homeFrag name: " + c.getClassroom());
                for (Day day : c.getWeek()) {
                  //  Log.d(TAG, "classroomData: homeFrag day: " + day.getDay() + " --> ttables: " + day.getTtables());
                }
            }
        });*/

        homeViewModel.sgetClassroom().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                initAvailable(classrooms);
                if (classrooms != null || !classrooms.isEmpty())
                    mRecyclerLoading.setVisibility(View.GONE);
                /*for (Classroom c : classrooms) {
                    Log.d(TAG, "service classroomData: homeFrag name: " + c.getClassroom());
                }*/
            }
        });
    }

    private void getDayOfWeek() {

        // use local date if web fails
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH); // i just want the day
        String date = sdf.format(c.getTime());
        Log.d(TAG, "getDayOfWeek: " + date);

        mHomeOCRText.setText(String.format("Hello, it's %s", date));

        homeViewModel.getClock().observe(getViewLifecycleOwner(), timeObserver -> {
            mHomeOCRText.setText(String.format("Hello, it's %s", timeObserver.getDayOfTheWeek()));
        });
        Log.i(TAG, "getDayOfWeek: day set");
    }

    private void logout() {
        callback.logout();  // todo: handle response from logout and run startAuthActivity
        callback.startAuthActivity();
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParent, message, Snackbar.LENGTH_LONG).show();
    }

    interface HomeCallbacks {
        void logout();
        void startAuthActivity();
    }

    private HomeCallbacks callback = null;

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
