package com.gabriel.smartclass.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.Institution;

import org.w3c.dom.Text;

import java.util.List;

public class InstitutionsAdapter extends RecyclerView.Adapter {
    private List<Institution> institutions;
    private MutableLiveData<List<Institution>> institutionsMutableLiveData;
    private ItemClickListener itemClickListener;

    public MutableLiveData<List<Institution>> getInstitutionsMutableLiveData() {
        return institutionsMutableLiveData;
    }

    public InstitutionsAdapter(List<Institution> institutions, ItemClickListener itemClickListener){
        this.institutions = institutions;
        institutionsMutableLiveData = new MutableLiveData<>();
        institutionsMutableLiveData.setValue(institutions);
        this.itemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.institutions_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String institutionName = institutionsMutableLiveData.getValue().get(position).getName();
        String cnpj = institutionsMutableLiveData.getValue().get(position).getCnpj();
        TextView textInstitutionName = holder.itemView.findViewById(R.id.textInstitutionName_adapter);
        TextView textInstitutionCNPJ = holder.itemView.findViewById(R.id.textInstitutionCNPJ_adapter);

        holder.itemView.setOnClickListener( view ->{
            itemClickListener.onItemClick(institutionsMutableLiveData.getValue().get(position));
        });

        textInstitutionName.setText(institutionName);
        textInstitutionCNPJ.setText(cnpj);

    }

    public void addItem(Institution institution){
        this.institutionsMutableLiveData.getValue().add(institution);
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
