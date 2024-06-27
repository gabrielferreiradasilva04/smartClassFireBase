package com.gabriel.smartclass.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.adapter.interfaces.TimeTableSubjectClickListener;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("NotifyDataSetChanged")

public class TimeTableItemsRCA extends RecyclerView.Adapter {
    private MutableLiveData<List<String>> subjects;
    private TimeTableSubjectClickListener removeSubject;

    public MutableLiveData<List<String>> getSubjects() {
        return subjects;
    }

    public void setSubjects(MutableLiveData<List<String>> subjects) {
        this.subjects = subjects;
    }

    public TimeTableSubjectClickListener getRemoveSubject() {
        return removeSubject;
    }

    public void setRemoveSubject(TimeTableSubjectClickListener removeSubject) {
        this.removeSubject = removeSubject;
    }

    public TimeTableItemsRCA (){
        this.subjects = new MutableLiveData<>(new ArrayList<>());
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_timetableitem_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        String subject = this.subjects.getValue().get(position);
        ImageButton buttonRemove = holder.itemView.findViewById(R.id.buttonremove);
        TextView txtSubject = holder.itemView.findViewById(R.id.subject_string);
        txtSubject.setText(subject);
        if(this.removeSubject != null){
            buttonRemove.setOnClickListener(view -> this.removeSubject.onClick(position));
        }
    }

    @Override
    public int getItemCount() {
        if(this.subjects.getValue()!= null){
            return this.subjects.getValue().size();
        }
        return 0;
    }
    public void addItem(String subject){
        if(this.subjects.getValue() != null){
            this.subjects.getValue().add(subject);
            this.notifyDataSetChanged();
        }
    }
    public void removeItem(int index){
        if(this.subjects.getValue() != null){
            this.subjects.getValue().remove(index);
            this.notifyDataSetChanged();
        }
    }
}
