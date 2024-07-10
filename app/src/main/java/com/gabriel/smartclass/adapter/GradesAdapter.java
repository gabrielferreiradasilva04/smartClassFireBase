package com.gabriel.smartclass.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.model.StudentGrade;

import java.util.ArrayList;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter{
    private MutableLiveData<List<StudentGrade>> list;

    public MutableLiveData<List<StudentGrade>> getList() {
        return list;
    }

    public void setList(MutableLiveData<List<StudentGrade>> list) {
        this.list = list;
    }

    public GradesAdapter(){
        this.list = new MutableLiveData<>(new ArrayList<>());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_grade_layout, parent, false);
        return new DefaultViewHolder(itemView);    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView subject = holder.itemView.findViewById(R.id.text_subjectname);
        TextView status = holder.itemView.findViewById(R.id.text_status);
        AppCompatButton grade_button = holder.itemView.findViewById(R.id.button_grade);
        subject.setText(this.list.getValue().get(position).getSubject().getDescription());
        status.setText(this.list.getValue().get(position).getFinalResult());
        if(this.list.getValue().get(position).getFinalResult() == null || this.list.getValue().get(position).getFinalResult().equals("") ){
            status.setText("Andamento");
        }
        grade_button.setText(Double.toString(this.list.getValue().get(position).getFinalGrade()));
    }

    @Override
    public int getItemCount() {
       return this.list.getValue().size();
    }
}
