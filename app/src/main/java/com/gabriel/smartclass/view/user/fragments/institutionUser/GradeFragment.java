package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.databinding.EmptyRcGradelayoutBinding;
import com.gabriel.smartclass.databinding.FragmentGradeBinding;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
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
        buildRecyclerView();
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        EmptyRcGradelayoutBinding viewEmpty = binding.emptyGradecontainer;
        EmptyRecyclerViewObserver observer = new EmptyRecyclerViewObserver(binding.rcGradefragment, viewEmpty.getRoot());
        viewModel.getGradeAdapter().registerAdapterDataObserver(observer);
        viewModel.getGradeAdapter().notifyDataSetChanged();
    }

    private void buildRecyclerView(){
        this.binding.rcGradefragment.setHasFixedSize(false);
        this.binding.rcGradefragment.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.binding.rcGradefragment.setAdapter(viewModel.getGradeAdapter());
        this.viewModel.getGradeAdapter().registerAdapterDataObserver(this.adapterObserver());
    }

    private RecyclerView.AdapterDataObserver adapterObserver() {
        return new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }
        };
    }


    public void buildViewModel(){
        this.viewModel = new ViewModelProvider(requireActivity()).get(ClassroomStudentMainMenuViewModel.class);
    }
}