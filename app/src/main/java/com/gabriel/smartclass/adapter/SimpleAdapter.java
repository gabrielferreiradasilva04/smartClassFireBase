package com.gabriel.smartclass.adapter;

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
import com.gabriel.smartclass.model.CourseMember;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SimpleAdapter<T extends CourseMember> extends RecyclerView.Adapter {
    private final MutableLiveData<List<T>> mutableLiveDataT;
    private final HashSet<String> tIds;
    private DefaultClickListener<T> clickListener;

    public SimpleAdapter(){
        this.mutableLiveDataT = new MutableLiveData<>(new ArrayList<>());
        this.tIds = new HashSet<>();
    }

    public MutableLiveData<List<T>> getMutableLiveDataT() {
        return mutableLiveDataT;
    }

    public HashSet<String> gettIds() {
        return tIds;
    }

    public DefaultClickListener<T> getClickListener() {
        return clickListener;
    }

    public void setClickListener(DefaultClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_adapter, parent, false);
        return new DefaultViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        String description = mutableLiveDataT.getValue().get(position).getDescription();
        TextView textDescription = holder.itemView.findViewById(R.id.description);
        textDescription.setText(description);
        if(clickListener != null){
            holder.itemView.setOnClickListener(view -> clickListener.onClick(this.getMutableLiveDataT().getValue().get(position)));
        }
        holder.itemView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if(mutableLiveDataT.getValue() != null){
            return mutableLiveDataT.getValue().size();
        }
        return 0;
    }
    public void addItem(@NotNull T t){
        if(!tIds.contains(t.getId())){
            tIds.add(t.getId());
            mutableLiveDataT.getValue().add(t);
            this.notifyItemInserted(mutableLiveDataT.getValue().indexOf(t));
        }
    }
    public void removeItem(@NonNull T t){
        if(mutableLiveDataT.getValue().contains(t)){
            mutableLiveDataT.getValue().remove(t);
            notifyDataSetChanged();
            if (this.getItemCount() == 0) {
                notifyDataSetChanged();
            }
        }

    }
}
