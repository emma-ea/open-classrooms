package com.oddlycoder.ocr.views;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.model.Classroom_rv;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.utils.IGoogleSignOut;
import com.oddlycoder.ocr.utils.WorldTimeApiClient;
import com.oddlycoder.ocr.viewmodel.HomeFragmentViewModel;
import com.oddlycoder.ocr.views.adapter.AvailableClassroomsAdapter;
import com.oddlycoder.ocr.views.adapter.UpcomingTimeAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.M)
public class HomeFragment extends Fragment {

    public static final String TAG = "co.oddlycoder.ocr";

    private HomeFragmentViewModel homeViewModel;

    private ConstraintLayout mParent;

    private RecyclerView mUpcomingTimes, mAvailableClassrooms;
    private TextView mHomeOCRText;

    private int exitCount = 0;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        mParent = view.findViewById(R.id.fragHomeParent);
        mAvailableClassrooms = view.findViewById(R.id.available_classrooms_recyclerview);
        mUpcomingTimes = view.findViewById(R.id.upcoming_times_recyclerview);
        mHomeOCRText = view.findViewById(R.id.home_ocr_text);
        getDayOfWeek();

        view.findViewById(R.id.ic_home_image).setOnClickListener(v -> {
            logout();
        });

        if (getActivity() != null)
            getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                    /*if (exitCount == 0)
                        snackMessage("Press back button again to exit app");
                    if (exitCount == 1)
                        snackMessage("Press back button one more time to exit");
                    if (exitCount >= 2)
                        getActivity().onBackPressed();
                    exitCount++;
                    // reset exitCount after 2 seconds if user has pressed back button.
                    Executors.newSingleThreadScheduledExecutor()
                            .schedule(()-> exitCount = 0, 2000L, TimeUnit.MILLISECONDS);*/
                }
            });

        initUpcoming();

        initAvailable();

        homeViewModel.getClassroom();

        // tables livedata
        classroomData();

    }

    private void initAvailable() {

        String[] classrooms = {
                "FF1", "FF2", "FF3", "FF4", "FF5", "FF6", "FF7",
                "FF8", "FF9", "FF10"
        };

        AvailableClassroomsAdapter adapter = new AvailableClassroomsAdapter(new ArrayList<>(Arrays.asList(classrooms)));
        mAvailableClassrooms.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mAvailableClassrooms.setAdapter(adapter);
    }

    private void initUpcoming() {
        //TODO: replace with data from firebase
        String[] timesList = {
                "8am - 9am",
                "9am - 10am",
                "10:30am - 11:30am",
                "11:30am - 12:30pm",
                "1pm - 2pm",
                "2pm - 3pm",
                "3pm - 4pm",
                "4pm - 5pm",
                "5pm - 6pm",
        };
        UpcomingTimeAdapter adapter = new UpcomingTimeAdapter(new ArrayList<>(Arrays.asList(timesList)));
        mUpcomingTimes.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mUpcomingTimes.setAdapter(adapter);
    }

    private void classroomData() {
        homeViewModel.getClassroomData().observe(getViewLifecycleOwner(), (observer) -> {
            for (Classroom_rv c : observer) {
               /* Log.d(TAG, "classroomData: name: " + c.getClassroom() +
                        " --> week " + Arrays.toString(c.getWeek().toArray()));*/
            }
        });
    }

    private void getDayOfWeek() {
        // if network fails.. rely on system date
        /*DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.FULL);
        Date date = Calendar.getInstance().getTime();
        String dayOfWeek = dateFormat.format(date);
        Log.d(TAG, "getDayOfWeek: Using Calendar " + dayOfWeek);
*/

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH); // i just want the day
        String date = sdf.format(c.getTime());
        Log.d(TAG, "getDayOfWeek: " + date);

        mHomeOCRText.setText(String.format("Hello, it's %s", date));
        //TODO fetch current day from web
        homeViewModel.getClock().observe(getViewLifecycleOwner(), timeObserver -> {
            mHomeOCRText.setText(String.format("Hello, it's %s", timeObserver.getDayOfTheWeek()));
        });
        Log.i(TAG, "getDayOfWeek: day set");
    }

    private void logout() {
        ((MainActivity) getActivity()).signOut();
    }

    private void snackMessage(String message) {
        if (message.isEmpty())
            message = getString(R.string.error_sigining_in);
        Snackbar.make(mParent, message, Snackbar.LENGTH_LONG).show();
    }

}
