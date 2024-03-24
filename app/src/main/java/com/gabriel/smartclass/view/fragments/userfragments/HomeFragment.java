package com.gabriel.smartclass.view.fragments.userfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.databinding.FragmentHomeBinding;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.view.UserInstitutionMenu;
import com.gabriel.smartclass.viewModels.HomeFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeFragmentViewModel homeFragmentViewModel;
    private RecyclerView institutionsRecyclerView;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeFragmentViewModel = new HomeFragmentViewModel(container.getContext());
        homeFragmentViewModel.getUserInstitutions(binding.institutionsRecyclerViewHomeFragment, new InstitutionsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Institution institution) {
                homeFragmentViewModel.setSelectedInstitution(institution);
                Intent i = new Intent(getContext(), UserInstitutionMenu.class);
                i.putExtra("institution", institution );
                startActivity(i);
            }
        });

        return binding.getRoot();

    }
}