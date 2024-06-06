package com.gabriel.smartclass.view.user.views.institution;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.SimpleDefaultAdapter;
import com.gabriel.smartclass.databinding.ActivityInstitutionsMembersSearchBinding;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.viewModels.InstitutionMembersViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class InstitutionsMembersSearch extends AppCompatActivity {
    private SimpleDefaultAdapter<InstitutionUser> adapter;
    private ActivityInstitutionsMembersSearchBinding binding;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityInstitutionsMembersSearchBinding.inflate(getLayoutInflater());
        initialize();
        setContentView(binding.getRoot());
    }
    public void initialize(){
        buildMenu();
        this.adapter = new SimpleDefaultAdapter<>();
        this.recyclerView = binding.recyclerView;
        InstitutionMembersViewModel viewModel = new ViewModelProvider(this).get(InstitutionMembersViewModel.class);
        viewModel.getInstitutionUserMutableLiveData().observe(this, institutionUsersObserver());
        this.buildRecyclerView();
        viewModel.getAllInstitutionUsers(FirebaseAuth.getInstance().getCurrentUser().getUid());
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
}