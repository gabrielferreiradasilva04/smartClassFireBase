package com.gabriel.smartclass.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.model.RankPosition;

import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter {
    private MutableLiveData<List<RankPosition>> list;

    public MutableLiveData<List<RankPosition>> getList() {
        return list;
    }

    public void setList(MutableLiveData<List<RankPosition>> list) {
        this.list = list;
    }

    public RankAdapter() {
        this.list = new MutableLiveData<>(new ArrayList<>());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_ranking_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView student_name = holder.itemView.findViewById(R.id.text_studentname);
        ImageButton button_position = holder.itemView.findViewById(R.id.button_position);
        AppCompatButton button_position_text = holder.itemView.findViewById(R.id.button_position_text);
        student_name.setText(list.getValue().get(position).getStudent());
        if (position == 0) {
            button_position.setImageResource(R.drawable.primeiro_lugar);
        } else if (position == 1) {
            button_position.setImageResource(R.drawable.segundo_lugar);
        } else if (position == 2) {
            button_position.setImageResource(R.drawable.terceiro_lugar);
        } else if (position == 3) {
            button_position.setImageResource(R.drawable.quarto_lugar);
        } else if (position == 4) {
            button_position.setImageResource(R.drawable.quinto_lugar);
        } else {
            button_position.setVisibility(View.GONE);
            button_position_text.setVisibility(View.VISIBLE);
            button_position_text.setText(Integer.toString(position + 1)+"°");
        }
}

    @Override
    public int getItemCount() {
        return this.list.getValue().size();
    }
}
