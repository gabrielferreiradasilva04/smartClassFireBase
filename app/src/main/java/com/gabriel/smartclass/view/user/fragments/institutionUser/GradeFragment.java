package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.databinding.FragmentGradeBinding;
import com.gabriel.smartclass.viewModels.ClassroomStudentMainMenuViewModel;

import org.jetbrains.annotations.NotNull;

public class GradeFragment extends Fragment {
    private FragmentGradeBinding binding;
    private ClassroomStudentMainMenuViewModel viewModel;


    public GradeFragment() {
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentGradeBinding.inflate(inflater, container, false);
        this.buildViewModel();
        return this.binding.getRoot();
    }

    private void buildRecyeclerview(){
        this.binding.rcGradefragment.setHasFixedSize(false);
        this.binding.rcGradefragment.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }



    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(ClassroomStudentMainMenuViewModel.class);
    }
}