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
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.UserType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@SuppressLint("NotifyDataSetChanged")

public class InstitutionUserAdapter extends RecyclerView.Adapter {
    private MutableLiveData<List<InstitutionUser>> institutionUsers;
    private HashSet<String> ids;
    private DefaultClickListener<InstitutionUser> clickListenerRemove;

    public InstitutionUserAdapter() {
        this.institutionUsers = new MutableLiveData<>(new ArrayList<>());
        this.ids = new HashSet<>();
    }

    public MutableLiveData<List<InstitutionUser>> getInstitutionUsers() {
        return institutionUsers;
    }

    public void setInstitutionUsers(MutableLiveData<List<InstitutionUser>> institutionUsers) {
        this.institutionUsers = institutionUsers;
    }

    public HashSet<String> getIds() {
        return ids;
    }

    public void setIds(HashSet<String> ids) {
        this.ids = ids;
    }

    public DefaultClickListener<InstitutionUser> getClickListenerRemove() {
        return clickListenerRemove;
    }

    public void setClickListenerRemove(DefaultClickListener<InstitutionUser> clickListenerRemove) {
        this.clickListenerRemove = clickListenerRemove;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_institutionuser_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        AtomicInteger aux = new AtomicInteger();
        UserTypeDAO userTypeDAO = new UserTypeDAO();
        MutableLiveData<UserType> userType = new MutableLiveData<>();
        userTypeDAO.getUserTypeByDocumentReference(institutionUsers.getValue().get(position).getUserType_id(), task2 -> {
            if (task2.isComplete() && task2.isSuccessful()) {
                userType.setValue(task2.getResult().toObject(UserType.class));
                aux.getAndIncrement();
                loadDetails(holder, position, userType, aux);
            }
        }, e -> {});
    }

    private void loadDetails(RecyclerView.ViewHolder holder, int position, MutableLiveData<UserType> userType, AtomicInteger aux) {
        if (aux.get() == 1 && userType != null) {
            TextView nameText = holder.itemView.findViewById(R.id.recyclerview_institutionuserlayout_name);
            TextView userTypeText = holder.itemView.findViewById(R.id.recyclerview_institutionuserlayout_usertype);
            ImageButton buttonRemove = holder.itemView.findViewById(R.id.recyclerview_institutionuserlayout_buttonremove);
            nameText.setText(this.institutionUsers.getValue().get(position).getDescription());
            userTypeText.setText(userType.getValue().getDescription());
            if(this.clickListenerRemove != null){
                buttonRemove.setOnClickListener(view -> clickListenerRemove.onClick(institutionUsers.getValue().get(position)));
            }
        }
    }
    public void addItem(InstitutionUser user){
        if(!this.ids.contains(user.getId())){
            this.institutionUsers.getValue().add(user);
            this.notifyDataSetChanged();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(InstitutionUser user){
        if(this.institutionUsers.getValue().get(this.institutionUsers.getValue().indexOf(user)) != null){
            this.institutionUsers.getValue().remove(user);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (this.institutionUsers != null) {
            return this.institutionUsers.getValue().size();
        } else {
            return 0;
        }
    }
}
