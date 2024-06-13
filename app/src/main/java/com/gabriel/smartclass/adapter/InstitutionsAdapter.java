package com.gabriel.smartclass.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;

import com.gabriel.smartclass.model.Institution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import com.gabriel.smartclass.adapter.interfaces.OnInstitutionItemClickListener;

public class InstitutionsAdapter extends RecyclerView.Adapter {
    private final MutableLiveData<List<Institution>> institutionsMutableLiveData;
    private final HashSet<String> itemIds;
    private OnInstitutionItemClickListener itemClickListener;
    public MutableLiveData<List<Institution>> getInstitutionsMutableLiveData() {
        return institutionsMutableLiveData;
    }

    public void setItemClickListener(OnInstitutionItemClickListener onInstitutionItemClickListener) {
        this.itemClickListener = onInstitutionItemClickListener;
    }

    public InstitutionsAdapter(List<Institution> institutions, OnInstitutionItemClickListener onInstitutionItemClickListener){
        institutionsMutableLiveData = new MutableLiveData<>();
        institutionsMutableLiveData.setValue(institutions);
        this.itemClickListener = onInstitutionItemClickListener;
        itemIds = new HashSet<>();
    }
    public InstitutionsAdapter (List<Institution> institutions){
        this.institutionsMutableLiveData = new MutableLiveData<>();
        this.institutionsMutableLiveData.setValue(institutions);
        itemIds = new HashSet<>();
    }
    public InstitutionsAdapter(){
        this.institutionsMutableLiveData = new MutableLiveData<>(new ArrayList<>());
        itemIds = new HashSet<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_institution_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            holder.itemView.setAlpha(0f);
            holder.itemView.animate().alpha(1f).setDuration(300).start();
            String institutionName = Objects.requireNonNull(institutionsMutableLiveData.getValue()).get(position).getName();
            String cnpj = institutionsMutableLiveData.getValue().get(position).getCnpj();
            TextView textInstitutionName = holder.itemView.findViewById(R.id.textInstitutionName_adapter);
            TextView textInstitutionCNPJ = holder.itemView.findViewById(R.id.textInstitutionCNPJ_adapter);
            ImageButton institutionProfilePicture = holder.itemView.findViewById(R.id.institutionProfilePicture);
            textInstitutionName.setText(institutionName);
            textInstitutionCNPJ.setText(cnpj);
            Glide.with(holder.itemView.getContext()).load(institutionsMutableLiveData.getValue().get(position).getPhotoUrl()).placeholder(R.drawable.icone_smarclass_sem_fundo).into(institutionProfilePicture);
            if(itemClickListener != null){
                holder.itemView.setOnClickListener( view ->{
                    itemClickListener.onItemClick(institutionsMutableLiveData.getValue().get(position));
                });
            }
    }

    public void addItem(Institution institution){
        if(!itemIds.contains(institution.getId())){
            itemIds.add(institution.getId());
            Objects.requireNonNull(this.institutionsMutableLiveData.getValue()).add(institution);
        }
    }
    @Override
    public int getItemCount() {
        return Objects.requireNonNull(institutionsMutableLiveData.getValue()).size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
