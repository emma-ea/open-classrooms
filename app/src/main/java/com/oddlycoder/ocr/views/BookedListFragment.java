package com.oddlycoder.ocr.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.oddlycoder.ocr.databinding.FragmentBookedListBinding;
import com.oddlycoder.ocr.model.BookedClassroom;
import com.oddlycoder.ocr.viewmodel.BookedListViewModel;
import com.oddlycoder.ocr.views.adapter.BookedListAdapter;

import java.util.Collections;
import java.util.List;

public class BookedListFragment extends Fragment {

    private FragmentBookedListBinding binding;
    private View view;

    private BookedListAdapter adapter;

    private BookedListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BookedListAdapter(Collections.emptyList());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookedListBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(BookedListViewModel.class);
        binding.bookedProgress.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.bookedRecyclerview.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        binding.bookedRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getBooked().observe(getViewLifecycleOwner(), bookedClassrooms -> {
            binding.bookedProgress.setVisibility(View.VISIBLE);
            if (bookedClassrooms.size() > 0) {
                updateUI(bookedClassrooms);
                binding.bookedProgress.setVisibility(View.GONE);
            }
        });
    }

    public void updateUI(List<BookedClassroom> bookedClassrooms) {
        adapter = new BookedListAdapter(bookedClassrooms);
        binding.bookedRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
