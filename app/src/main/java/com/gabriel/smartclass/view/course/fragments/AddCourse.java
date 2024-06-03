package com.gabriel.smartclass.view.course.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.databinding.FragmentAddCourseBinding;
import com.gabriel.smartclass.viewModels.AddCourseViewModel;
import com.gabriel.smartclass.viewModels.factorys.AddCourseViewModelFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class AddCourse extends Fragment {
    private AddCourseViewModel viewModel;
    private FragmentAddCourseBinding binding;
    private EditText editTextname;
    private EditText editTextDescription;
    private EditText editTextDuration;
    private EditText editTextDivision;
    private Spinner areaSpinner;
    private Spinner coordinatorSpinner;
    private ImageButton inflateAddAreaDialogButton;
    private Button createCourse;


    public Button getCreateCourse() {
        return createCourse;
    }

    public EditText getEditTextname() {
        return editTextname;
    }

    public EditText getEditTextDescription() {
        return editTextDescription;
    }

    public EditText getEditTextDuration() {
        return editTextDuration;
    }

    public EditText getEditTextDivision() {
        return editTextDivision;
    }

    public Spinner getAreaSpinner() {
        return areaSpinner;
    }

    public Spinner getCoordinatorSpinner() {
        return coordinatorSpinner;
    }

    public ImageButton getInflateAddAreaDialogButton() {
        return inflateAddAreaDialogButton;
    }

    public AddCourse() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    public void loadComponents() {
        this.editTextname = binding.addCourseEdtxtName;
        this.editTextDescription = binding.addCourseEdtxtDescription;
        this.editTextDivision = binding.addCourseDivision;
        this.editTextDuration = binding.addCourseEdtxtDuration;
        this.areaSpinner = binding.addCourseAreaSpinner;
        this.coordinatorSpinner = binding.addCourseCoordinatorSpinner;
        this.inflateAddAreaDialogButton = binding.addCourseCreatenewareaButton;
        this.createCourse = binding.addCourseCreateButton;
    }

    public void initialize() {
        AddCourseViewModelFactory factory = new AddCourseViewModelFactory(this);
        ViewModelProvider provider = new ViewModelProvider(requireActivity(), factory);
        viewModel = provider.get(AddCourseViewModel.class);
        loadComponents();
        this.inflateAddAreaDialogButton.setOnClickListener(inflateAddAreaDialogListener());
        viewModel.getSnackbarText().observe(getViewLifecycleOwner(), snakbarObserver());
        viewModel.getAllAreasAndpopulateSpinner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        viewModel.getCoordinatorsAndPopulateSpinner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        createCourse.setOnClickListener(createCourseClickListener());
    }


    private View.OnClickListener createCourseClickListener() {
        return view -> {
            viewModel.createNewCourse(FirebaseAuth.getInstance().getCurrentUser().getUid());
        };
    }

    private Observer<? super String> snakbarObserver() {
        return text -> {
            Snackbar.make(getContext(), binding.addCourseCreateButton, text, Snackbar.LENGTH_SHORT).show();
        };
    }

    private View.OnClickListener inflateAddAreaDialogListener() {
        return view -> {
            inflateAddAreaDialog();
        };
    }

    public void inflateAddAreaDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setCancelable(false);
        final View popUp = getLayoutInflater().inflate(R.layout.dialog_addnewarea_course, null);
        dialogBuilder.setView(popUp);
        Button buttonConfirm = popUp.findViewById(R.id.add_new_area_button_confirm);
        Button buttonDone = popUp.findViewById(R.id.add_new_area_button_cancel);
        EditText editTextDescription = popUp.findViewById(R.id.add_new_area_edtxt_description);
        Dialog dialog = dialogBuilder.create();
        dialog.show();
        buttonDone.setOnClickListener(view -> {
            dialog.dismiss();
            dialog.setOnDismissListener(listener -> {
                buttonDone.setOnClickListener(null);
                buttonConfirm.setOnClickListener(null);
                listener.cancel();
            });
        });
        buttonConfirm.setOnClickListener(v2 -> {
            addNewArea(editTextDescription);
        });
    }
    private void addNewArea(EditText editTextDescription) {
        viewModel.addNewArea(editTextDescription.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        editTextDescription.setText("");
    }



}