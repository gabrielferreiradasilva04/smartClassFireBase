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
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.Institution;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class InstitutionsAdapter extends RecyclerView.Adapter {
    private MutableLiveData<List<Institution>> institutionsMutableLiveData;
    private HashSet<String> itemIds;
    private ItemClickListener itemClickListener;
    public MutableLiveData<List<Institution>> getInstitutionsMutableLiveData() {
        return institutionsMutableLiveData;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public InstitutionsAdapter(List<Institution> institutions, ItemClickListener itemClickListener){
        institutionsMutableLiveData = new MutableLiveData<>();
        institutionsMutableLiveData.setValue(institutions);
        this.itemClickListener = itemClickListener;
        itemIds = new HashSet<>();
    }
    public InstitutionsAdapter (List<Institution> institutions){
        this.institutionsMutableLiveData = new MutableLiveData<>();
        this.institutionsMutableLiveData.setValue(institutions);
        itemIds = new HashSet<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.institutions_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserDAO userDAO = new UserDAO();
        String itemId = Objects.requireNonNull(institutionsMutableLiveData.getValue()).get(position).getId();
            holder.itemView.setAlpha(0f);
            holder.itemView.animate().alpha(1f).setDuration(300).start();
            String institutionName = institutionsMutableLiveData.getValue().get(position).getName();
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
            this.institutionsMutableLiveData.getValue().add(institution);
        }
    }
    @Override
    public int getItemCount() {
        return institutionsMutableLiveData.getValue().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        TextView textInstitutionName = itemView.findViewById(R.id.textInstitutionName_adapter);
        public TextView getTextInstitutionName() {
            return textInstitutionName;
        }
        public void setTextInstitutionName(TextView textInstitutionName) {
            this.textInstitutionName = textInstitutionName;
        }
    }
    public interface ItemClickListener{
        void onItemClick(Institution institution);
    }
}
