package com.gabriel.smartclass.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DefaultAdapter <T extends SimpleAuxEntity> extends RecyclerView.Adapter {
    private final MutableLiveData<List<T>> mutableLiveDataT;
    private final HashSet<String> tIds;
    private boolean showUserDetails = false;
    private boolean addAdapter = false;
    private DefaultClickListener<T> clickListener;

    public DefaultAdapter(){
        this.mutableLiveDataT = new MutableLiveData<>(new ArrayList<>());
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DefaultViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        String description = mutableLiveDataT.getValue().get(position).getDescription();
        TextView textDescription = holder.itemView.findViewById(android.R.id.text1);
        textDescription.setText(description);
        if(this.clickListener != null){
            holder.itemView.setOnClickListener(view ->{this.clickListener.onClick(this.mutableLiveDataT.getValue().get(position));});
        }
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
