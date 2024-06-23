package com.gabriel.smartclass.view.user.fragments.institutionUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gabriel.smartclass.databinding.DialogAddmembersOnclassBinding;

public class DialogAddMembersOnClass extends DialogFragment {
    private DialogAddmembersOnclassBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DialogAddmembersOnclassBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    public void initialize(){
        this.buildViewModel();
        this.buildRecyclerView();
    }

    private void buildViewModel() {
    }

    private void buildRecyclerView() {
    }
}
