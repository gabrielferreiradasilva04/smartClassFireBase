package com.gabriel.smartclass.viewModels;
import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.dao.SubjectDAO;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Subject;
import com.gabriel.smartclass.view.course.views.AddSubjectsOnCourse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddSubjectsOnCourseViewModel extends ViewModel {
    private final AddSubjectsOnCourse view;
    private MutableLiveData<String> snackBarText = new MutableLiveData<>();
    private Course courseEdit;
    private SimpleDefaultAdapter<Subject> adapter;

    public void setSnackBarText(MutableLiveData<String> snackBarText) {
        this.snackBarText = snackBarText;
    }

    public MutableLiveData<String> getSnackBarText() {
        return snackBarText;
    }

    public Course getCourseEdit() {
        return courseEdit;
    }

    public void setCourseEdit(Course courseEdit) {
        this.courseEdit = courseEdit;
    }

    public SimpleDefaultAdapter<Subject> getAdapter() {
        return adapter;
    }

    public void setAdapter(SimpleDefaultAdapter<Subject> adapter) {
        this.adapter = adapter;
    }

    public AddSubjectsOnCourseViewModel(AddSubjectsOnCourse view){
        this.view = view;
    }

    public void addNewSubjectOnCourse(String institutionID, String description, String minimumGrade){
        if(!description.isEmpty() && !minimumGrade.isEmpty()){
            double minimumGradeInt = Double.parseDouble(minimumGrade);
            Subject subject = new Subject();
            subject.setMinimum_grade(minimumGradeInt);
            subject.setDescription(description.toLowerCase());
            Stream<Subject> subjectStream = Objects.requireNonNull(adapter.getMutableLiveDataT().getValue()).stream().filter(object -> object.getDescription().equals(description));
            List<Subject> list = subjectStream.collect(Collectors.toList());
            saveNewSubject(institutionID, subject, list);
        }else{
            snackBarText.setValue("Preencha todos os campos");
        }
    }

    private void saveNewSubject(String institutionID, Subject subject, List<Subject> list) {
        if(list.isEmpty()){
            new CourseDAO().addSubjectsOnCourse(this.courseEdit.getId(), institutionID, subject, task -> {
                subject.setId(task.getResult().getId());
                updateSubjectID(institutionID, task);
                this.adapter.addItem(subject);
            }, e -> {
            });
        }else{
            snackBarText.setValue("Essa matéria já foi adicionada");
        }
    }

    private void updateSubjectID(String institutionID, Task<DocumentReference> task) {
        HashMap<String, Object> update = new HashMap<>();
        update.put("id", task.getResult().getId());
        new SubjectDAO().updateSubject(institutionID, courseEdit.getId(), task.getResult().getId(), update, unused ->{}, e -> {});
    }

    public void removeSubjectFromCourse(String institutionID, String courseID, Subject subject){
        new CourseDAO().removeSubjectFromCourse(institutionID,courseID,subject.getId(), task-> adapter.removeItem(subject), e -> snackBarText.setValue("Erro ao deltar matéria"));
    }
    @SuppressLint("NotifyDataSetChanged")
    public void initSubjectAdapter(String institutionID){
        this.adapter = new SimpleDefaultAdapter<>();
        new SubjectDAO().getAllSubjectsFromCourse(institutionID, courseEdit.getId(), task -> {
            if(task.isComplete() && task.isSuccessful()){
                this.adapter.getMutableLiveDataT().setValue(task.getResult().toObjects(Subject.class));
                Log.d("Retorno do adapter", "initSubjectAdapter: "+adapter.getMutableLiveDataT().getValue());
                this.adapter.notifyDataSetChanged();
            }
        }, e->{});
    }
}
