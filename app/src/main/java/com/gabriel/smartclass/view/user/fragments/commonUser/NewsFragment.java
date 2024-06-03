package com.gabriel.smartclass.view.user.fragments.commonUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.view.user.views.commonUser.CommonUserMainMenu;

public class NewsFragment extends Fragment {


    public NewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUserMainMenu main = (CommonUserMainMenu) getActivity();
        main.updateTitle("Notícias");
        return inflater.inflate(R.layout.fragment_commonuser_news, container, false);

    }
}