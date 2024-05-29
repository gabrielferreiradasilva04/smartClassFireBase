package com.gabriel.smartclass.adapter.spinnerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.lifecycle.MutableLiveData;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.dao.UserDAO;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.User;

import java.util.ArrayList;
import java.util.List;

public class InstitutionUserSpinnerAdapter extends BaseAdapter {
    private Context context;
    private MutableLiveData<List<InstitutionUser>> mutableLiveDataInstitutionUser;

    public MutableLiveData<List<InstitutionUser>> getMutableLiveDataInstitutionUser() {
        return mutableLiveDataInstitutionUser;
    }
    public InstitutionUserSpinnerAdapter (Context context, MutableLiveData<List<InstitutionUser>>mutableLiveData){
        this.context = context;
        this.mutableLiveDataInstitutionUser = mutableLiveData;
        this.mutableLiveDataInstitutionUser.setValue(new ArrayList<>());
    }

    @Override
    public int getCount() {
        if (this.mutableLiveDataInstitutionUser.getValue() != null) {
            return this.mutableLiveDataInstitutionUser.getValue().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(this.mutableLiveDataInstitutionUser.getValue()!=null){
            return this.mutableLiveDataInstitutionUser.getValue().get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(this.mutableLiveDataInstitutionUser.getValue()!=null){
            return position;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(this.context).inflate(R.layout.spinner_layout, parent,false);
        TextView textDescription = rootView.findViewById(R.id.text_description);
        new UserDAO().getUserByDocumentReference(mutableLiveDataInstitutionUser.getValue().get(position).getUser_id(), task->{
            User user = task.getResult().toObject(User.class);
            textDescription.setText(user.getName());
        },e->{});
        return rootView;
    }

}
