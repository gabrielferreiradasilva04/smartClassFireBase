package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.databinding.ChooseclassroomLayoutBinding;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.view.user.views.institutionUser.ClassroomStudentMainMenu;
import com.gabriel.smartclass.view.user.views.institutionUser.ClassroomTeacherMainMenu;
import com.gabriel.smartclass.viewModels.InstitutionUserMainMenuViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@SuppressLint("NotifyDataSetChanged")

public class ChooseClassDialog extends DialogFragment {
    private DefaultAdapter<Classroom> adapter = new DefaultAdapter<>();
    private InstitutionUserMainMenuViewModel viewModel;
    private ChooseclassroomLayoutBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = ChooseclassroomLayoutBinding.inflate(inflater, container, false);
        initialize();
        return this.binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        this.viewModel.getClassroomMutableLiveData().setValue(new ArrayList<>());
    }

    public void initialize(){
        this.buildViewModel();
        this.buildRecyclerView();
    }

    private void buildRecyclerView() {
        binding.rcClassroom.setHasFixedSize(true);
        binding.rcClassroom.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rcClassroom.setAdapter(this.adapter);
        this.adapter.setClickListener(this::callClassView);
    }

    private void callClassView(Classroom classroom) {
        if(this.viewModel.getCurrentInstitutionUser().getUserType_id().equals(new UserTypeDAO().STUDENT_TYPE_REFERENCE)){
            Intent i = new Intent(requireActivity(), ClassroomStudentMainMenu.class);
            i.putExtra("course", this.viewModel.getSelected_course());
            i.putExtra("user", this.viewModel.getCurrentInstitutionUser());
            i.putExtra("institution", this.viewModel.getCurrentInstitution());
            i.putExtra("classroom", classroom);
            startActivity(i);
            this.dismiss();
        } else if (this.viewModel.getCurrentInstitutionUser().getUserType_id().equals(new UserTypeDAO().TEACHER_TYPE_REFERENCE)) {
            Intent i = new Intent(requireActivity(), ClassroomTeacherMainMenu.class);
            i.putExtra("course", this.viewModel.getSelected_course());
            i.putExtra("user", this.viewModel.getCurrentInstitutionUser());
            i.putExtra("institution", this.viewModel.getCurrentInstitution());
            i.putExtra("classroom", classroom);
            startActivity(i);
            this.dismiss();
        }

    }

    private void buildViewModel(){
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(InstitutionUserMainMenuViewModel.class);
        this.viewModel.getClassroomMutableLiveData().observe(this.getViewLifecycleOwner(), this.classroomObserver());
    }

    private Observer<? super List<Classroom>> classroomObserver() {
        return list ->{
          this.adapter.getMutableLiveDataT().setValue(list);
          this.adapter.notifyDataSetChanged();
        };
    }
}
