package com.gabriel.smartclass.observer;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.gabriel.smartclass.databinding.EmptyRequestBinding;

public class EmptyRecyclerViewObserver extends RecyclerView.AdapterDataObserver {
/*Classe que mostra o layout vazio caso o recycler view passado para ela não possua nenhum item a ser mostrado*/
    private RecyclerView recyclerView;
    private View empView;

    public EmptyRecyclerViewObserver(RecyclerView recyclerView, View emptyView){
        this.recyclerView = recyclerView;
        this.empView = emptyView;

    }

    @Override
    public void onChanged() {
        super.onChanged();
        checkEmpty();
    }


    public void checkEmpty(){
        if(recyclerView.getAdapter().getItemCount() <= 0){
            empView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            empView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
