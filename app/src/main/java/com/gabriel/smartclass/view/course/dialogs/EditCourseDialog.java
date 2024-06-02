package com.gabriel.smartclass.view.course.dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.Area;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.viewModels.SearchAndEditCourseViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Objects;

public class EditCourseDialog extends DialogFragment {
    private View view;
    private SearchAndEditCourseViewModel viewModel;
    private EditText textDescription;
    private EditText durationText;
    private EditText divisionText;
    private Spinner areaSpinner;
    private Spinner coordinatorSpinner;
    private TextView titleText;
    private ImageButton backButton;
    private Button saveButton;
    public EditCourseDialog(){
    }@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.dialog_edit_course, container, false);
        initialize();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.areaSpinner.setAdapter(null);
        this.coordinatorSpinner.setAdapter(null);
        this.backButton.setOnClickListener(null);
        this.saveButton.setOnClickListener(null);
        viewModel.getSnackBarText().removeObserver(snackbarObserve());
        viewModel.getSnackBarText().setValue(null);
        viewModel.getSpinnerAreaAdapter().getMutableLiveDataTList().setValue(new ArrayList<>());
        viewModel.getSpinnerCoordinatorAdapter().getMutableLiveDataTList().setValue(new ArrayList<>());
        this.viewModel.getSnackBarText().removeObserver(snackbarObserve());
        this.viewModel.getSnackBarText().setValue(null);
        this.viewModel = null;
    }

    public void initialize(){
        loadComponents();
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(SearchAndEditCourseViewModel.class);
        viewModel.getSnackBarText().observe(getViewLifecycleOwner(), snackbarObserve());
        populateComponents();
        backButton.setOnClickListener(backButtonClickListener());
        saveButton.setOnClickListener(saveButtonClickListener());
    }

    private View.OnClickListener saveButtonClickListener() {
        return view -> saveCourseChanges();
    }

    private void saveCourseChanges() {
        String description = textDescription.getText().toString().toLowerCase();
        String duration = durationText.getText().toString();
        String division  = divisionText.getText().toString();
        InstitutionUser coordinator = (InstitutionUser) coordinatorSpinner.getSelectedItem();
        Area area = (Area) areaSpinner.getSelectedItem();
        viewModel.saveCourseChanges(description, duration, division, coordinator, area, viewModel.getCourseEdit(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), this );

    }

    private Observer<? super String> snackbarObserve() {
        return text ->{
          if(text != null && !text.equals("")) {
              Snackbar.make(saveButton, text, Snackbar.LENGTH_SHORT).show();
          }
        };
    }

    private View.OnClickListener backButtonClickListener() {
        return view -> this.dismiss();
    }

    public void loadComponents(){
        textDescription = view.findViewById(R.id.dialog_editCourse_edtxt_description);
        durationText = view.findViewById(R.id.dialog_editCourse_edtxt_duration);
        divisionText = view.findViewById(R.id.dialog_editCourse_division);
        areaSpinner = view.findViewById(R.id.dialog_editCourse_area_spinner);
        coordinatorSpinner = view.findViewById(R.id.dialog_editCourse_coordinator_spinner);
        backButton = view.findViewById(R.id.dialog_editCourse_backbutton);
        saveButton = view.findViewById(R.id.dialog_editCourse_create_button);
        titleText = view.findViewById(R.id.dialog_editcourse_title_name);
    }
    @SuppressLint("SetTextI18n")
    public void populateComponents(){
        viewModel.initEditCourseDialog(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), viewModel.getCourseEdit(), areaSpinner, coordinatorSpinner);
        this.textDescription.setText(viewModel.getCourseEdit().getDescription());
        this.durationText.setText(Integer.toString(viewModel.getCourseEdit().getDuration()));
        this.divisionText.setText(Integer.toString(viewModel.getCourseEdit().getDivision_of_the_school_year()));
        this.titleText.setText(viewModel.getCourseEdit().getName());
    }


}
