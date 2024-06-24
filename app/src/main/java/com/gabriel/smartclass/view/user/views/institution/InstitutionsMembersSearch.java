package com.gabriel.smartclass.view.user.views.institution;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.databinding.ActivityInstitutionsMembersSearchBinding;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.viewModels.InstitutionMembersViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class InstitutionsMembersSearch extends AppCompatActivity {
    private SimpleDefaultAdapter<InstitutionUser> adapter;
    private ActivityInstitutionsMembersSearchBinding binding;
    private RecyclerView recyclerView;
    private InstitutionMembersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityInstitutionsMembersSearchBinding.inflate(getLayoutInflater());
        initialize();
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.viewModel.getSnackBarText().removeObserver(snackBarObserver());
        this.viewModel.getSnackBarText().setValue("");
        this.viewModel.getInstitutionUserMutableLiveData().removeObserver(institutionUsersObserver());
        this.adapter.setClickListener(null);
    }

    public void initialize(){
        buildMenu();
        this.adapter = new SimpleDefaultAdapter<>();
        this.adapter.setClickListener(removeItemClickListener());
        this.recyclerView = binding.recyclerView;
        viewModel = new ViewModelProvider(this).get(InstitutionMembersViewModel.class);
        viewModel.getInstitutionUserMutableLiveData().observe(this, institutionUsersObserver());
        this.buildRecyclerView();
        viewModel.getAllInstitutionUsers(FirebaseAuth.getInstance().getCurrentUser().getUid());
        this.viewModel.getSnackBarText().observe(this, snackBarObserver());
    }
    public void buildRecyclerView(){
        this.recyclerView.setHasFixedSize(false);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
    }
    @SuppressLint("NotifyDataSetChanged")
    private Observer<? super List<InstitutionUser>> institutionUsersObserver() {
        return list->{
            this.adapter.getMutableLiveDataT().setValue(list);
            this.adapter.notifyDataSetChanged();
        };
    }

    private void buildMenu() {
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Membros");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_action_bar, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private Observer<? super String> snackBarObserver() {
        return text ->{
            if(text != null && !text.equals("")){
                Snackbar.make(this, binding.recyclerView, text, Snackbar.LENGTH_SHORT).show();
            }
        };
    }
    private DefaultClickListener<InstitutionUser> removeItemClickListener() {
        return institutionUser ->{
            this.viewModel.removeMember(FirebaseAuth.getInstance().getCurrentUser().getUid(), institutionUser);
        };
    }
}