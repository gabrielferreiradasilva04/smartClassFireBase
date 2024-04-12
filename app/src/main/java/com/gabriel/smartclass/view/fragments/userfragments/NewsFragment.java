package com.gabriel.smartclass.view.fragments.userfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.view.StudentMainMenu;

public class NewsFragment extends Fragment {


    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StudentMainMenu main = (StudentMainMenu) getActivity();
        main.updateTitle("Notícias");
        return inflater.inflate(R.layout.fragment_news, container, false);

    }
}