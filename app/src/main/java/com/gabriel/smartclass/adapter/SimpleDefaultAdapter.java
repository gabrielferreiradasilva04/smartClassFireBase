package com.gabriel.smartclass.adapter;

import android.util.Log;
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
import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SimpleDefaultAdapter<T extends SimpleAuxEntity> extends RecyclerView.Adapter{
    private final MutableLiveData<List<T>> mutableLiveDataT;
    private final HashSet<String> tIds;
    private DefaultClickListener<T> clickListener;

    public SimpleDefaultAdapter(){
        this.mutableLiveDataT = new MutableLiveData<>(new ArrayList<>());
        this.tIds = new HashSet<>();
    }

    public MutableLiveData<List<T>> getMutableLiveDataT() {
        return mutableLiveDataT;
    }

    public HashSet<String> gettIds() {
        return tIds;
    }

    public DefaultClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(DefaultClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_default_recycler_view_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        try{
            holder.itemView.setAlpha(0f);
            holder.itemView.animate().alpha(1f).setDuration(300).start();
            String description = mutableLiveDataT.getValue().get(position).getDescription();
            ImageButton buttonRemoveItem = holder.itemView.findViewById(R.id.simple_recycler_view_buttonremove);
            TextView textDescription = holder.itemView.findViewById(R.id.simple_recyclerview_title);
            textDescription.setText(description);
            if(clickListener != null){
                buttonRemoveItem.setOnClickListener(view ->{
                    clickListener.onClick(mutableLiveDataT.getValue().get(position));
                });
            }
        }catch (NullPointerException e){
            Log.d("ADAPTER ERROR", "onBindViewHolder: valor nulo encontrado no adapter");
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
        }
    }
    public void removeItem(@NonNull T t){
        if(mutableLiveDataT.getValue().contains(t)){
            int removedPosition = mutableLiveDataT.getValue().indexOf(t);
            mutableLiveDataT.getValue().remove(t);
            notifyItemRemoved(removedPosition);
            this.notifyItemRangeChanged(removedPosition, this.getItemCount());
        }

    }

}
