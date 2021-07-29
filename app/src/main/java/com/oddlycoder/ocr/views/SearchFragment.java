package com.oddlycoder.ocr.views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.databinding.FragmentSearchBinding;
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.model.Day;
import com.oddlycoder.ocr.model.TTable;
import com.oddlycoder.ocr.viewmodel.HomeViewModel;
import com.oddlycoder.ocr.viewmodel.SearchViewModel;
import com.oddlycoder.ocr.views.adapter.SearchAdapter;
import com.oddlycoder.ocr.views.adapter.SearchListAdapter;
import com.oddlycoder.ocr.views.adapter.SearchResultAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    //private SearchViewModel searchViewModel;
    private HomeViewModel searchViewModel;  // sharing homeViewModel
    private SearchAdapter adapter;

    private FragmentSearchBinding binding;
    private View view;

    private SearchListAdapter searchListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchAdapter(Collections.emptyList());
        searchViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.searchProgressBar.setVisibility(View.GONE);
//        binding.searchResultRecyclerview.setVisibility(View.GONE);

        binding.clearEditText.setOnClickListener(listener -> binding.searchEditText.getText().clear());

        /*binding.searchResultRecyclerview.setLayoutManager(
                new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        binding.searchResultRecyclerview.setAdapter(adapter);
*/
//        searchViewModel.getClassrooms().observe(getViewLifecycleOwner(), this::initAdapter);
        searchViewModel.getClassrooms().observe(getViewLifecycleOwner(), this::initSearchListAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.searchFragDescParent.setVisibility(View.GONE);
                //adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        binding.searchEditText.addTextChangedListener(watcher);

        binding.searchEditText.setOnItemClickListener(itemClickListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private final AdapterView.OnItemClickListener itemClickListener = (adapter, view, pos, lPos) -> {
        Log.d(TAG, "adapter item clicked: at " + pos + "view: " + view);
        InputMethodManager ims = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        ims.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        binding.searchResultInclude.searchResultParent.setVisibility(View.VISIBLE);
        setSelectedInformation((Classroom) searchListAdapter.getItem(pos));
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSelectedInformation(Classroom classroom) {
        binding.searchResultInclude.classroom.setText(classroom.getClassroom());
        binding.searchResultInclude.searchResultRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        SearchResultAdapter resultAdapter = new SearchResultAdapter(classroom.getWeek());
        binding.searchResultInclude.searchResultRecyclerView.setAdapter(resultAdapter);
        resultAdapter.notifyDataSetChanged();

        //StringBuilder builder = new StringBuilder();
        /*for (Day day : classroom.getWeek()) {
            builder.append(day.getDay());
            day.getClassHours().forEach((hourkey, hourValue) -> {
                if (hourValue == null || hourValue.isEmpty()) {
                    builder.append(String.format("-- %s", hourkey));
                }
            });
            builder.append("\n");
        }*/
        // binding.searchResultInclude.dayOfWeek.setText(builder);
    }

    private void initSearchListAdapter(List<Classroom> classrooms) {
        searchListAdapter = new SearchListAdapter(
                SearchFragment.this.requireActivity(), android.R.layout.simple_list_item_1, classrooms);
        binding.searchEditText.setAdapter(searchListAdapter);
        searchListAdapter.notifyDataSetChanged();
    }

    private void initAdapter(List<Classroom> classroomList) {
        if (classroomList.isEmpty()) {
            binding.searchFragDescParent.setVisibility(View.VISIBLE);
        }
        adapter = new SearchAdapter(classroomList);
        //binding.searchResultRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
