package com.gabriel.smartclass.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.InstitutionLinkRequestViewHolder;
import com.gabriel.smartclass.adapter.interfaces.ApproveLinkRequestClickListener;
import com.gabriel.smartclass.adapter.interfaces.RejectLinkRequestClickListener;
import com.gabriel.smartclass.dao.LinkRequestStatusDAO;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.dao.UserTypeDAO;
import com.gabriel.smartclass.model.InstitutionLinkRequest;
import com.gabriel.smartclass.model.LinkRequestStatus;
import com.gabriel.smartclass.model.User;
import com.gabriel.smartclass.model.UserType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class InstitutionLinkRequestsAdapter extends RecyclerView.Adapter {
    private MutableLiveData<List<InstitutionLinkRequest>> institutionLinkRequestMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private HashSet<String> linkRequestsId;
    private ApproveLinkRequestClickListener approveLinkRequestClickListener;
    private RejectLinkRequestClickListener rejectLinkRequestClickListener;

    public void setRejectLinkRequestClickListener(RejectLinkRequestClickListener rejectLinkRequestClickListener) {
        this.rejectLinkRequestClickListener = rejectLinkRequestClickListener;
    }

    public MutableLiveData<List<InstitutionLinkRequest>> getInstitutionLinkRequestMutableLiveData() {
        return institutionLinkRequestMutableLiveData;
    }

    public void setInstitutionLinkRequestMutableLiveData(MutableLiveData<List<InstitutionLinkRequest>> institutionLinkRequestMutableLiveData) {
        this.institutionLinkRequestMutableLiveData = institutionLinkRequestMutableLiveData;
    }
    public void setApproveLinkRequestClickListener(ApproveLinkRequestClickListener approveLinkRequestClickListener) {
        this.approveLinkRequestClickListener = approveLinkRequestClickListener;
    }
    public InstitutionLinkRequestsAdapter() {
        linkRequestsId = new HashSet<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.institutions_link_requests_adapter, parent, false);
        return new InstitutionLinkRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AtomicInteger aux = new AtomicInteger();
        UserDAO userDAO = new UserDAO();
        UserTypeDAO userTypeDAO = new UserTypeDAO();
        MutableLiveData<User> userRequest = new MutableLiveData<>();
        MutableLiveData<UserType> userTypeRequest = new MutableLiveData<>();
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(300).start();
        userDAO.getUserByDocumentReference(Objects.requireNonNull(institutionLinkRequestMutableLiveData.getValue()).get(position).getUser(), task -> {
            if (task.isComplete() && task.isSuccessful()) {
                userRequest.setValue(task.getResult().toObject(User.class));
                aux.getAndIncrement();
                checkAndLoadAdapterDetails(holder, position, userRequest, userTypeRequest, aux);
            }
        }, e -> {
            return;
        });
        userTypeDAO.getUserTypeByDocumentReference(institutionLinkRequestMutableLiveData.getValue().get(position).getUserType(), task2 -> {
            if (task2.isComplete() && task2.isSuccessful()) {
                userTypeRequest.setValue(task2.getResult().toObject(UserType.class));
                aux.getAndIncrement();
                checkAndLoadAdapterDetails(holder, position, userRequest, userTypeRequest, aux);
            }
        }, e -> {
            return;
        });
    }

    private void checkAndLoadAdapterDetails(RecyclerView.ViewHolder holder, int position, MutableLiveData<User> userRequest, MutableLiveData<UserType> userTypeRequest, AtomicInteger aux) {
        if (aux.get() == 2) {
            if (userRequest.getValue() != null && userTypeRequest.getValue() != null) {
                loadAdapterDetails(holder, position, userRequest, userTypeRequest);
            }
        }
    }

    private void loadAdapterDetails(RecyclerView.ViewHolder holder, int position, MutableLiveData<User> userRequest, MutableLiveData<UserType> userTypeRequest) {
        holder.itemView.setAlpha(0f);
        holder.itemView.animate().alpha(1f).setDuration(400).start();
        TextView titleText = holder.itemView.findViewById(R.id.institution_link_request_adapter_title);
        TextView requestUserTypeText = holder.itemView.findViewById(R.id.institution_link_request_adapter_type_request);
        TextView requestStatusText = holder.itemView.findViewById(R.id.institution_link_request_adapter_type_request_status);
        ImageButton profilePictureUserRequest = holder.itemView.findViewById(R.id.institution_link_request_adapter_profile_picture);
        ImageButton approveButton = holder.itemView.findViewById(R.id.approve_link_request);
        ImageButton rejectButton = holder.itemView.findViewById(R.id.reject_link_request);

        titleText.setText(userRequest.getValue().getName());
        requestUserTypeText.setText(userTypeRequest.getValue().getDescription());
        if (institutionLinkRequestMutableLiveData.getValue().get(position).getLinkRequestStatus_id().equals(LinkRequestStatusDAO.APPROVED_REFERENCE)) {
            requestStatusText.setText("Aprovado");
            approveButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
        } else if (institutionLinkRequestMutableLiveData.getValue().get(position).getLinkRequestStatus_id().equals(LinkRequestStatusDAO.PENDING_REFERENCE)) {
            requestStatusText.setText("Pendente");
            approveButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            if (approveLinkRequestClickListener != null) approveButton.setOnClickListener(view -> {
                approveLinkRequestClickListener.approve(institutionLinkRequestMutableLiveData.getValue().get(position));
            });
            if (rejectLinkRequestClickListener != null) rejectButton.setOnClickListener(view -> {
                rejectLinkRequestClickListener.reject(institutionLinkRequestMutableLiveData.getValue().get(position));
            });
        } else if (institutionLinkRequestMutableLiveData.getValue().get(position).getLinkRequestStatus_id().equals(LinkRequestStatusDAO.REJECTED_REFERENCE)) {
            requestStatusText.setText("Rejeitado");
            approveButton.setVisibility(View.INVISIBLE);
            rejectButton.setVisibility(View.INVISIBLE);
        }
        Glide.with(holder.itemView.getContext()).load(userRequest.getValue().getPhotoUrl()).placeholder(R.drawable.icone_smarclass_sem_fundo).into(profilePictureUserRequest);
        holder.itemView.setVisibility(View.VISIBLE);
    }

    public void addItem(InstitutionLinkRequest linkRequest) {
        if (!linkRequestsId.contains(linkRequest.getId())) {
            linkRequestsId.add(linkRequest.getId());
            Objects.requireNonNull(institutionLinkRequestMutableLiveData.getValue()).add(linkRequest);
        }
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(institutionLinkRequestMutableLiveData.getValue()).size();
    }
}
