package com.gabriel.smartclass.view.user.views.institutionUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.CourseMembersSimpleAdapter;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.ActivityClassroomTeacherMainMenuBinding;
import com.gabriel.smartclass.model.CourseMember;
import com.gabriel.smartclass.view.course.views.TimeTableView;
import com.gabriel.smartclass.view.user.fragments.institutionUser.DialogTeacherSubjectChoose;
import com.gabriel.smartclass.viewModels.ClassroomTeacherMainMenuViewModel;

import java.util.List;

public class ClassroomTeacherMainMenu extends AppCompatActivity {
    private CourseMembersSimpleAdapter<CourseMember> studentAdapter = new CourseMembersSimpleAdapter<>();
    private ActivityClassroomTeacherMainMenuBinding binding;
    private ClassroomTeacherMainMenuViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityClassroomTeacherMainMenuBinding.inflate(getLayoutInflater());
        this.buildViewModel();
        this.initialize();
        this.buildMenu();
        setContentView(binding.getRoot());
    }

    private void initialize() {
        this.binding.rcTeachermanagementStudents.setHasFixedSize(false);
        this.binding.rcTeachermanagementStudents.setLayoutManager(new LinearLayoutManager(this));
        this.binding.rcTeachermanagementStudents.setAdapter(this.studentAdapter);
        this.viewModel.getStudents().observe(this, this.studentsObserver());
        this.studentAdapter.setClickListener(this.OnTeacherClick());
    }

    private void buildViewModel() {
        this.viewModel = new ViewModelProvider(this).get(ClassroomTeacherMainMenuViewModel.class);
        this.viewModel.setInstitutionUser(this.getIntent().getParcelableExtra("user"));
        this.viewModel.setInstitution(this.getIntent().getParcelableExtra("institution"));
        this.viewModel.setClassroom(this.getIntent().getParcelableExtra("classroom"));
        this.viewModel.setCourse(this.getIntent().getParcelableExtra("course"));
        Log.d("Recebimento", "buildViewModel: "+this.viewModel.getInstitutionUser().getDescription()+"\n"+this.viewModel.getInstitution().getName()+"\n"+this.viewModel.getClassroom().getDescription()+"\n"+this.viewModel.getCourse().getDescription() );
        this.viewModel.getAllStudents();
        this.viewModel.getTeacherSubjects();
        binding.teachermenubutton.setOnClickListener(this.floatClickListener());
    }

    private View.OnClickListener floatClickListener() {
        return this::openMenuOptions;
    }

    private void buildMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(this.viewModel.getClassroom().getDescription());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private DefaultClickListener<CourseMember> OnTeacherClick() {
        return member -> {
            this.viewModel.setStudent(member);
            DialogTeacherSubjectChoose dialog = new DialogTeacherSubjectChoose();
            dialog.show(getSupportFragmentManager(), dialog.getTag());
        };
    }
    private Observer<? super List<CourseMember>> studentsObserver() {
        return list ->{
            this.studentAdapter.getMutableLiveDataT().setValue(list);
            this.studentAdapter.notifyDataSetChanged();
        };
    }
    private void openMenuOptions(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.teacher_classroom_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id == R.id.teacher_timetable){
                Intent i = new Intent(this, TimeTableView.class);
                i.putExtra("institution", this.viewModel.getInstitution());
                i.putExtra("course", this.viewModel.getCourse());
                i.putExtra("classroom", this.viewModel.getClassroom());
                i.putExtra("courseMember", false);
                this.startActivity(i);
            }
            return true;
        });
        popupMenu.show();
    }
}