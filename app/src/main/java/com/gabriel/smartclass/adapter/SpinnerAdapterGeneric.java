package com.gabriel.smartclass.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.model.baseEntitys.SimpleAuxEntity;

import java.util.List;

public class SpinnerAdapterGeneric<T extends SimpleAuxEntity> extends BaseAdapter {
    private Context context;
    private MutableLiveData<List<T>> mutableLiveDataTList;

    public SpinnerAdapterGeneric(Context context, MutableLiveData<List<T>> mutableLiveDataTList){
        this.context = context;
        this.mutableLiveDataTList = mutableLiveDataTList;
    }
    @Override
    public int getCount() {
        if(mutableLiveDataTList.getValue() != null){
            return mutableLiveDataTList.getValue().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mutableLiveDataTList.getValue() != null){
            return mutableLiveDataTList.getValue().get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.spinner_layout, parent,false);
        TextView textDescription = rootView.findViewById(R.id.text_description);
        textDescription.setText(this.mutableLiveDataTList.getValue().get(position).getDescription());
        return rootView;
    }
}
