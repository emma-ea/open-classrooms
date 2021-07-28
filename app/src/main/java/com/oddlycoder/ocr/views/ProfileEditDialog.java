package com.oddlycoder.ocr.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oddlycoder.ocr.R;
import com.oddlycoder.ocr.databinding.ProfileEditBinding;
import com.oddlycoder.ocr.model.Student;
import com.oddlycoder.ocr.viewmodel.ProfileEditDialogViewModel;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditDialog extends DialogFragment {

    public static final String STUDENT_DATA = "student-data";

    private ProfileEditBinding binding;
    private View view;

    private Student student;

    private ProfileEditDialogViewModel viewModel;

    public static ProfileEditDialog newInstance(Student student) {
        ProfileEditDialog profileEditDialog = new ProfileEditDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(STUDENT_DATA, student);
        profileEditDialog.setArguments(bundle);
        return profileEditDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            student = (Student) getArguments().getSerializable(STUDENT_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = ProfileEditBinding.inflate(inflater);
        view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(ProfileEditDialogViewModel.class);
        binding.profileEditProgressUpdate.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeFields();

        binding.buttonProfileEdit.setOnClickListener(view1 -> {
            String username = binding.usernameProfileEdit.getText().toString().trim();
            String courseProgram = binding.courseProgramProfileEdit.getText().toString().trim();
            String year = binding.yearProfileEdit.getText().toString().trim();

            if (username.equalsIgnoreCase(student.getUsername())
                    && courseProgram.equalsIgnoreCase(student.getCourseProgram())
                    && year.equalsIgnoreCase(student.getYear())) {
                this.dismiss();
            } else {
                binding.profileEditProgressUpdate.setVisibility(View.VISIBLE);
                student.setUsername(username);
                student.setCourseProgram(courseProgram);
                student.setYear(year);
                updateProfile();
            }
        });
    }

    private void initializeFields() {
        binding.usernameProfileEdit.setText(student.getUsername());
        binding.courseProgramProfileEdit.setText(student.getCourseProgram());
        binding.yearProfileEdit.setText(student.getYear());
    }

    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uuid = user.getUid();
            Map<String, String> data = new HashMap<>();
            data.put("username", student.getUsername());
            data.put("courseProgram", student.getCourseProgram());
            data.put("year", student.getYear());
            data.put("email", student.getEmail());
            data.put("fullName", student.getFullName());
            data.put("profileImageUrl", student.getProfileImageUrl());
            viewModel.updateProfile(uuid, data).observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean) {
                    Toast.makeText(this.getActivity(), "Update Success", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(STUDENT_DATA, student);
                    this.getParentFragmentManager().setFragmentResult(STUDENT_DATA, bundle);
                    ProfileEditDialog.this.dismiss();
                }
                binding.profileEditProgressUpdate.setVisibility(View.GONE);
            });
        }
    }
}
