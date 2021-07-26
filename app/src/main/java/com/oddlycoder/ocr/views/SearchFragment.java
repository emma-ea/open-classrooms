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
import com.oddlycoder.ocr.model.Classroom;
import com.oddlycoder.ocr.viewmodel.SearchViewModel;
import com.oddlycoder.ocr.views.adapter.SearchAdapter;

import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchEditText;
    private LinearLayoutCompat searchDescParent;
    private SearchViewModel searchViewModel;
    private RecyclerView searchRecyclerView;
    private ProgressBar searchProgressBar;
    private ImageView clearEditText;

    private SearchAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchAdapter(Collections.emptyList());
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchDescParent = view.findViewById(R.id.search_frag_desc_parent);
        searchRecyclerView = view.findViewById(R.id.search_result_recyclerview);
        searchProgressBar = view.findViewById(R.id.search_progress_bar);
        clearEditText = view.findViewById(R.id.clear_edit_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchProgressBar.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.GONE);

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        searchRecyclerView.setAdapter(adapter);

        clearEditText.setOnClickListener((listener) -> searchEditText.getText().clear());

    }

    @Override
    public void onStart() {
        super.onStart();
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchDescParent.setVisibility(View.VISIBLE);
                // adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        searchEditText.addTextChangedListener(watcher);

    }

    private void initAdapter(List<Classroom> classroomList) {
        adapter = new SearchAdapter(classroomList);
    }
}
