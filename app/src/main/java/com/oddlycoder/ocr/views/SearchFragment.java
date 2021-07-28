package com.oddlycoder.ocr.views;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.oddlycoder.ocr.viewmodel.HomeViewModel;
import com.oddlycoder.ocr.viewmodel.SearchViewModel;
import com.oddlycoder.ocr.views.adapter.SearchAdapter;

import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    //private SearchViewModel searchViewModel;
    private HomeViewModel searchViewModel;  // sharing homeViewModel
    private SearchAdapter adapter;

    private FragmentSearchBinding binding;
    private View view;

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
        binding = FragmentSearchBinding.inflate(inflater);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.searchProgressBar.setVisibility(View.GONE);
        binding.searchResultRecyclerview.setVisibility(View.GONE);

        binding.clearEditText.setOnClickListener(listener -> binding.searchEditText.getText().clear());

        binding.searchResultRecyclerview.setLayoutManager(
                new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        binding.searchResultRecyclerview.setAdapter(adapter);

       searchViewModel.getClassrooms().observe(getViewLifecycleOwner(), this::initAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.searchFragDescParent.setVisibility(View.GONE);
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        binding.searchEditText.addTextChangedListener(watcher);

    }

    private void initAdapter(List<Classroom> classroomList) {
        if (classroomList.isEmpty()) {
            binding.searchFragDescParent.setVisibility(View.VISIBLE);
        }
        adapter = new SearchAdapter(classroomList);
        binding.searchResultRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
