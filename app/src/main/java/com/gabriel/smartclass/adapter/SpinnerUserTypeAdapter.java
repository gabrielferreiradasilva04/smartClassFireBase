package com.gabriel.smartclass.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.UserType;

import java.util.List;

public class SpinnerUserTypeAdapter extends BaseAdapter {
    private Context context;
    private List<UserType> userTypeList;
    private MutableLiveData<List<UserType>> mutableLiveDataUserType;

    public MutableLiveData<List<UserType>> getMutableLiveDataUserType() {
        return mutableLiveDataUserType;
    }

    public SpinnerUserTypeAdapter(Context context, List<UserType> userTypeList){
        this.userTypeList = userTypeList;
        this.mutableLiveDataUserType = new MutableLiveData<>(this.userTypeList);
        this.context = context;
    }

    @Override
    public int getCount() {
        if(mutableLiveDataUserType.getValue() != null){
            return mutableLiveDataUserType.getValue().size();

        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mutableLiveDataUserType.getValue()!=null){
            return mutableLiveDataUserType.getValue().get(position);
        }
        return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.spinner_user_type_layout, parent,false);
        TextView textDescriptionUserType = rootView.findViewById(R.id.text_userType_description);
        textDescriptionUserType.setText(this.mutableLiveDataUserType.getValue().get(position).getDescription());
        return rootView;
    }
}
