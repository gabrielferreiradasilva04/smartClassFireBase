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
import com.gabriel.smartclass.model.Frequency;

import java.util.ArrayList;
import java.util.List;

public class FrequencyAdapter extends RecyclerView.Adapter {
    private MutableLiveData<List<Frequency>> list;

    public MutableLiveData<List<Frequency>> getList() {
        return list;
    }

    public void setList(MutableLiveData<List<Frequency>> list) {
        this.list = list;
    }

    public FrequencyAdapter(){
        this.list = new MutableLiveData<>(new ArrayList<>());
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_frequency_layout, parent, false);
        return new DefaultViewHolder(itemView);    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView subject = holder.itemView.findViewById(R.id.text_subjectname_frequency);
        AppCompatButton button_frequency = holder.itemView.findViewById(R.id.button_frequency);
        subject.setText(this.list.getValue().get(position).getSubject().getDescription());
        button_frequency.setText(Double.toString(this.list.getValue().get(position).getPercent())+"%");
    }

    @Override
    public int getItemCount() {
        return this.list.getValue().size();
    }
}
