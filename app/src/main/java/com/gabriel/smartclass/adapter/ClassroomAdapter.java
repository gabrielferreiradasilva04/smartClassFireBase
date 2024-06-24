package com.gabriel.smartclass.adapter;

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
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.model.Classroom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter{
    private final MutableLiveData<List<Classroom>> classrooms;
    private final HashSet<String> tIds;
    private boolean showUserDetails = false;
    private boolean addAdapter = false;
    private DefaultClickListener<Classroom> clickListener;

    public ClassroomAdapter(){
        this.classrooms = new MutableLiveData<>(new ArrayList<>());
        this.tIds = new HashSet<>();
    }

    public void setShowUserDetails(boolean showUserDetails) {
        this.showUserDetails = showUserDetails;
    }

    public boolean isAddAdapter() {
        return addAdapter;
    }

    public void setAddAdapter(boolean addAdapter) {
        this.addAdapter = addAdapter;
    }

    public boolean isShowUserDetails() {
        return showUserDetails;
    }

    public MutableLiveData<List<Classroom>> getClassrooms() {
        return classrooms;
    }

    public HashSet<String> gettIds() {
        return tIds;
    }

    public DefaultClickListener<Classroom> getClickListener() {
        return clickListener;
    }

    public void setClickListener(DefaultClickListener<Classroom> clickListener) {
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_classroom_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        TextView textDescription = holder.itemView.findViewById(R.id.text_description_rcclassroom);
        ImageButton editbutton = holder.itemView.findViewById(R.id.edit_rcclassroom);
        ImageButton deletebutton = holder.itemView.findViewById(R.id.buttondelete_rcclassroom);
        ImageButton timetable = holder.itemView.findViewById(R.id.buttonTimetable);
        textDescription.setText(this.classrooms.getValue().get(position).getDescription());
        holder.itemView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if(classrooms.getValue() != null){
            return classrooms.getValue().size();
        }
        return 0;
    }
    public void addItem(@NotNull Classroom t){
        if(!tIds.contains(t.getId())){
            tIds.add(t.getId());
            classrooms.getValue().add(t);
            this.notifyItemInserted(classrooms.getValue().indexOf(t));
        }
    }
    public void removeItem(@NonNull Classroom t){
        if(classrooms.getValue().contains(t)){
            classrooms.getValue().remove(t);
            notifyDataSetChanged();
            if (this.getItemCount() == 0) {
                notifyDataSetChanged();
            }
        }

    }
}
