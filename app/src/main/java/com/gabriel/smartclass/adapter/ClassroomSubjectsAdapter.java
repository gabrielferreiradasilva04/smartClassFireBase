package com.gabriel.smartclass.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.model.ClassroomSubject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@SuppressLint("NotifyDataSetChanged")

public class ClassroomSubjectsAdapter extends RecyclerView.Adapter{
    private MutableLiveData<List<ClassroomSubject>> classroomSubjects;
    private final HashSet<String> ids;
    private DefaultClickListener<ClassroomSubject> clickListener;

    public ClassroomSubjectsAdapter() {
        this.classroomSubjects = new MutableLiveData<>(new ArrayList<>());
        this.ids = new HashSet<>();
    }

    public MutableLiveData<List<ClassroomSubject>> getClassroomSubjects() {
        return classroomSubjects;
    }

    public void setClassroomSubjects(MutableLiveData<List<ClassroomSubject>> classroomSubjects) {
        this.classroomSubjects = classroomSubjects;
    }

    public HashSet<String> getIds() {
        return ids;
    }

    public DefaultClickListener<ClassroomSubject> getClickListener() {
        return clickListener;
    }

    public void setClickListener(DefaultClickListener<ClassroomSubject> clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_class_subjects_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView subject = holder.itemView.findViewById(R.id.subjectdescription);
        TextView teacher = holder.itemView.findViewById(R.id.teachername);
        subject.setText(this.classroomSubjects.getValue().get(position).getDescription());
        teacher.setText(this.classroomSubjects.getValue().get(position).getTeacher().getDescription());
        if(this.clickListener != null){
            holder.itemView.setOnClickListener(view -> clickListener.onClick(this.classroomSubjects.getValue().get(position)));
        }
    }

    @Override
    public int getItemCount() {
        if(this.classroomSubjects.getValue() != null){
            return this.classroomSubjects.getValue().size();
        }else{
            return 0;
        }
    }
    public void addItem(ClassroomSubject classroomSubject){
        if(!this.ids.contains(classroomSubject.getId())){
            this.getClassroomSubjects().getValue().add(classroomSubject);
            this.notifyDataSetChanged();
        }
    }
    public void removeItem(ClassroomSubject classroomSubject){
        if(this.ids.contains(classroomSubject.getId())){
            this.classroomSubjects.getValue().removeIf(object -> object.getId().equals(classroomSubject.getId()));
            this.notifyDataSetChanged();
        }
    }
}
