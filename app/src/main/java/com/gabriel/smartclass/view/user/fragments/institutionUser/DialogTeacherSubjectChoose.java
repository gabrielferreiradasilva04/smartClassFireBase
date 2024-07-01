package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.adapter.DefaultAdapter;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.DialogchooseteachersubjectBinding;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.view.user.views.institutionUser.ClassroomTeacherManagement;
import com.gabriel.smartclass.viewModels.ClassroomTeacherMainMenuViewModel;

import java.util.List;

public class DialogTeacherSubjectChoose extends DialogFragment {
    private ClassroomTeacherMainMenuViewModel viewModel;
    private DialogchooseteachersubjectBinding binding;
    private DefaultAdapter<Subject> adapter = new DefaultAdapter<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DialogchooseteachersubjectBinding.inflate(inflater, container, false);
        this.buildViewModel();
        this.initialize();
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.viewModel.getTeachersSubjects().removeObserver(this.subjectObserver());
    }

    public void initialize(){
        this.binding.rcSubject.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.rcSubject.setHasFixedSize(true);
        this.binding.rcSubject.setAdapter(this.adapter);
        this.adapter.setClickListener(this.onSubjectClick());
    }

    private DefaultClickListener<Subject> onSubjectClick() {
        return subject -> {
            Intent i = new Intent(this.getActivity(), ClassroomTeacherManagement.class);
            i.putExtra("course", this.viewModel.getCourse());
            i.putExtra("subject", subject);
            i.putExtra("student", this.viewModel.getStudent());
            i.putExtra("institution", this.viewModel.getInstitution());
            i.putExtra("classroom", this.viewModel.getClassroom());
            i.putExtra("teacher", this.viewModel.getInstitutionUser());
            startActivity(i);
            this.dismiss();
        };
    }

    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(ClassroomTeacherMainMenuViewModel.class);
        this.viewModel.getTeachersSubjects().observe(this.getViewLifecycleOwner(), this.subjectObserver());
    }

    private Observer<? super List<Subject>> subjectObserver() {
        return list ->{
          this.adapter.getMutableLiveDataT().setValue(list);
          this.adapter.notifyDataSetChanged();
        };
    }
}
