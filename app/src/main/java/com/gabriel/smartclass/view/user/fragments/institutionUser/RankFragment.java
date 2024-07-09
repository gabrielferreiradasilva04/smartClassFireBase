package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.databinding.FragmentRankBinding;
import com.gabriel.smartclass.viewModels.ClassroomStudentMainMenuViewModel;

public class RankFragment extends Fragment {
    private FragmentRankBinding binding;
    private ClassroomStudentMainMenuViewModel viewModel;

    public RankFragment(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentRankBinding.inflate(inflater, container, false);
        buildViewModel();
        buildRecyclerView();
        return this.binding.getRoot();
    }
    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(ClassroomStudentMainMenuViewModel.class);
    }
    public void buildRecyclerView(){
        this.binding.rcRanking.setHasFixedSize(false);
        this.binding.rcRanking.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.rcRanking.setAdapter(this.viewModel.getRankAdapter());
    }

}